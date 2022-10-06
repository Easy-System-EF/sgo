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
import sgo.model.dao.BalcaoDao;
import sgo.model.entities.Balcao;
import sgo.model.entities.Funcionario;

public class BalcaoDaoJDBC implements BalcaoDao {

// tb entra construtor p/ conexão
	private Connection conn;

	public BalcaoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	String classe = "Balcao ";

	@Override
	public void insert(Balcao obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO balcao " + "(DataBal, FuncionarioBal, DescontoBal, TotalBal, " +
							"PagamentoBal, DataPrimeiroPagamentoBal, NnfBal, ObservacaoBal, " + 
							"MesBal, AnoBal, FuncionarioIdBal ) "
							+ "VALUES " + 
							"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
					Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataBal().getTime()));
			st.setString(2, obj.getFuncionarioBal());
			st.setDouble(3, obj.getDescontoBal());
			st.setDouble(4, obj.getTotalBal());
			st.setInt(5, obj.getPagamentoBal());
			st.setDate(6, new java.sql.Date(obj.getDataPrimeiroPagamentoBal().getTime()));
			st.setInt(7, obj.getNnfBal());
			st.setString(8, obj.getObservacaoBal());
			st.setInt(9, obj.getMesBal());
			st.setInt(10, obj.getAnoBal());
			st.setInt(11, obj.getFuncionario().getCodigoFun());

			int rowsaffectad = st.executeUpdate();

			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setNumeroBal(codigo);
				} else {
					throw new DbException(classe + "Erro!!! sem inclusão");
				}
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Balcao obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"UPDATE balcao " + 
							"SET DataBal = ?, " +
							"FuncionarioBal = ?, " +
							"DescontoBal = ?, " + 
							"TotalBal = ?, " + 
							"PagamentoBal  = ?, " +
							"DataPrimeiroPagamentoBal = ?, " + 
							"NnfBal = ?, " + 
							"ObservacaoBal = ?, " +  
							"MesBal = ?, " + 
							"AnoBal = ?, " + 
							"FuncionarioIdBal  = ? " +    					
					 "WHERE (NumeroBal = ? )",

					Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataBal().getTime()));
			st.setString(2, obj.getFuncionarioBal());
			st.setDouble(3, obj.getDescontoBal());
			st.setDouble(4, obj.getTotalBal());
			st.setInt(5, obj.getPagamentoBal());
			st.setDate(6, new java.sql.Date(obj.getDataPrimeiroPagamentoBal().getTime()));
			st.setInt(7, obj.getNnfBal());
			st.setString(8, obj.getObservacaoBal());
			st.setInt(9, obj.getMesBal());
			st.setInt(10, obj.getAnoBal());
			st.setInt(11, obj.getFuncionario().getCodigoFun());
			st.setInt(12, obj.getNumeroBal());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM balcao WHERE NumeroBal = ? ", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, codigo);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(classe + "Erro!!! naõ excluído " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Balcao findById(Integer codigo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT balcao.*, funcionario.* " + 
							"FROM balcao " + 
								"INNER JOIN funcionario " +
									"ON balcao.FuncionarioIdBal = funcionario.CodigoFun " + 
					"WHERE NumeroBal = ?");

			st.setInt(1, codigo);
			rs = st.executeQuery();

			if (rs.next()) {
				Funcionario fun = instantiateFuncionario(rs);
				Balcao obj = instantiateBalcao(rs, fun);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Balcao> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					 "SELECT balcao.*, funcionario.* " +
							   "FROM balcao " +
						 		 "INNER JOIN funcionario " +
					 				 "ON balcao.FuncionarioIdBal = funcionario.CodigoFun " +
								 "ORDER BY - NumeroBal");
			
			rs = st.executeQuery();
			
 			List<Balcao> list = new ArrayList();
 			Funcionario fun = new Funcionario();
  			Map<Integer, Funcionario> mapFun = new HashMap<>();
 			
			while (rs.next()) 
 			{ 	fun = mapFun.get(rs.getInt("FuncionarioIdBal"));
  			    if (fun == null)
  			    { 	fun = instantiateFuncionario(rs);
  			    	mapFun.put(rs.getInt("FuncionarioIdBal"),fun);
  			    }
 			    Balcao obj = instantiateBalcao(rs, fun);
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
	public List<Balcao> findMesAno(Integer mm, Integer aa) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					 "SELECT balcao.*, funcionario.* " +
							   "FROM balcao " +
						 		 "INNER JOIN funcionario " +
					 				 "ON balcao.FuncionarioIdBal = funcionario.CodigoFun " +
						 		 "WHERE anoBal = ? AND mesBal = ? " +
							"ORDER BY NumeroBal");
			
			st.setInt(1, aa);
			st.setInt(2, mm);
			
			rs = st.executeQuery();
			
 			List<Balcao> list = new ArrayList();
 			Funcionario fun = new Funcionario();
  			Map<Integer, Funcionario> mapFun = new HashMap<>();
 			
			while (rs.next()) 
 			{ 	fun = mapFun.get(rs.getInt("FuncionarioIdBal"));
  			    if (fun == null)
  			    { 	fun = instantiateFuncionario(rs);
  			    	mapFun.put(rs.getInt("FuncionarioIdBal"),fun);
  			    }
 			    Balcao obj = instantiateBalcao(rs, fun);
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
	
	private Balcao instantiateBalcao(ResultSet rs, Funcionario fun) throws SQLException {
		Balcao bal = new Balcao();
		bal.setNumeroBal(rs.getInt("NumeroBal"));
		bal.setDataBal(new java.util.Date(rs.getTimestamp("DataBal").getTime()));
		bal.setFuncionarioBal(rs.getString("FuncionarioBal"));
		bal.setDescontoBal(rs.getDouble("DescontoBal"));
		bal.setTotalBal(rs.getDouble("TotalBal"));
		bal.setPagamentoBal(rs.getInt("PagamentoBal"));
		bal.setDataPrimeiroPagamentoBal(new java.util.Date(rs.getTimestamp("DataPrimeiroPagamentoBal").getTime()));
		bal.setNnfBal(rs.getInt("NnfBal"));
		bal.setObservacaoBal(rs.getString("ObservacaoBal"));
		bal.setMesBal(rs.getInt("MesBal"));
		bal.setAnoBal(rs.getInt("AnoBal"));
		bal.setFuncionario(fun);
		return bal;
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
		fun.setAdiantamentoFun(rs.getDouble("AdiantamentoFun"));
//		fun.setCargo(objCargo);
//		fun.setSituacao(objSit);
		return fun;
	}
}
