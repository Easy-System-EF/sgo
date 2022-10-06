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
import sgcp.model.entityes.Fornecedor;
import sgo.model.dao.EntradaDao;
import sgo.model.entities.Entrada;
import sgo.model.entities.Grupo;
import sgo.model.entities.Material;
  
public class EntradaDaoJDBC implements EntradaDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public EntradaDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Entrada";
	
	@Override
	public void insert(Entrada obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO entrada " 
				      + "(NnfEnt, DataEnt, NomeFornEnt, NomeMatEnt, QuantidadeMatEnt, "
				      + "ValorMatEnt, MaterialId, FornecedorId)" 
   				      + "VALUES " 
				      + "(?, ?, ?, ?, ?, ?, ?, ?)",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getNnfEnt());
			st.setDate(2, new java.sql.Date(obj.getDataEnt().getTime()));
			st.setString(3, obj.getForn().getRazaoSocial());
			st.setString(4, obj.getMat().getNomeMat());
 			st.setDouble(5, obj.getQuantidadeMatEnt());
			st.setDouble(6, obj.getValorMatEnt());
			st.setInt(7, obj.getMat().getCodigoMat());
			st.setInt(8,  obj.getForn().getCodigo());
 
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroEnt(codigo);
//					System.out.println("Novo inserido: " + classe + " " + obj.getNumeroEnt());
				}
				else
				{	throw new DbException("Erro!!! sem inclusão" + classe);
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
	public void update(Entrada obj) {
		PreparedStatement st = null;
  		try {
			st = conn.prepareStatement(
					"UPDATE entrada " +  
							"SET NnfEnt = ?, "
							+ "DataEnt = ?, "
							+ "NomeFornEnt = ?, "
							+ "NomeMatEnt = ?, "
							+ "QuantidadeMatEnt = ?, "
							+ "ValorMatEnt = ?, "
 							+ "MaterialId = ?, "
 							+ "FornecedorId = ? "
 					+ "WHERE (NumeroEnt = ?)",
        			 Statement.RETURN_GENERATED_KEYS);

			st.setInt(1,  obj.getNnfEnt());
			st.setDate(2, new java.sql.Date(obj.getDataEnt().getTime()));
			st.setString(3,  obj.getForn().getRazaoSocial());
			st.setString(4, obj.getMat().getNomeMat());
 			st.setDouble(5, obj.getQuantidadeMatEnt());
			st.setDouble(6, obj.getValorMatEnt());
			st.setInt(7, obj.getMat().getCodigoMat());
			st.setInt(8,  obj.getForn().getCodigo());
			st.setInt(9, obj.getNumeroEnt()); 
 			st.executeUpdate();
   		} 
 		catch (SQLException e) {
 				throw new DbException ( "Erro!!! sem atualização " +  classe + " " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
   		try {
			st = conn.prepareStatement(
					"DELETE FROM entrada WHERE NumeroEnt = ?", Statement.RETURN_GENERATED_KEYS);
				  
			st.setInt(1, codigo);
			st.executeUpdate();
   		}
 		catch (SQLException e) {
			throw new DbException ( "Erro!!! naõ excluído " +  classe + " " + e.getMessage()); }
 		finally {
 			DB.closeStatement(st);
		}
	}

	@Override
	public List<Entrada> findAll(List<Grupo> grupo) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try 
		{	st = conn.prepareStatement( 
					 "SELECT *, material.CodigoMat, fornecedor.Codigo "
						 + "FROM entrada " 
					 		+ "INNER JOIN material " 
								+ "on entrada.MaterialId = material.CodigoMat "
					 		+ "INNER JOIN fornecedor " 
					 			+ "on entrada.FornecedorId = fornecedor.Codigo " 
						+ "ORDER BY - DataEnt");
  
			rs = st.executeQuery();
			
			List<Entrada> list = new ArrayList<>();
			Material mat = new Material();
			Grupo gru = new Grupo();
  			Map<Integer, Material> mapMat = new HashMap<>();
			Map<Integer, Fornecedor> mapForn = new HashMap<>();
			Map<Integer, Grupo> mapGru = new HashMap<>();
   			
			while (rs.next()) 
 			{	mat =	mapMat.get(rs.getInt("MaterialId")); 
 				if (mat == null)
 				{	gru = mapGru.get(rs.getInt("GrupoMat"));
 					if (gru == null)
  					{	for (Grupo g : grupo)
 						{	Integer cod = g.getCodigoGru();
 							Integer codRs = rs.getInt("GrupoMat");
  							if (cod == codRs)
 							{	int gr = g.getCodigoGru();
 								String nome = g.getNomeGru();
 								gru = instantiateGrupo(gr, nome);
 								mapGru.put(rs.getInt("GrupoMat"), gru);
 							}
 						}
  					}
 				}	
 				mat = instantiateMaterial(rs, gru);
 				mapMat.put(rs.getInt("MaterialId"), mat);
			
  				Fornecedor forn = mapForn.get(rs.getInt("FornecedorId"));
  				if (forn == null)
  				{	forn = instantiateFornecedor(rs);
  					mapForn.put(rs.getInt("FornecedorId"), forn);  				
  				}
  			    Entrada obj = instantiateEntrada(rs, mat, forn);
  			    list.add(obj);
  			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException( classe + " " + e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	private Entrada instantiateEntrada(ResultSet rs, Material mat, Fornecedor forn) throws SQLException {
 		Entrada ent = new Entrada();
 
 		ent.setNumeroEnt(rs.getInt("NumeroEnt"));
 		ent.setNnfEnt(rs.getInt("NnfEnt"));
  		ent.setDataEnt(new java.util.Date(rs.getTimestamp("DataEnt").getTime()));
 		ent.setNomeFornEnt(rs.getString("NomeFornEnt"));
 		ent.setNomeMatEnt(rs.getString("NomeMatEnt"));
  		ent.setQuantidadeMatEnt(rs.getDouble("QuantidadeMatEnt"));
 		ent.setValorMatEnt(rs.getDouble("ValorMatEnt"));
 		ent.setMat(mat);
 		ent.setForn(forn);
        return ent;
	}
	
	private Fornecedor instantiateFornecedor(ResultSet rs) throws SQLException {
 		Fornecedor forn = new Fornecedor();
  		forn.setCodigo(rs.getInt("Codigo"));
 		forn.setRazaoSocial(rs.getString("RazaoSocial"));	
 		forn.setRua(rs.getString("Rua"));
 		forn.setNumero(rs.getInt("Numero"));
 		forn.setComplemento(rs.getString("Complemento"));
 		forn.setBairro(rs.getString("Bairro"));
 		forn.setCidade(rs.getString("Cidade"));
 		forn.setUf(rs.getString("UF"));
 		forn.setCep(rs.getInt("Cep"));
 		forn.setDdd01(rs.getInt("Ddd01"));
 		forn.setTelefone01(rs.getInt("Telefone01"));
 		forn.setDdd02(rs.getInt("Ddd02"));
 		forn.setTelefone02(rs.getInt("Telefone02"));
 		forn.setContato(rs.getString("Contato"));
 		forn.setDddContato(rs.getInt("DddContato"));
 		forn.setTelefoneContato(rs.getInt("TelefoneContato"));
 		forn.setEmail(rs.getString("Email"));
 		forn.setPix(rs.getString("Pix"));
 		forn.setObservacao(rs.getString("Observacao"));
        return forn;
	}
	
 	private Grupo instantiateGrupo(Integer gr, String nome) throws SQLException {
 		Grupo grupoIns = new Grupo();
 		grupoIns.setCodigoGru(gr);
 		grupoIns.setNomeGru(nome);
      	return grupoIns;
	}

 	private Material instantiateMaterial(ResultSet rs, Grupo gru) throws SQLException {
 		Material material = new Material();
   		material.setCodigoMat(rs.getInt("CodigoMat"));
   		material.setGrupoMat(rs.getInt("GrupoMat"));
  		material.setNomeMat(rs.getString("NomeMat"));
  		material.setEntradaMat(rs.getDouble("EntradaMat"));
  		material.setSaidaMat(rs.getDouble("SaidaMat"));
//  		material.setSaldoMat(rs.getDouble("EntradaMat, SaldoMat"));
  		material.setPrecoMat(rs.getDouble("PrecoMat"));
  		material.setVendaMat(rs.getDouble("VendaMat"));
  		material.setVidaKmMat(rs.getInt("VidaKmMat"));
  		material.setVidaMesMat(rs.getInt("VidaMesMat"));
  		material.setCmmMat(rs.getDouble("CmmMat"));
  		material.setGrupo(gru);
      	return material;
	}

	@Override
	public Entrada findById(Integer codigo, List<Grupo> grupo) {
		// TODO Auto-generated method stub
		return null;
	}
}
