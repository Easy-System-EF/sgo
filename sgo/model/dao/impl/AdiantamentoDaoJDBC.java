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
import sgo.model.dao.AdiantamentoDao;
import sgo.model.entities.Adiantamento;
import sgo.model.entities.Cargo;
import sgo.model.entities.Funcionario;
import sgo.model.entities.Situacao;

public class AdiantamentoDaoJDBC implements AdiantamentoDao {

// tb entra construtor p/ conexão
	private Connection conn;

	public AdiantamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	String classe = "Adiantamento";
	
	@Override
	public void insert(Adiantamento obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO adiantamento " +
							"(DataAdi, CodFunAdi, NomeFunAdi, CargoAdi, SituacaoAdi, " +
							"ValorAdi, MesAdi, AnoAdi, ComissaoAdi, OsAdi, balcaoAdi, " +
							"TipoAdi, FuncionarioIdAdi, CargoIdAdi, SituacaoIdAdi )" +
								"VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataAdi().getTime()));
			st.setInt(2, obj.getCodFunAdi());
			st.setString(3, obj.getNomeFunAdi());
			st.setString(4, obj.getCargoAdi());
			st.setString(5, obj.getSituacaoAdi());
			st.setDouble(6, obj.getValorAdi());
			st.setInt(7, obj.getMesAdi());
			st.setInt(8, obj.getAnoAdi());
			st.setDouble(9,  obj.getComissaoAdi());
			st.setInt(10, obj.getOsAdi());
			st.setInt(11, obj.getBalcaoAdi());
			st.setString(12, obj.getTipoAdi());
			st.setInt(13, obj.getFuncionario().getCodigoFun());
			st.setInt(14, obj.getCargo().getCodigoCargo());
			st.setInt(15, obj.getSituacao().getNumeroSit());

			int rowsaffectad = st.executeUpdate();

			if (rowsaffectad > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codigo = rs.getInt(1);
					obj.setNumeroAdi(codigo);
				} else {
					throw new DbException(classe + " Erro!!! sem inclusão");
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
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM adiantamento WHERE NumeroAdi = ? ",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, codigo);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(classe + " Erro!!! naõ excluído " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Adiantamento> findPesquisa(String str) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.*, cargo.* " + 
						"FROM adiantamento " + 
							"INNER JOIN funcionario " +
								"ON adiantamento.FuncionarioIdAdi = funcionario.CodigoFun " + 
							"INNER JOIN cargo " +
								"ON adiantamento.CargoIdAdi = cargo.CodigoCargo " + 
							"INNER JOIN situacao " +
								"ON adiantamento.SituacaoIdAdi = situacao.NumeroSit " + 
						"WHERE NomeFunAdi like ? ");

			st.setString(1, str);
			rs = st.executeQuery();
			
			List<Adiantamento> adi = new ArrayList<>();

			if (rs.next()) {
				Cargo cargo = instantiateCargo(rs);
				Situacao situacao = instantiateSituacao(rs);
				Funcionario fun = instantiateFuncionario(rs, cargo, situacao);
				Adiantamento obj = instantiateAdiantamento(rs, fun, cargo, situacao);
				adi.add(obj);
			}
			return adi;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public Adiantamento findByOs(Integer os) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.*, cargo.* " + 
						"FROM adiantamento " + 
							"INNER JOIN funcionario " +
								"ON adiantamento.FuncionarioIdAdi = funcionario.CodigoFun " + 
							"INNER JOIN cargo " +
								"ON adiantamento.CargoIdAdi = cargo.CodigoCargo " + 
							"INNER JOIN situacao " +
								"ON adiantamento.SituacaoIdAdi = situacao.NumeroSit " + 
						"WHERE OsAdi = ? ");

			st.setInt(1, os);
			rs = st.executeQuery();

			if (rs.next()) {
				Cargo cargo = instantiateCargo(rs);
				Situacao situacao = instantiateSituacao(rs);
				Funcionario fun = instantiateFuncionario(rs, cargo, situacao);
				Adiantamento obj = instantiateAdiantamento(rs, fun, cargo, situacao);
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
	public Adiantamento findByBal(Integer numBal) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.*, cargo.* " + 
						"FROM adiantamento " + 
							"INNER JOIN funcionario " +
								"ON adiantamento.FuncionarioIdAdi = funcionario.CodigoFun " + 
							"INNER JOIN cargo " +
								"ON adiantamento.CargoIdAdi = cargo.CodigoCargo " + 
							"INNER JOIN situacao " +
								"ON adiantamento.SituacaoIdAdi = situacao.NumeroSit " + 
						"WHERE BalAdi = ? ");

			st.setInt(1, numBal);
			rs = st.executeQuery();

			if (rs.next()) {
				Cargo cargo = instantiateCargo(rs);
				Situacao situacao = instantiateSituacao(rs);
				Funcionario fun = instantiateFuncionario(rs, cargo, situacao);
				Adiantamento obj = instantiateAdiantamento(rs, fun, cargo, situacao);
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

//auxiliar
// tipo => "A" = vale
//         "C" = comissão
//         "B" = balcão	
	@Override
	public List<Adiantamento> findMesTipo(Integer mes, Integer ano, String tipo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.*, cargo.* " + 
						"FROM adiantamento " +
							"INNER JOIN funcionario " +
								"ON adiantamento.FuncionarioIdAdi = funcionario.CodigoFun " + 
							"INNER JOIN cargo " +
								"ON adiantamento.CargoIdAdi = cargo.CodigoCargo " +
							"INNER JOIN situacao " +
								"ON adiantamento.SituacaoIdAdi = situacao.NumeroSit " + 
							"WHERE MesAdi = ? AND AnoAdi = ? AND TipoAdi = ? " +
						"ORDER BY - DataAdi ");

			st.setInt(1, mes);
			st.setInt(2, ano);
			st.setString(3, tipo);
			rs = st.executeQuery();

			List<Adiantamento> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFun = new HashMap();
			Map<Integer, Cargo> mapCar = new HashMap();
			Map<Integer, Situacao> mapSit = new HashMap();

			while (rs.next()) {
				Cargo objCar = mapCar.get(rs.getInt("CargoIdAdi"));
				if (objCar == null) {
					objCar = instantiateCargo(rs);
					mapCar.put(rs.getInt("CargoIdAdi"), objCar);
				}	
				Situacao objSit = mapSit.get(rs.getInt("SituacaoIdAdi"));
				if (objSit == null) {
					objSit = instantiateSituacao(rs);
					mapSit.put(rs.getInt("SituacaoIdAdi"), objSit);
				}	
				Funcionario objFun = mapFun.get(rs.getInt("FuncionarioIdAdi"));
				if (objFun == null) {
					objFun = instantiateFuncionario(rs, objCar, objSit);
					mapFun.put(rs.getInt("FuncionarioIdAdi"), objFun);
				}	
				Adiantamento obj = instantiateAdiantamento(rs, objFun, objCar, objSit);
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
	public List<Adiantamento> findMes(Integer mes, Integer ano) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT *, funcionario.*, cargo.* " + 
						"FROM adiantamento " +
							"INNER JOIN funcionario " +
								"ON adiantamento.FuncionarioIdAdi = funcionario.CodigoFun " + 
							"INNER JOIN cargo " +
								"ON adiantamento.CargoIdAdi = cargo.CodigoCargo " +
							"INNER JOIN situacao " +
								"ON adiantamento.SituacaoIdAdi = situacao.NumeroSit " + 
							"WHERE MesAdi = ? AND AnoAdi = ? " +
						"ORDER BY - DataAdi ");

			st.setInt(1, mes);
			st.setInt(2, ano);
			rs = st.executeQuery();

			List<Adiantamento> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFun = new HashMap();
			Map<Integer, Cargo> mapCar = new HashMap();
			Map<Integer, Situacao> mapSit = new HashMap();

			while (rs.next()) {
				Cargo objCar = mapCar.get(rs.getInt("CargoIdAdi"));
				if (objCar == null) {
					objCar = instantiateCargo(rs);
					mapCar.put(rs.getInt("CargoIdAdi"), objCar);
				}	
				Situacao objSit = mapSit.get(rs.getInt("SituacaoIdAdi"));
				if (objSit == null) {
					objSit = instantiateSituacao(rs);
					mapSit.put(rs.getInt("SituacaoIdAdi"), objSit);
				}	
				Funcionario objFun = mapFun.get(rs.getInt("FuncionarioIdAdi"));
				if (objFun == null) {
					objFun = instantiateFuncionario(rs, objCar, objSit);
					mapFun.put(rs.getInt("FuncionarioIdAdi"), objFun);
				}	
				Adiantamento obj = instantiateAdiantamento(rs, objFun, objCar, objSit);
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

	private Adiantamento instantiateAdiantamento
			(ResultSet rs, Funcionario fun, Cargo cargo, Situacao situacao) throws SQLException {
		Adiantamento adiantamento = new Adiantamento();
		adiantamento.setNumeroAdi(rs.getInt("NumeroAdi"));
		adiantamento.setDataAdi(new java.util.Date(rs.getTimestamp("DataAdi").getTime()));
		adiantamento.setCodFunAdi(rs.getInt("CodFunAdi"));
		adiantamento.setNomeFunAdi(rs.getString("NomeFunAdi"));
		adiantamento.setCargoAdi(rs.getString("CargoAdi"));
		adiantamento.setSituacaoAdi(rs.getString("SituacaoAdi"));
		adiantamento.setValorAdi(rs.getDouble("ValorAdi"));
		adiantamento.setMesAdi(rs.getInt("MesAdi"));
		adiantamento.setAnoAdi(rs.getInt("AnoAdi"));
		adiantamento.setComissaoAdi(rs.getDouble("ComissaoAdi"));
		adiantamento.setOsAdi(rs.getInt("OsAdi"));
		adiantamento.setBalcaoAdi(rs.getInt("BalcaoAdi"));
		adiantamento.setTipoAdi(rs.getString("TipoAdi"));
		adiantamento.setFuncionario(fun);
		adiantamento.setCargo(cargo);
		adiantamento.setSituacao(situacao);
		return adiantamento;
	}

	private Funcionario instantiateFuncionario(ResultSet rs, Cargo cargo, Situacao situacao) throws SQLException {
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
		fun.setPixFun(rs.getString("PixFun"));
		fun.setCpfFun(rs.getString("CpfFun"));
		fun.setCargo(cargo);
		fun.setSituacao(situacao);
		return fun;
	}
	
	private Cargo instantiateCargo(ResultSet rs) throws SQLException {
 		Cargo car = new Cargo();
 		car.setCodigoCargo(rs.getInt("CodigoCargo"));
 		car.setNomeCargo(rs.getString("NomeCargo"));	
 		car.setSalarioCargo(rs.getDouble("SalarioCargo"));
 		car.setComissaoCargo(rs.getDouble("ComissaoCargo"));
        return car;
	}
	
	private Situacao instantiateSituacao(ResultSet rs) throws SQLException {
		Situacao sit = new Situacao();
		sit.setNumeroSit(rs.getInt("NumeroSit"));
		sit.setNomeSit(rs.getString("NomeSit"));
		return sit;
	}
	
}
