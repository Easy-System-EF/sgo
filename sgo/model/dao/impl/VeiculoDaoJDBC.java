package sgo.model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import sgo.model.dao.VeiculoDao;
import sgo.model.entities.Veiculo;
  
public class VeiculoDaoJDBC implements VeiculoDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public VeiculoDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Veículo";
	
	@Override
	public void insert(Veiculo obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO veiculo " +
				      "(PlacaVei, KmInicialVei, KmFinalVei, ModeloVei, AnoVei)" + 
  				      "VALUES " +
				      "(?, ?, ?, ?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setString(1, obj.getPlacaVei());
 			st.setInt(2, obj.getKmInicialVei());
 			st.setInt(3, obj.getKmFinalVei());
 			st.setString(4, obj.getModeloVei());
 			st.setInt(5, obj.getAnoVei());
			 
// 			st.executeUpdate();

 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroVei(codigo);;
					System.out.println("New key inserted: " + obj.getNumeroVei());
				}
				else
				{	throw new DbException("Erro!!! " + classe + " sem inclusão" );
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
	public void update(Veiculo obj) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"UPDATE veiculo " +  
							"SET PlacaVei = ?, " +
							"KmInicialVei = ?, " +
							"KmFinalVei = ?, " +
							"ModeloVei = ?, " +
							"AnoVei = ? " +
   					"WHERE (NumeroVei = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

 			st.setString(1, obj.getPlacaVei());
 			st.setInt(2, obj.getKmInicialVei());
 			st.setInt(3, obj.getKmFinalVei());
 			st.setString(4, obj.getModeloVei());
 			st.setInt(5, obj.getAnoVei());
 			st.setInt(6,  obj.getNumeroVei());
    			
			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 		throw new DbException ( "Erro!!! " + classe + " sem atualização " + e.getMessage()); }

  		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(String placa) {
		PreparedStatement st = null;
//		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM veiculo WHERE PlacaVei = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setString(1, placa);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! " + classe + " naõ excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public List<Veiculo> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM veiculo " +
					"ORDER BY NumeroVei");
			
			rs = st.executeQuery();
			
			List<Veiculo> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	Veiculo obj = instantiateVeiculos(rs);
 					list.add(obj);
				}	
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
	public Veiculo findByPlaca(String placa) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM veiculo " +
					"WHERE PlacaVei = ? "); 		
			
			st.setString(1, placa);
			rs = st.executeQuery();
			Veiculo obj = new Veiculo();
			
			if (rs.next())
			{	if (rs != null)
				{	obj = instantiateVeiculos(rs);
					return obj;
				}	
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
	
	private Veiculo instantiateVeiculos(ResultSet rs) throws SQLException {
 		Veiculo vei = new Veiculo();
 		
 		vei.setNumeroVei(rs.getInt("NumeroVei"));
 		vei.setPlacaVei(rs.getString("PlacaVei"));	
 		vei.setKmInicialVei(rs.getInt("KmInicialVei"));
 		vei.setKmFinalVei(rs.getInt("KmFinalVei"));
 		vei.setModeloVei(rs.getString("ModeloVei"));
 		vei.setAnoVei(rs.getInt("AnoVei"));
        return vei;
	}
}
