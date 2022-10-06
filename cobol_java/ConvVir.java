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
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import sgo.model.entities.Material;
import sgo.model.entities.OrcVirtual;
import sgo.model.services.MaterialService;
import sgo.model.services.OrcVirtualService;

public class ConvVir implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
 	 		String pathVir = "c:\\ARQS\\CGOTXT\\ATXTVIR.TXT";
 	 		OrcVirtual vir = new OrcVirtual();
 	 		OrcVirtualService virService = new OrcVirtualService();
 	 		List<OrcVirtual> list = new ArrayList<>();
 	 		Material mat = new Material();
 	 		MaterialService matService = new MaterialService();
 	 		int num = 0;
 	 				
 	 		try {
	 	 		BufferedReader brVir = new BufferedReader(new FileReader(pathVir));
	 			String lineVir = brVir.readLine();
 	 	 		while (lineVir != null) { 
	 	 			String[] campos = lineVir.split(";");
	 	 			vir.setNumeroVir(null);
	 	 			vir.setNomeMatVir(campos[7]);
	 	 			vir.setQuantidadeMatVir(Double.parseDouble(campos[2]));
	 	 			vir.setPrecoMatVir(Double.parseDouble(campos[3]) / 100);
	 	 			vir.setTotalMatVir(Double.parseDouble(campos[4]) / 100);
	 	 			vir.setNumeroOrcVir(Integer.parseInt(campos[5]));
	 	 			vir.setNumeroBalVir(0);
	 	 			mat = matService.findById(Integer.parseInt(campos[1]));
	 	 			vir.setMaterial(mat);

						num += 1;
					vir.setNumeroVir(num);
		 	 		list.add(new OrcVirtual(vir.getNumeroVir(), vir.getNomeMatVir(), 
			 	 		vir.getQuantidadeMatVir(), vir.getPrecoMatVir(),
			 	 		vir.getTotalMatVir(), vir.getNumeroOrcVir(),
			 	 		vir.getNumeroBalVir(), vir.getMaterial()));
System.out.println(vir.getNumeroVir());						
	 	 			lineVir = brVir.readLine();
 	 	 		}
 	 	 		brVir.close();
 	 	 		virService.zeraAll();
 	 	 		for (OrcVirtual o : list) {
	 	 			if (o != null) {
	 	 				vir = (OrcVirtual) o;
	 	 				insertVir(vir);
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
	 
	public static void insertVir(OrcVirtual vir) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DB.getConnection();	
			st = conn.prepareStatement(
					"INSERT INTO orcVirtual " +
	 	 			      "(NumeroVir, NomeMatVir, QuantidadeMatVir, PrecoMatVir, " +
	 	 			       "TotalMatVir, NumeroOrcVir, NumeroBalVir, MaterialId )" + 
	 	 				    "VALUES " +
	 	 			      "(?, ?, ?, ?, ?, ?, ?, ?)",
	 	 	 					 Statement.RETURN_GENERATED_KEYS); 

	 				st.setInt(1, vir.getNumeroVir());
	 				st.setString(2, vir.getNomeMatVir());
	 				st.setDouble(3, vir.getQuantidadeMatVir());
	 				st.setDouble(4, vir.getPrecoMatVir());
	 				st.setDouble(5, vir.getTotalMatVir());
	 				st.setInt(6, vir.getNumeroOrcVir());
System.out.println(vir.getNumeroVir() + " " + vir.getNumeroOrcVir());	 				
	 				st.setInt(7, vir.getNumeroBalVir());
	 				st.setInt(8, vir.getMaterial().getCodigoMat());
	 	 	   			
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
