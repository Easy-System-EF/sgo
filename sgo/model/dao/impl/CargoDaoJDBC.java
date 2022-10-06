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
import sgo.model.dao.CargoDao;
import sgo.model.entities.Cargo;
  
public class CargoDaoJDBC implements CargoDao {
	
// tb entra construtor p/ conex�o
	private Connection conn;
	
	public CargoDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Cargo ";
	
	@Override
	public void insert(Cargo obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO cargo " +
				      "(NomeCargo, SalarioCargo, ComissaoCargo )" + 
  				      "VALUES " +
				      "(?, ?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setString(1, obj.getNomeCargo());
 			st.setDouble(2, obj.getSalarioCargo());
 			st.setDouble(3, obj.getComissaoCargo());
			 
// 			st.executeUpdate();

 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigoCargo(codigo);;
//					System.out.println("New key inserted: " + obj.getCodigoCargo());
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
	public void update(Cargo obj) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"UPDATE cargo " +  
							"SET NomeCargo = ?, SalarioCargo = ?, ComissaoCargo = ? " +
   					"WHERE (CodigoCargo = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

 			st.setString(1, obj.getNomeCargo());
 			st.setDouble(2, obj.getSalarioCargo());
 			st.setDouble(3, obj.getComissaoCargo());
 			st.setInt(4, obj.getCodigoCargo());
    			
			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 		throw new DbException (classe + "Erro!!! sem atualiza��o " + e.getMessage()); }

  		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigoCar) {
		PreparedStatement st = null;
//		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM cargo WHERE CodigoCargo = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigoCar);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! na� exclu�do " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public Cargo findById(Integer codigo) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM cargo " +
 				 "WHERE CodigoCargo = ?");
 			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			if (rs.next())
			{	if (rs != null)
				{	Cargo obj = instantiateCargos(rs);
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
	@Override
	public List<Cargo> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM cargo " +
					"ORDER BY NomeCargo");
			
			rs = st.executeQuery();
			
			List<Cargo> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	Cargo obj = instantiateCargos(rs);
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
	
	private Cargo instantiateCargos(ResultSet rs) throws SQLException {
 		Cargo car = new Cargo(); 		
 		car.setCodigoCargo(rs.getInt("CodigoCargo"));
 		car.setNomeCargo(rs.getString("NomeCargo"));	
 		car.setSalarioCargo(rs.getDouble("SalarioCargo"));
 		car.setComissaoCargo(rs.getDouble("ComissaoCargo"));
        return car;
	}
}
