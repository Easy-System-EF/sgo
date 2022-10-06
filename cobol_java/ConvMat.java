package cobol_java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import db.DB;
import db.DbException;
import sgo.model.entities.Grupo;
import sgo.model.entities.Material;
import sgo.model.services.GrupoService;

public class ConvMat implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dataHj = new Date();
 	 		String pathMc = "c:\\ARQS\\CGOTXT\\ATXTMAT.TXT";
 	 		Material mat = new Material(); 
 	 		Set<Material> set = new HashSet<>();
 	 		Grupo grupo =  new Grupo();
 	 		GrupoService gruService = new GrupoService();
 	 		int qtd = 0;
 	 				
 	 		try {
	 	 		BufferedReader brMat = new BufferedReader(new FileReader(pathMc));
	 			String lineMat = brMat.readLine();
 	 	 		while (lineMat != null) { 
	 	 			String[] campos = lineMat.split(";");
	 	 			mat.setCodigoMat(Integer.parseInt(campos[0]));
	 	 			mat.setGrupoMat(Integer.parseInt(campos[1]));
	 	 			mat.setNomeMat(campos[2]);
	 	 			mat.setEntradaMat(0.00);
	 	 			mat.setSaidaMat(0.00);
	 	 			double preco = Double.parseDouble(campos[4]);
	 	 			mat.setPrecoMat(preco / 100);
	 	 			double venda = Double.parseDouble(campos[5]);
	 	 			mat.setVendaMat(venda / 100);
	 	 			int km = (Integer.parseInt(campos[7]));
	 	 			if (km == 999999 || km == 900000 || km == 99999 || km == 77777) {
	 	 				mat.setVidaKmMat(0);
	 	 			} else {
	 	 				mat.setVidaKmMat(Integer.parseInt(campos[7]));
	 	 			}
	 	 			int mm = (Integer.parseInt(campos[6]));
	 	 			if (mm == 99 || mm == 77) {
	 	 				mat.setVidaMesMat(0);
	 	 			} else {
	 	 				mat.setVidaMesMat(Integer.parseInt(campos[6]));
	 	 			}	
	 	 			mat.setCmmMat(0.00);
	 	 			mat.setDataCadastroMat(dataHj);
	 	 			grupo = gruService.findById(mat.getGrupoMat());
	 	 			mat.setGrupo(grupo);
					if (set.contains(mat.getCodigoMat())) {
						int nada = 1;
					}
					else {
						set.add(new Material(mat.getCodigoMat(), mat.getGrupoMat(), mat.getNomeMat(),  
	 	 					mat.getEntradaMat(), mat.getSaidaMat(), mat.getSaldoMat(), mat.getPrecoMat(),
	 	 					mat.getVendaMat(), mat.getVidaKmMat(), mat.getVidaMesMat(),
	 	 					mat.getCmmMat(), mat.getDataCadastroMat(), mat.getGrupo()));
						qtd += 1;
					}
	 	 			lineMat = brMat.readLine();
 	 	 		}
 	 	 		brMat.close();
 	 	 		for (Object o : set) {
	 	 			if (o != null) {	 	 				
	 	 				mat = (Material) o;
System.out.println(mat.getCodigoMat());	 	 				
	 	 				insertMat(mat);
	 	 			}
 	 	 		}	
	 	 	}
 	 		catch (FileNotFoundException e) {
 	 			throw new DbException (e.getMessage());
 	 		}
 	 		catch (IOException e) {
 	 			throw new DbException (e.getMessage());
 	 		}
	}	
	 
	public static void insertMat(Material mat) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DB.getConnection();	
			st = conn.prepareStatement(
					"INSERT INTO material " +
	 	 			      "(CodigoMat, GrupoMat, NomeMat, EntradaMat, SaidaMat, PrecoMat, " +
	 	 			       "VendaMat, VidaKmMat, VidaMesMat, CmmMat, " +
	 	 			       "DataCadastroMat, GrupoId)" + 
	 	 				    "VALUES " +
	 	 			      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
	 	 	 					 Statement.RETURN_GENERATED_KEYS); 
	 	 	 
	 				st.setInt(1, mat.getCodigoMat());
	 				st.setInt(2, mat.getGrupoMat());
	 				st.setString(3, mat.getNomeMat());
	 	 			st.setDouble(4, mat.getEntradaMat());
	 	 			st.setDouble(5, mat.getSaidaMat());
	 	 			st.setDouble(6, mat.getPrecoMat());
	 	 			st.setDouble(7, mat.getVendaMat());
	 	 			st.setInt(8, mat.getVidaKmMat());
	 	 			st.setInt(9, mat.getVidaMesMat());
	 	 			st.setDouble(10, mat.getCmmMat());
	 	 			st.setDate(11, new java.sql.Date(mat.getDataCadastroMat().getTime()));
	 	 	   		st.setInt(12, mat.getGrupo().getCodigoGru());
	 	 	   			
	 	 	   		st.executeUpdate();
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
