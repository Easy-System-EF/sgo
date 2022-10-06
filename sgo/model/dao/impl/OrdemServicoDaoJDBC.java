
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
import sgo.model.dao.OrdemServicoDao;
import sgo.model.entities.Orcamento;
import sgo.model.entities.OrdemServico;
 
public class OrdemServicoDaoJDBC implements OrdemServicoDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public OrdemServicoDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	String classe = "Ordem de Serviço ";

	@Override
	public void insert(OrdemServico obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO ordemServico " +
				      "(DataOS, OrcamentoOS, PlacaOS, ClienteOS, ValorOS, ParcelaOS, PrazoOS, " +
				      "PagamentoOS, DataPrimeiroPagamentoOS, NnfOS, KmOS, ObservacaoOS, " +
				      "MesOs, AnoOs, OrcamentoId )" +
   				      	"VALUES " + 
   				      		"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 
 
			st.setDate(1, new java.sql.Date(obj.getDataOS().getTime()));
			st.setInt(2, obj.getOrcamentoOS());
			st.setString(3,  obj.getPlacaOS());
			st.setString(4, obj.getClienteOS());
			st.setDouble(5, obj.getValorOS());
			st.setInt(6, obj.getParcelaOS());
			st.setInt(7, obj.getPrazoOS());
			st.setInt(8, obj.getPagamentoOS());
			st.setDate(9, new java.sql.Date(obj.getDataPrimeiroPagamentoOS().getTime()));
			st.setInt(10, obj.getNnfOS());
			st.setInt(11, obj.getKmOS());
			st.setString(12, obj.getObservacaoOS());
			st.setInt(13, obj.getMesOs());
			st.setInt(14, obj.getAnoOs());
			st.setInt(15, obj.getOrcamento().getNumeroOrc());
			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroOS(codigo);
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
	public void update(OrdemServico obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(

					"UPDATE ordemServico " +  
							"SET DataOS = ?, " +
							    "OrcamentoOS = ?, " +
							    "PlacaOS = ?, " +
							    "ClienteOS = ?, " +
							    "ValorOS = ?, " +
							    "ParcelaOS = ?, " +
							    "PrazoOS = ?, " +
							    "PagamentoOS = ?, " +
							    "DataPrimeiroPagamentoOS = ?, " +
							    "NnfOS = ?, " +
							    "KmOS = ?, " +
							    "MesOs = ?, " +
							    "AnoOs = ?, " +
							    "ObservacaoOS = ?, " +
							    "OrcamentoId = ? " +
   					"WHERE (NumeroOS = ?)",
   						Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDataOS().getTime()));
			st.setInt(2, obj.getOrcamentoOS());
			st.setString(3,  obj.getPlacaOS());
			st.setString(4, obj.getClienteOS());
			st.setDouble(5, obj.getValorOS());
			st.setInt(6, obj.getParcelaOS());
			st.setInt(7, obj.getPrazoOS());
			st.setInt(8, obj.getPagamentoOS());
			st.setDate(9, new java.sql.Date(obj.getDataPrimeiroPagamentoOS().getTime()));
			st.setInt(10, obj.getNnfOS());
			st.setInt(11, obj.getKmOS());
			st.setString(12, obj.getObservacaoOS());
			st.setInt(13, obj.getMesOs());
			st.setInt(14, obj.getAnoOs());
			st.setInt(15, obj.getOrcamento().getNumeroOrc());
 			st.setInt(16,  obj.getNumeroOS());
			
 			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 				throw new DbException ( classe + "Erro!!! sem atualização " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM ordemServico WHERE NumeroOS = ?", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigo);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ( classe + "Erro!!! naõ excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public OrdemServico findById(Integer codigo) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT ordemServico.*, orcamento.* "
				 + "FROM ordemServico " 
			 		+ "INNER JOIN orcamento " 
	 					+ "on ordemServico.OrcamentoId = orcamento.NumeroOrc " 
				+ "WHERE NumeroOS = ?");
 			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			
			while (rs.next()) 
 			{ 	 
				Orcamento orc = instantiateOrcamento(rs);
 			    OrdemServico obj = instantiateOrdemServico(rs, orc);
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
	public List<OrdemServico> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
 							 "SELECT *, orcamento.* " + 
 								"FROM ordemServico " + 
 									"INNER JOIN orcamento " +  
 										"ON ordemServico.OrcamentoId = orcamento.NumeroOrc " + 
								"ORDER BY - NumeroOS");
			
			rs = st.executeQuery();
			
 			List<OrdemServico> list = new ArrayList<>();
 			Orcamento orc = new Orcamento();
  			Map<Integer, Orcamento> mapOrc = new HashMap<>();
 			
			while (rs.next()) 
 			{ 	orc = mapOrc.get(rs.getInt("OrcamentoId"));
 				if (orc == null)
 				{ 	orc =	instantiateOrcamento(rs);
 					mapOrc.put(rs.getInt("OrcamentoId"), orc);
 				}
 			    OrdemServico obj = instantiateOrdemServico(rs, orc);
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
	public List<OrdemServico> findPlaca(String placa) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
 							 "SELECT *, orcamento.* " + 
 								"FROM ordemServico " + 
 									"INNER JOIN orcamento " +  
 										"ON ordemServico.OrcamentoId = orcamento.NumeroOrc " + 
 									"WHERE PlacaOS = ?"); 			
			
			st.setString(1, placa);
			rs = st.executeQuery();
			
 			List<OrdemServico> list = new ArrayList<>();
 			Orcamento orc = new Orcamento();
  			Map<Integer, Orcamento> mapOrc = new HashMap<>();
 			
			while (rs.next()) 
 			{ 	orc = mapOrc.get(rs.getInt("OrcamentoId"));
 				if (orc == null)
 				{ 	orc =	instantiateOrcamento(rs);
 					mapOrc.put(rs.getInt("OrcamentoId"), orc);
 				}
 			    OrdemServico obj = instantiateOrdemServico(rs, orc);
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
	public List<OrdemServico> findMesAno(Integer mes, Integer ano) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
 							 "SELECT *, orcamento.* " + 
 								"FROM ordemServico " + 
 									"INNER JOIN orcamento " +  
 										"ON ordemServico.OrcamentoId = orcamento.NumeroOrc " + 
 									"WHERE MesOS = ? AND AnoOS = ? " +
 										"ORDER BY NumeroOS"); 			
			
			st.setInt(1, mes);
			st.setInt(2, ano);
			rs = st.executeQuery();
			
 			List<OrdemServico> list = new ArrayList<>();
 			Orcamento orc = new Orcamento();
  			Map<Integer, Orcamento> mapOrc = new HashMap<>();
 			
			while (rs.next()) 
 			{ 	orc = mapOrc.get(rs.getInt("OrcamentoId"));
 				if (orc == null)
 				{ 	orc =	instantiateOrcamento(rs);
 					mapOrc.put(rs.getInt("OrcamentoId"), orc);
 				}
 			    OrdemServico obj = instantiateOrdemServico(rs, orc);
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
	
	private OrdemServico instantiateOrdemServico(ResultSet rs, Orcamento orc) throws SQLException {
 		OrdemServico os = new OrdemServico();
 		os.setNumeroOS(rs.getInt("NumeroOS"));
		os.setDataOS(new java.util.Date(rs.getTimestamp("DataOS").getTime()));
		os.setOrcamentoOS(rs.getInt("OrcamentoOS"));
		os.setPlacaOS(rs.getString("PlacaOS"));
		os.setClienteOS(rs.getString("ClienteOS"));
		os.setValorOS(rs.getDouble("ValorOS"));
		os.setParcelaOS(rs.getInt("ParcelaOS"));
		os.setPrazoOS(rs.getInt("PrazoOs"));
		os.setPagamentoOS(rs.getInt("PagamentoOS"));
		os.setDataPrimeiroPagamentoOS(new java.util.Date(rs.getTimestamp("DataPrimeiroPagamentoOS").getTime()));
		os.setNnfOS(rs.getInt("NnfOS"));
		os.setKmOS(rs.getInt("KmOS"));
		os.setObservacaoOS(rs.getString("ObservacaoOS"));
		os.setMesOs(rs.getInt("MesOs"));
		os.setAnoOs(rs.getInt("AnoOs"));
		os.setOrcamento(orc);
        return os;
	}

	private Orcamento instantiateOrcamento(ResultSet rs) throws SQLException {
 		Orcamento orc = new Orcamento();
 		orc.setNumeroOrc(rs.getInt("NumeroOrc"));
		orc.setDataOrc(new java.util.Date(rs.getTimestamp("DataOrc").getTime()));
		orc.setPlacaOrc(rs.getString("PlacaOrc"));
		orc.setKmInicialOrc(rs.getInt("KmInicialOrc"));
		orc.setKmFinalOrc(rs.getInt("KmFinalOrc"));
 		orc.setOsOrc(rs.getInt("OsOrc"));
        return orc;
	}
}
