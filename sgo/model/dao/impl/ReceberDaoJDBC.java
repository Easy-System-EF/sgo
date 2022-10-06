package sgo.model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import db.DB;
import db.DbException;
import sgcp.model.entityes.consulta.ParPeriodo;
import sgo.model.dao.ReceberDao;
import sgo.model.entities.Receber;

public class ReceberDaoJDBC implements ReceberDao {

// aq entra um construtor declarando a conexão	
	private Connection conn;
	
	public ReceberDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	Locale ptBR = new Locale("pt", "BR");
	String classe = "Receber ";

	@Override
	public void insert(Receber obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
   		try {
			st = conn.prepareStatement(
					"INSERT INTO receber " + 
						"(FuncionarioRec, ClienteRec, NomeClienteRec, " +
						"OsRec, DataOsRec, PlacaRec, ParcelaRec, FormaPagamentoRec, " +
						"ValorRec, DataVencimentoRec, DataPagamentoRec, PeriodoIdRec) " +
					"VALUES " +  
						"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
// retorna o o novo Id inserido 					
						Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getFuncionarioRec());
			st.setInt(2, obj.getClienteRec());
			st.setString(3, obj.getNomeClienteRec());
			st.setInt(4, obj.getOsRec());
			st.setDate(5, new java.sql.Date(obj.getDataOsRec().getTime()));
			st.setString(6, obj.getPlacaRec());
			st.setInt(7, obj.getParcelaRec());
			st.setString(8, obj.getFormaPagamentoRec());
			st.setDouble(9, obj.getValorRec());
			st.setDate(10, new java.sql.Date(obj.getDataVencimentoRec().getTime()));
				st.setDate(11, new java.sql.Date(obj.getDataPagamentoRec().getTime()));
			st.setInt(12, obj.getPeriodo().getIdPeriodo());
   			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroRec(codigo);
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
	public void update(Receber obj) {
		PreparedStatement st = null;
 		try {
			st = conn.prepareStatement(
					
 					"UPDATE receber " +
						"SET FuncionarioRec = ?, " +
							"ClienteRec = ?, " +
							"NomeClienteRec = ?, " +
							"OsRec = ?, " + 								
							"DataOsRec = ?, " +	
							"PlacaRec = ?, " +
							"ParcelaRec = ?, " +
							"FormaPagamentoRec = ?, " +
							"ValorRec = ?, " +
							"DataVencimentoRec = ?, " +
							"DataPagamentoRec = ?, " +
							"PeriodoIdRec = ? " +
 						"WHERE (NumeroRec = ? ) ",
								Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getFuncionarioRec());
			st.setInt(2, obj.getClienteRec());
			st.setString(3, obj.getNomeClienteRec());
			st.setInt(4, obj.getOsRec());
			st.setDate(5, new java.sql.Date(obj.getDataOsRec().getTime()));
			st.setString(6, obj.getPlacaRec());
			st.setInt(7, obj.getParcelaRec());
			st.setString(8, obj.getFormaPagamentoRec());
			st.setDouble(9, obj.getValorRec());
			st.setDate(10, new java.sql.Date(obj.getDataVencimentoRec().getTime()));
			st.setDate(11, new java.sql.Date(obj.getDataPagamentoRec().getTime()));
			st.setInt(12, obj.getPeriodo().getIdPeriodo());   			
 			st.setInt(13, obj.getNumeroRec());
   			
			st.executeUpdate();
			
    		}
 		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void removeOS(Integer osRec) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM receber WHERE OsRec = ? ", 
						Statement.RETURN_GENERATED_KEYS);
 			st.setInt(1, osRec);
 						
 			st.executeUpdate();
 			
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro receber !!! sem exclusão " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}
 
	private Receber instantiateReceber(ResultSet rs, ParPeriodo per) throws SQLException {
		Receber obj = new Receber();
		obj.setNumeroRec(rs.getInt("NumeroRec"));
		obj.setFuncionarioRec(rs.getInt("FuncionarioRec"));
		obj.setClienteRec(rs.getInt("ClienteRec"));
		obj.setNomeClienteRec(rs.getString("NomeClienteRec"));
		obj.setOsRec(rs.getInt("OsRec"));
		obj.setDataOsRec(new java.util.Date(rs.getTimestamp("DataOsRec").getTime()));
		obj.setPlacaRec(rs.getString("PlacaRec"));
		obj.setParcelaRec(rs.getInt("ParcelaRec"));
		obj.setFormaPagamentoRec(rs.getString("FormaPagamentoRec"));
		obj.setValorRec(rs.getDouble("ValorRec"));
		obj.setDataVencimentoRec(new java.util.Date(rs.getTimestamp("DataVencimentoRec").getTime()));
		obj.setDataPagamentoRec(new java.util.Date(rs.getTimestamp("DataPagamentoRec").getTime()) );
//objetos montados (sem id e com a associação la da classe Fornrecedor fornecedor...				
		obj.setPeriodo(per);
  		return obj;
	}

	private ParPeriodo instantiateParPeriodo(ResultSet rs) throws SQLException {
		ParPeriodo per = new ParPeriodo();
		per.setIdPeriodo(rs.getInt("IdPeriodo"));
		per.setDtiPeriodo(new java.util.Date(rs.getTimestamp("DtiPeriodo").getTime()));
		per.setDtfPeriodo(new java.util.Date(rs.getTimestamp("DtfPeriodo").getTime()));
   		return per;
	}
 
 	@Override
	public List<Receber> findByAllOs(Integer os) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
 					"SELECT *, parPeriodo.* " +  
 					    	"FROM receber "  +
 					    		"INNER JOIN parPeriodo " +
 					    			"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " + 
 							"Where OsRec = ? " +
								"ORDER BY DataVencimentoRec, ParcelaRec ");

			st.setInt(1, os);
			rs = st.executeQuery();
 			 
 			List<Receber> list = new ArrayList<>();
 			
			while (rs.next()) 
 			{ 	ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
   				list.add(obj);
  			}
 			return list;
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
	public List<Receber> findAllAberto() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
 					"SELECT *, parPeriodo.* " +  
				    	"FROM receber "  +
				    		"INNER JOIN parPeriodo " +
				    			"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " + 
						"WHERE DataPagamentoRec = 0000-00-00 " +
							"ORDER BY DataVencimentoRec ");

 			rs = st.executeQuery();
 			 
 			List<Receber> list = new ArrayList();
 			
			while (rs.next()) 
 			{ 	ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
   				list.add(obj);
  			}
 			return list;
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
	public List<Receber> findAllPago() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
 					"SELECT * " +  
				    	"FROM receber "  +
				    		"INNER JOIN parPeriodo " +
				    			"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " + 
						"WHERE DataPagamentoRec > 0000-00-00 " +
							"ORDER BY DataVencimentoRec, ParcelaRec ");

 			rs = st.executeQuery();
 			 
 			List<Receber> list = new ArrayList();
 			
			while (rs.next()) 
 			{ 	ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
   				list.add(obj);
  			}
 			return list;
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
	public List<Receber> findPeriodoAberto() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
 			st = conn.prepareStatement(
  					"SELECT * " +  
  						"FROM receber "  +
  							"INNER JOIN parPeriodo " +
  								"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " +
						"WHERE DataOsRec >= DataPagamentoRec AND " +
  							"receber.DataVencimentoRec >= parPeriodo.DtiPeriodo AND " +
							"receber.DataVencimentoRec <= parPeriodo.DtfPeriodo " +
						"ORDER BY DataVencimentoRec, ParcelaRec ");
 				
 			rs = st.executeQuery();
			
			List<Receber> list = new ArrayList<>();
 			
			while (rs.next()) 
 			{ 	ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
   				list.add(obj);
  			}	
  			return list;
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
	public List<Receber> findPeriodoPago() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
 			st = conn.prepareStatement(

 					"SELECT * " +  
 		  				"FROM receber "  +
 		  					"INNER JOIN parPeriodo " +
 		  						"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " +
						"WHERE  DataPagamentoRec >= DataOsRec AND " +
  							"receber.DataVencimentoRec >= parPeriodo.DtiPeriodo AND " +
  							"receber.DataVencimentoRec <= parPeriodo.DtfPeriodo " +
						"ORDER BY DataVencimentoRec, ParcelaRec ");
    
 			rs = st.executeQuery();
			
			List<Receber> list = new ArrayList<>();
 			
			while (rs.next()) 
 			{ 	ParPeriodo per = instantiateParPeriodo(rs);
				Receber obj = instantiateReceber(rs, per);
   				list.add(obj);
  			}
 			return list;
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
	public List<Receber> findByIdClienteAberto(Integer cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
 					"SELECT *, parPeriodo.* " +  
 					    	"FROM receber "  +
 					    		"INNER JOIN parPeriodo " +
 					    			"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " + 
						"WHERE ClienteRec = ? AND DataPagamentoRec = 0000-00-00 " +
					"ORDER BY DataVencimentoRec, ParcelaRec");
 			
  			st.setInt(1, cod);
			rs = st.executeQuery();
			
			List<Receber> list = new ArrayList<>();
  
  			while (rs.next()) 
 			{ 	ParPeriodo per = instantiateParPeriodo(rs);
  				Receber obj = instantiateReceber(rs, per);
  				list.add(obj);
   			}
  			return list;
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
	public List<Receber> findByIdClientePago(Integer cod) {
		PreparedStatement st = null;
		ResultSet rs = null;
 		try {
			st = conn.prepareStatement(
 
 					"SELECT *, parPeriodo.* " +  
 					    "FROM receber "  +
 					    	"INNER JOIN parPeriodo " +
 					    		"ON receber.PeriodoIdRec = parPeriodo.IdPeriodo " + 
						"WHERE ClienteRec = ? AND DataPagamentoRec > 0000-00-00 " +
							"ORDER BY DataVencimentoRec, ParcelaRec");
 			
  			st.setInt(1, cod);
			rs = st.executeQuery();
			
			List<Receber> list = new ArrayList<>();
  
  			while (rs.next()) 
 			{ 	ParPeriodo per = instantiateParPeriodo(rs);
  				Receber obj = instantiateReceber(rs, per);
  				list.add(obj);
   			}
  			return list;
 		}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
 	}
 }
