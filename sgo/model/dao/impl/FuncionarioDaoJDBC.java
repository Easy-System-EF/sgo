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
import sgo.model.dao.FuncionarioDao;
import sgo.model.entities.Cargo;
import sgo.model.entities.Funcionario;
import sgo.model.entities.Situacao;
 
public class FuncionarioDaoJDBC implements FuncionarioDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;

	public FuncionarioDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Funcionario ";
		
	@Override
	public void insert(Funcionario obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO funcionario " +
				      "(NomeFun, EnderecoFun, BairroFun, CidadeFun, " +
				       "UfFun, CepFun, DddFun, TelefoneFun, " +
				       "CpfFun, PixFun, CargoFun, SituacaoFun, ComissaoFun, AdiantamentoFun," +
				       "CargoId, SituacaoId )" + 
  				    "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 
 
  			st.setString(1, obj.getNomeFun());
 			st.setString(2, obj.getEnderecoFun());
  			st.setString(3, obj.getBairroFun());
 			st.setString(4, obj.getCidadeFun());
 			st.setString(5, obj.getUfFun());
  			st.setString(6,  obj.getCepFun());
  			st.setInt(7, obj.getDddFun());
   			st.setInt(8,  obj.getTelefoneFun());
   			st.setString(9, obj.getCpfFun());
   			st.setString(10, obj.getPixFun());
   			st.setString(11, obj.getCargoFun());
   			st.setString(12, obj.getSituacaoFun());
   			st.setDouble(13, obj.getComissaoFun());
   			st.setDouble(14, obj.getAdiantamentoFun());
   			st.setInt(15, obj.getCargo().getCodigoCargo());
   			st.setInt(16,  obj.getSituacao().getNumeroSit());
   			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigoFun(codigo);
//					System.out.println("Novo inserido: " + obj.getCodigoFun());
				}
				else 
				{	throw new DbException(classe + "Erro!!! sem inclus�o" );
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
	public void update(Funcionario obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE funcionario " +
						    "SET NomeFun = ?, EnderecoFun = ?, BairroFun = ?, CidadeFun = ?, " +
						       "UfFun = ?, CepFun = ?, DddFun = ?, TelefoneFun = ?, " +
						       "CpfFun = ?, PixFun = ?, CargoFun = ?, SituacaoFun = ?, " +
						       "ComissaoFun = ?, AdiantamentoFun = ?, CargoId = ?, SituacaoId = ? " + 
							"WHERE (CodigoFun = ?) ",
								Statement.RETURN_GENERATED_KEYS);

  			st.setString(1, obj.getNomeFun());
 			st.setString(2, obj.getEnderecoFun());
  			st.setString(3, obj.getBairroFun());
 			st.setString(4, obj.getCidadeFun());
 			st.setString(5, obj.getUfFun());
  			st.setString(6,  obj.getCepFun());
  			st.setInt(7, obj.getDddFun());
   			st.setInt(8,  obj.getTelefoneFun());
   			st.setString(9, obj.getCpfFun());
   			st.setString(10, obj.getPixFun());
   			st.setString(11, obj.getCargoFun());
   			st.setString(12, obj.getSituacaoFun());
   			st.setDouble(13, obj.getComissaoFun());
   			st.setDouble(14, obj.getAdiantamentoFun());
   			st.setInt(15, obj.getCargo().getCodigoCargo());
   			st.setInt(16,  obj.getSituacao().getNumeroSit());
   			st.setInt(17, obj.getCodigoFun());
			
 			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 				throw new DbException (classe +  "Erro!!! sem atualiza��o " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM funcionario WHERE CodigoFun = ? ", 
					Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigo);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! na� exclu�do " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public Funcionario findById(Integer codigo) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT *, cargo.*, situacao.* "
					+ "FROM funcionario " 
					+ "INNER JOIN cargo "
						+ " ON funcionario.CargoId = cargo.CodigoCargo "
					+ "INNER JOIN situacao "
						+ " ON funcionario.SituacaoId = situacao.NumeroSit "
				+ "WHERE CodigoFun = ?");
 			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			
			if (rs.next())
			{	Cargo cargo = instantiateCargo(rs);
				Situacao sit = instantiateSit(rs);
				Funcionario obj = instantiateFuncionario(rs, cargo, sit);
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
	public List<Funcionario> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, cargo.*, situacao.* " 
						+ "FROM funcionario " 
							+ "INNER JOIN cargo " 
								+ "ON funcionario.CargoId = cargo.CodigoCargo "
							+ "INNER JOIN situacao "
								+ " ON funcionario.SituacaoId = situacao.NumeroSit "
						+ "ORDER BY NomeFun ");
			
			rs = st.executeQuery();
			
			List<Funcionario> list = new ArrayList<>();
			Map<Integer, Cargo> mapCargo = new HashMap();
			Map<Integer, Situacao> mapSit = new HashMap();
			
			while (rs.next())
			{	Cargo objCargo = mapCargo.get(rs.getInt("CargoId"));
				if (objCargo == null) {
					objCargo = instantiateCargo(rs);
					mapCargo.put(rs.getInt("CargoId"), objCargo);
				}
				Situacao objSit = mapSit.get(rs.getInt("NumeroSit"));
				if (objSit == null) {
					objSit = instantiateSit(rs);
					mapSit.put(rs.getInt("NumeroSit"), objSit);
				}
				Funcionario obj = instantiateFuncionario(rs, objCargo, objSit);
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
	public List<Funcionario> findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, cargo.*, situacao.* " 
						+ "FROM funcionario " 
							+ "INNER JOIN cargo " 
								+ "ON funcionario.CargoId = cargo.CodigoCargo "
							+ "INNER JOIN situacao "
								+ " ON funcionario.SituacaoId = situacao.NumeroSit "
							+ "WHERE  NomeFun like ? "
						+ "ORDER BY NomeFun ");
			
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
			List<Funcionario> list = new ArrayList<>();
			Map<Integer, Cargo> mapCargo = new HashMap();
			Map<Integer, Situacao> mapSit = new HashMap();
			
			while (rs.next())
			{	Cargo objCargo = mapCargo.get(rs.getInt("CargoId"));
				if (objCargo == null) {
					objCargo = instantiateCargo(rs);
					mapCargo.put(rs.getInt("CargoId"), objCargo);
				}
				Situacao objSit = mapSit.get(rs.getInt("NumeroSit"));
				if (objSit == null) {
					objSit = instantiateSit(rs);
					mapSit.put(rs.getInt("NumeroSit"), objSit);
				}
				Funcionario obj = instantiateFuncionario(rs, objCargo, objSit);
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
	public List<Funcionario> findFechamento() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, cargo.*, situacao.* " 
						+ "FROM funcionario " 
							+ "INNER JOIN cargo " 
								+ "ON funcionario.CargoId = cargo.CodigoCargo "
							+ "INNER JOIN situacao "
								+ " ON funcionario.SituacaoId = situacao.NumeroSit "
							+ "Where (ComissaoFun > 0 OR AdiantamentoFun > 0) "
						+ "ORDER BY NomeFun ");
			
			rs = st.executeQuery();
			
			List<Funcionario> list = new ArrayList<>();
			Map<Integer, Cargo> mapCargo = new HashMap();
			Map<Integer, Situacao> mapSit = new HashMap();
			
			while (rs.next())
			{	Cargo objCargo = mapCargo.get(rs.getInt("CargoId"));
				if (objCargo == null) {
					objCargo = instantiateCargo(rs);
					mapCargo.put(rs.getInt("CargoId"), objCargo);
				}
				Situacao objSit = mapSit.get(rs.getInt("NumeroSit"));
				if (objSit == null) {
					objSit = instantiateSit(rs);
					mapSit.put(rs.getInt("NumeroSit"), objSit);
				}
				Funcionario obj = instantiateFuncionario(rs, objCargo, objSit);
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
	public List<Funcionario> findByAtivo(String situacao) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, cargo.*, situacao.* " + 
						 "FROM funcionario " +
							 "INNER JOIN cargo " +
								 "ON funcionario.CargoId = cargo.CodigoCargo " +
							  "INNER JOIN situacao " +
								 " ON funcionario.SituacaoId = situacao.NumeroSit " +
					 "WHERE SituacaoFun = ? " +		
						 "ORDER BY NomeFun ");
			
			st.setString(1, situacao);
			rs = st.executeQuery();
			
			List<Funcionario> fun = new ArrayList<>();
			Map<Integer, Cargo> mapCargo = new HashMap();
			Map<Integer, Situacao> mapSit = new HashMap();
			
			while (rs.next())
			{	Cargo objCargo = mapCargo.get(rs.getInt("CargoId"));
				if (objCargo == null) {
					objCargo = instantiateCargo(rs);
					mapCargo.put(rs.getInt("CargoId"), objCargo);
				}
				Situacao objSit = mapSit.get(rs.getInt("NumeroSit"));
				if (objSit == null) {
					objSit = instantiateSit(rs);
					mapSit.put(rs.getInt("NumeroSit"), objSit);
				}
				Funcionario obj = instantiateFuncionario(rs, objCargo, objSit);
 				fun.add(obj);
 			}
			return fun;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	private Funcionario instantiateFuncionario(ResultSet rs, Cargo objCargo,
							Situacao objSit) throws SQLException {
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
   		fun.setAdiantamentoFun(rs.getDouble("AdiantamentoFun"));
   		fun.setCargo(objCargo);
   		fun.setSituacao(objSit);
        return fun;
	}

	private Cargo instantiateCargo(ResultSet rs) throws SQLException {
		Cargo cargo = new Cargo();
		cargo.setCodigoCargo(rs.getInt("CodigoCargo"));
		cargo.setNomeCargo(rs.getString("NomeCargo"));
		cargo.setSalarioCargo(rs.getDouble("SalarioCargo"));
		cargo.setComissaoCargo(rs.getDouble("ComissaoCargo"));
		return cargo;
	}	

	private Situacao instantiateSit(ResultSet rs) throws SQLException {
		Situacao sit = new Situacao();
		sit.setNumeroSit(rs.getInt("NumeroSit"));
		sit.setNomeSit(rs.getString("NomeSit"));
		return sit;
	}	
}
