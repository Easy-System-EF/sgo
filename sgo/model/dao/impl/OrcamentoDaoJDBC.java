package sgo.model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import sgo.model.dao.OrcamentoDao;
import sgo.model.entities.Cliente;
import sgo.model.entities.Funcionario;
import sgo.model.entities.Orcamento;
 
public class OrcamentoDaoJDBC implements OrcamentoDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public OrcamentoDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Orçamento ";
	
	@Override
	public void insert(Orcamento obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO orcamento " +
				      "(DataOrc, ClienteOrc, FuncionarioOrc, PlacaOrc, KmInicialOrc, " +
				       "KmFinalOrc, DescontoOrc, TotalOrc, OsOrc, ClienteId, FuncionarioId) " +
   				       "VALUES " +
				       "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setDate(1, new java.sql.Date(obj.getDataOrc().getTime()));
			st.setString(2, obj.getClienteOrc());
			st.setString(3, obj.getFuncionarioOrc());
			st.setString(4,  obj.getPlacaOrc());
			st.setInt(5, obj.getKmInicialOrc());
			st.setInt(6,  obj.getKmFinalOrc());
			st.setDouble(7, obj.getDescontoOrc());
			
			st.setDouble(8, obj.getTotalOrc());
			st.setInt(9, obj.getOsOrc());
			st.setInt(10, obj.getCliente().getCodigoCli());
			st.setInt(11, obj.getFuncionario().getCodigoFun());
 
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroOrc(codigo);
				}
				else
				{	throw new DbException(classe + "Erro!!! sem inclusão" );
				}
			}	
  		}
 		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
 
	@Override
	public void update(Orcamento obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE orcamento " +  
							"SET DataOrc = ?, " +
							 "ClienteOrc = ?, " +
							 "FuncionarioOrc = ?, " +
							 "PlacaOrc = ?, " +
							 "KmInicialOrc = ?, " +
							 "KmFinalOrc = ?, " +
							 "DescontoOrc = ?, " +
							 "TotalOrc = ?, " +
							 "OsOrc = ?, " +
							 "ClienteId = ?, " +
							 "FuncionarioId = ? " +
   					 "WHERE NumeroOrc = ? ",
        			 Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataOrc().getTime()));
			st.setString(2, obj.getClienteOrc());
			st.setString(3, obj.getFuncionarioOrc());
			st.setString(4,  obj.getPlacaOrc());
			st.setInt(5, obj.getKmInicialOrc());
			st.setInt(6,  obj.getKmFinalOrc());
			st.setDouble(7, obj.getDescontoOrc());
			st.setDouble(8, obj.getTotalOrc());
			st.setInt(9, obj.getOsOrc());
			st.setInt(10, obj.getCliente().getCodigoCli());
			st.setInt(11, obj.getFuncionario().getCodigoFun());
			st.setInt(12, obj.getNumeroOrc());
			
 			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 				throw new DbException (classe + "Erro!!! sem atualização " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM orcamento WHERE NumeroOrc = ? ", 
						Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigo);
			st.executeUpdate();
   		}
  		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! naõ excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public Orcamento findById(Integer codigo) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT orcamento.*, cliente.*, funcionario.* " +
				   "FROM orcamento " +
			 		 "INNER JOIN cliente " +
	 					 "on orcamento.ClienteId = cliente.CodigoCli " + 
			 		 "INNER JOIN funcionario " +
		 				 "ON orcamento.FuncionarioId = funcionario.CodigoFun " +
				 "WHERE NumeroOrc = ?");
 			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			
			while (rs.next()) 
 			{ 	 
 			    Cliente cli = instantiateCliente(rs);
 			    Funcionario fun = instantiateFuncionario(rs);
 			    Orcamento obj = instantiateOrcamento(rs, cli, fun);
  				return obj;
 			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
 	}

	@Override
	public Orcamento findByOS(Integer os) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT orcamento.*, cliente.*, funcionario.* " +
				   "FROM orcamento " +
			 		 "INNER JOIN cliente " +
	 					 "on orcamento.ClienteId = cliente.CodigoCli " + 
			 		 "INNER JOIN funcionario " +
		 				 "ON orcamento.FuncionarioId = funcionario.CodigoFun " +
				 "WHERE OsOrc = ?");
 			
			st.setInt(1, os);
			rs = st.executeQuery();
			
			while (rs.next()) 
 			{ 	 
 			    Cliente cli = instantiateCliente(rs);
 			    Funcionario fun = instantiateFuncionario(rs);
 			    Orcamento obj = instantiateOrcamento(rs, cli, fun);
  				return obj;
 			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
 	}

	@Override
	public Orcamento findByPlaca(String placa) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT orcamento.*, cliente.*, funcionario.* " +
				   "FROM orcamento " +
			 		 "INNER JOIN cliente " +
	 					 "on orcamento.ClienteId = cliente.CodigoCli " + 
			 		 "INNER JOIN funcionario " +
		 				 "ON orcamento.FuncionarioId = funcionario.CodigoFun " +
				 "WHERE PlacaOrc = ?");
 			
			st.setString(1, placa);
			rs = st.executeQuery();
			
			while (rs.next()) 
 			{ 	 
 			    Cliente cli = instantiateCliente(rs);
 			    Funcionario fun = instantiateFuncionario(rs);
 			    Orcamento obj = instantiateOrcamento(rs, cli, fun);
  				return obj;
 			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
 	}
	
	@Override
	public List<Orcamento> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					 "SELECT orcamento.*, cliente.*, funcionario.* " +
							   "FROM orcamento " +
						 		 "INNER JOIN cliente " +
				 					 "on orcamento.ClienteId = cliente.CodigoCli " + 
						 		 "INNER JOIN funcionario " +
					 				 "ON orcamento.FuncionarioId = funcionario.CodigoFun " +
								 "ORDER BY - NumeroOrc");
			
			rs = st.executeQuery();
			
 			List<Orcamento> list = new ArrayList();
 			Cliente cli = new Cliente();
 			Funcionario fun = new Funcionario();
  			Map<Integer, Cliente> mapCli = new HashMap<>();
  			Map<Integer, Funcionario> mapFun = new HashMap<>();
 			
			while (rs.next()) 
 			{ 	cli = mapCli.get(rs.getInt("ClienteId"));
 				if (cli == null)
 				{ 	cli =	instantiateCliente(rs);
 					mapCli.put(rs.getInt("ClienteId"), cli);
 				}
  			    fun = mapFun.get(rs.getInt("FuncionarioId"));
  			    if (fun == null)
  			    { 	fun = instantiateFuncionario(rs);
  			    	mapFun.put(rs.getInt("FuncionarioId"),fun);
  			    }
 			    Orcamento obj = instantiateOrcamento(rs, cli, fun);
  				list.add(obj);
 			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	@Override
	public List<Orcamento> findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					 "SELECT orcamento.*, cliente.*, funcionario.* " +
							   "FROM orcamento " +
						 		 "INNER JOIN cliente " +
				 					 "on orcamento.ClienteId = cliente.CodigoCli " + 
						 		 "INNER JOIN funcionario " +
					 				 "ON orcamento.FuncionarioId = funcionario.CodigoFun " +
						 		 "WHERE OsOrc = 0 AND PlacaOrc like ? " +
								"ORDER BY - NumeroOrc");
			
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
 			List<Orcamento> list = new ArrayList();
 			Cliente cli = new Cliente();
 			Funcionario fun = new Funcionario();
  			Map<Integer, Cliente> mapCli = new HashMap<>();
  			Map<Integer, Funcionario> mapFun = new HashMap<>();
 			
			while (rs.next()) 
 			{ 	cli = mapCli.get(rs.getInt("ClienteId"));
 				if (cli == null)
 				{ 	cli =	instantiateCliente(rs);
 					mapCli.put(rs.getInt("ClienteId"), cli);
 				}
  			    fun = mapFun.get(rs.getInt("FuncionarioId"));
  			    if (fun == null)
  			    { 	fun = instantiateFuncionario(rs);
  			    	mapFun.put(rs.getInt("FuncionarioId"),fun);
  			    }
 			    Orcamento obj = instantiateOrcamento(rs, cli, fun);
  				list.add(obj);
 			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	private Orcamento instantiateOrcamento(ResultSet rs, Cliente cli, Funcionario fun) throws SQLException {
 		Orcamento orc = new Orcamento();
 		orc.setNumeroOrc(rs.getInt("NumeroOrc"));
		orc.setDataOrc(new java.util.Date(rs.getTimestamp("DataOrc").getTime()));
		orc.setClienteOrc(rs.getString("ClienteOrc"));
		orc.setFuncionarioOrc(rs.getString("FuncionarioOrc"));
		orc.setPlacaOrc(rs.getString("PlacaOrc"));
		orc.setKmInicialOrc(rs.getInt("KmInicialOrc"));
		orc.setKmFinalOrc(rs.getInt("KmFinalOrc"));
		orc.setDescontoOrc(rs.getDouble("DescontoOrc"));
		orc.setTotalOrc(rs.getDouble("TotalOrc"));
 		orc.setOsOrc(rs.getInt("OsOrc"));
		orc.setCliente(cli);
		orc.setFuncionario(fun); 
        return orc;
	}

	private Cliente instantiateCliente(ResultSet rs) throws SQLException {
 		Cliente cli = new Cliente();
 		cli.setCodigoCli(rs.getInt("CodigoCli"));
 		cli.setNomeCli(rs.getString("NomeCli"));	
 		cli.setRuaCli(rs.getString("RuaCli"));
 		cli.setNumeroCli(rs.getInt("NumeroCli"));
 		cli.setComplementoCli(rs.getString("ComplementoCli"));
 		cli.setBairroCli(rs.getString("BairroCli"));
 		cli.setCidadeCli(rs.getString("CidadeCli")); 
 		cli.setUfCli(rs.getString("UFCli"));
 		cli.setCepCli(rs.getString("CepCli"));
 		cli.setDdd01Cli(rs.getInt("Ddd01Cli"));
  		cli.setTelefone01Cli(rs.getInt("Telefone01Cli"));
 		cli.setDdd02Cli(rs.getInt("Ddd02Cli"));
  		cli.setTelefone02Cli(rs.getInt("Telefone02Cli"));
  		cli.setEmailCli(rs.getString("EmailCli"));
   		cli.setPessoaCli(rs.getString("PessoaCli").charAt(0));
   		cli.setCpfCli(rs.getString("CpfCli"));
   		cli.setCnpjCli(rs.getString("CnpjCli"));
        return cli;
	}

 	private Funcionario instantiateFuncionario(ResultSet rs) throws SQLException {
 		Funcionario fun = new Funcionario();
 		
 		fun.setCodigoFun(rs.getInt("CodigoFun"));
 		fun.setNomeFun(rs.getString("NomeFun"));	
 		fun.setEnderecoFun(rs.getString("EnderecoFun"));
  		fun.setBairroFun(rs.getString("BairroFun"));
 		fun.setCidadeFun(rs.getString("CidadeFun")); 
 		fun.setUfFun(rs.getString("UfFun"));
 		fun.setCepFun(rs.getString("CepFun"));
 		fun.setDddFun(rs.getInt("DddFun"));
  		fun.setTelefoneFun(rs.getInt("TelefoneFun"));
   		fun.setCpfFun(rs.getString("CpfFun"));
   		fun.setPixFun(rs.getString("PixFun"));
   		fun.setCargoFun(rs.getString("CargoFun"));
   		fun.setSituacaoFun(rs.getString("SituacaoFun"));
   		fun.setComissaoFun(rs.getDouble("ComissaoFun"));
        return fun;
	}
}
