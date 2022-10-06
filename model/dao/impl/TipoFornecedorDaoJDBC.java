package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.TipoFornecedorDao;
import sgcp.model.entityes.TipoFornecedor;
 
public class TipoFornecedorDaoJDBC implements TipoFornecedorDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public TipoFornecedorDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(TipoFornecedor obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO tipoFornecedor " +
				      "(NomeTipo)" + 
  				      "VALUES " +
				      "(?)",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setString(1, obj.getNomeTipo());
			
// 			st.executeUpdate();

 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigoTipo(codigo);;
//					System.out.println("New key inserted: " + obj.getCodigoTipo());
				}
				else
				{	throw new DbException("Erro!!! sem inclusão" );
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
	public void update(TipoFornecedor obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE tipoFornecedor " +  
							"SET NomeTipo = ? " +
   					"WHERE (CodigoTipo = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

 			st.setString(1, obj.getNomeTipo());
 			st.setInt(2, obj.getCodigoTipo());
    			
			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 		throw new DbException ( "Erro!!! sem atualização " + e.getMessage()); }

  		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigoTipo) {
		PreparedStatement st = null;
//		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM tipoFornecedor WHERE CodigoTipo = ? ", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigoTipo);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! naõ excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public TipoFornecedor findById(Integer codigo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM tipoFornecedor " +
 				 "WHERE CodigoTipo = ?");
 			
			st.setInt(1, codigo);
			rs = st.executeQuery();
			if (rs.next())
			{	TipoFornecedor obj = instantiateTipoFornecedores(rs);
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
	public List<TipoFornecedor> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM tipoFornecedor " +
					"ORDER BY NomeTipo");
			
			rs = st.executeQuery();
			
			List<TipoFornecedor> list = new ArrayList<>();
			
			while (rs.next())
			{	TipoFornecedor obj = instantiateTipoFornecedores(rs);
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
	
	private TipoFornecedor instantiateTipoFornecedores(ResultSet rs) throws SQLException {
 		TipoFornecedor tipoForn = new TipoFornecedor();
 		
 		tipoForn.setCodigoTipo(rs.getInt("CodigoTipo"));
 		tipoForn.setNomeTipo(rs.getString("NomeTipo"));	
         return tipoForn;
	}
}
