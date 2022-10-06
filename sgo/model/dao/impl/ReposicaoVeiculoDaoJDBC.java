package sgo.model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.DB;
import db.DbException;
import sgo.model.dao.ReposicaoVeiculoDao;
import sgo.model.entities.ReposicaoVeiculo;
  
public class ReposicaoVeiculoDaoJDBC implements ReposicaoVeiculoDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public ReposicaoVeiculoDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Reposicao ";
	
	@Override
	public void insert(ReposicaoVeiculo obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO reposicaoVeiculo " +
				      "(OsRep, DataRep, PlacaRep, ClienteRep, DddClienteRep, TelefoneClienteRep, " +
						"CodigoMaterialRep, MaterialRep, KmTrocaRep, ProximaKmRep, " +
				      	"ProximaDataRep) " + 
  				      "VALUES " +
  				      	"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 

 			st.setInt(1, obj.getOsRep());
 			st.setDate(2, new java.sql.Date(obj.getDataRep().getTime()));
 			st.setString(3, obj.getPlacaRep());
 			st.setString(4, obj.getClienteRep());
 			st.setInt(5, obj.getDddClienteRep());
 			st.setInt(6, obj.getTelefoneClienteRep());
 			st.setInt(7, obj.getCodigoMaterialRep());
 			st.setString(8,  obj.getMaterialRep());
 			st.setInt(9,  obj.getKmTrocaRep());
 			st.setInt(10,  obj.getProximaKmRep());
 			st.setDate(11, new java.sql.Date(obj.getProximaDataRep().getTime()));
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroRep(codigo);
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
	public void deleteByOs(Integer os) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"DELETE FROM reposicaoVeiculo WHERE OsRep = ? ", 
							Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, os);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException (classe + "Erro!!! naõ excluído " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public List<ReposicaoVeiculo> findByPlaca(String placa, Integer km) {
 		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				 "SELECT * FROM reposicaoVeiculo " +
 				 "WHERE PlacaRep = ? AND ProximaKmRep < ? ");
 			
			st.setString(1,	placa);
			st.setInt(2, km);
			rs = st.executeQuery();

			List<ReposicaoVeiculo> list = new ArrayList<>();
			
			while (rs.next())
			{	ReposicaoVeiculo obj = instantiateReposicaoVeiculos(rs);
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
	public List<ReposicaoVeiculo> findAllData() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		Date dth = new Date();
		try {
			st = conn.prepareStatement( 
					"SELECT * FROM reposicaoVeiculo " +
					"ORDER BY PlacaRep ");
			
			rs = st.executeQuery();
			
			List<ReposicaoVeiculo> list = new ArrayList<>();
			
			while (rs.next())
			{	if (rs != null)
				{	ReposicaoVeiculo obj = instantiateReposicaoVeiculos(rs);
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
	
	private ReposicaoVeiculo instantiateReposicaoVeiculos(ResultSet rs) throws SQLException {
 		ReposicaoVeiculo rep = new ReposicaoVeiculo();
 		rep.setNumeroRep(rs.getInt("NumeroRep"));
 		rep.setOsRep(rs.getInt("OsRep"));
		rep.setDataRep(new java.util.Date(rs.getTimestamp("DataRep").getTime()));
 		rep.setPlacaRep(rs.getString("PlacaRep"));
 		rep.setClienteRep(rs.getString("ClienteRep"));
 		rep.setDddClienteRep(rs.getInt("DddClienteRep"));
 		rep.setTelefoneClienteRep(rs.getInt("TelefoneClienteRep"));
 		rep.setCodigoMaterialRep(rs.getInt("CodigoMaterialRep"));
 		rep.setMaterialRep(rs.getString("MaterialRep"));
 		rep.setKmTrocaRep(rs.getInt("KmTrocaRep"));
 		rep.setProximaKmRep(rs.getInt("ProximaKmRep"));
 		rep.setProximaDataRep(new java.util.Date(rs.getTimestamp("ProximaDataRep").getTime()));
        return rep;
	}
}
