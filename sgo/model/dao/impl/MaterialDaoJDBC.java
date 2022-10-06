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
import sgo.model.dao.MaterialDao;
import sgo.model.entities.Grupo;
import sgo.model.entities.Material;
  
public class MaterialDaoJDBC implements MaterialDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	String classe = "Material";
	
	public MaterialDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
 	@Override
	public void insert(Material obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO material " 
				      + "(GrupoMat, NomeMat, EntradaMat, SaidaMat, PrecoMat, VendaMat, "
				      + "VidaKmMat, VidaMesMat, CmmMat, DataCadastroMat, GrupoId)"  
  				      + "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 

  			st.setInt(1, obj.getGrupoMat());
			st.setString(2,  obj.getNomeMat());
			st.setDouble(3,  obj.getEntradaMat());
			st.setDouble(4,  obj.getSaidaMat());
			st.setDouble(5,  obj.getPrecoMat());
			st.setDouble(6,  obj.getVendaMat());
			st.setInt(7,  obj.getVidaKmMat());
			st.setInt(8, obj.getVidaMesMat());
			st.setDouble(9,  obj.getCmmMat());
			st.setDate(10, new java.sql.Date(obj.getDataCadastroMat().getTime()));
			st.setInt(11, obj.getGrupo().getCodigoGru());
  
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setCodigoMat(codigo);;
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
	public void update(Material obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
  					"UPDATE material " +  
  						"SET GrupoMat = ?, NomeMat = ?, EntradaMat = ?, SaidaMat = ?, "
  							+ "PrecoMat = ?, VendaMat = ?, VidaKmMat = ?, VidaMesMat = ?, "
  							+ "CmmMat = ?, DataCadastroMat = ?, GrupoId = ? "
  							+ "WHERE (CodigoMat = ?)",
        			 Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, obj.getGrupoMat());
			st.setString(2,  obj.getNomeMat());
			st.setDouble(3,  obj.getEntradaMat());
			st.setDouble(4,  obj.getSaidaMat());
			st.setDouble(5,  obj.getPrecoMat());
			st.setDouble(6,  obj.getVendaMat());
			st.setInt(7,  obj.getVidaKmMat());
			st.setInt(8, obj.getVidaMesMat());
			st.setDouble(9,  obj.getCmmMat());
			st.setDate(10, new java.sql.Date(obj.getDataCadastroMat().getTime()));
			st.setInt(11, obj.getGrupo().getCodigoGru());
  			st.setInt(12, obj.getCodigoMat());
  			
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
//		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM material WHERE CodigoMat = ? ", 
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
	public Material findById(Integer codigo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(

					"SELECT *, grupo.CodigoGru " + 
						"FROM material " +
							"INNER JOIN grupo " +
								"ON material.GrupoId = grupo.CodigoGru " +
						"WHERE CodigoMat = ? ");
 					
 			st.setInt(1, codigo);
			rs = st.executeQuery(); 

			List<Material> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			if (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoId"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoId"), gru);
				}	
				Material obj = instantiateMaterial(rs, gru);
				list.add(obj);
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
	public List<Material> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 

					"SELECT *, grupo.* " +  
						"FROM material " +
 							"INNER JOIN grupo "  +
								"ON material.GrupoId = grupo.CodigoGru " + 
 					"ORDER BY NomeMat");
 			
			rs = st.executeQuery();
			
			List<Material> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			
			while (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoId"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoId"), gru);
				}	
				Material obj = instantiateMaterial(rs, gru);
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
	public List<Material> findPesquisa(String str) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 

					"SELECT *, grupo.* " +  
						"FROM material " +
 							"INNER JOIN grupo "  +
								"ON material.GrupoId = grupo.CodigoGru " +
 						"WHERE nomeMat like ? " +
 					"ORDER BY NomeMat");
 			
			st.setString(1, str + "%");
			rs = st.executeQuery();
			
			List<Material> list = new ArrayList<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
			
			while (rs.next())
			{	Grupo gru = mapGru.get(rs.getInt("GrupoId"));
				if (gru == null)
				{	gru = instantiateGrupo(rs);
					mapGru.put(rs.getInt("GrupoId"), gru);
				}	
				Material obj = instantiateMaterial(rs, gru);
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
	
 	private Material instantiateMaterial(ResultSet rs, Grupo gru) throws SQLException {
 		Material material = new Material();
   		material.setCodigoMat(rs.getInt("CodigoMat"));
  		material.setGrupoMat(rs.getInt("GrupoMat"));
  		material.setNomeMat(rs.getString("NomeMat"));
  		material.setEntradaMat(rs.getDouble("EntradaMat"));
  		material.setSaidaMat(rs.getDouble("SaidaMat"));
  		material.setPrecoMat(rs.getDouble("PrecoMat"));
  		material.setVendaMat(rs.getDouble("VendaMat"));
  		material.setVidaKmMat(rs.getInt("VidaKmMat"));
  		material.setVidaMesMat(rs.getInt("VidaMesMat"));
  		material.setCmmMat(rs.getDouble("CmmMat"));
  		material.setDataCadastroMat(new java.util.Date(rs.getTimestamp("DataCadastroMat").getTime()));
  		material.setGrupo(gru);
    	return material;
	}
 
 	private Grupo instantiateGrupo(ResultSet rs) throws SQLException {
		Grupo gru = new Grupo();
		gru.setCodigoGru(rs.getInt("CodigoGru"));
		gru.setNomeGru(rs.getString("NomeGru"));
  		return gru;
 	}
}
