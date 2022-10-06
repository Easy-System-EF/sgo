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
import sgo.model.entities.Veiculo;
import sgo.model.services.CargoService;
import sgo.model.services.SituacaoService;

public class ConVei implements Serializable{

	private static final long serialVersionUID = 1L;

		public static void main(String[] args) {
	 	 		String pathVei = "c:\\ARQS\\CGOTXT\\ATXTVEI.TXT";
	 	 		Veiculo vei = new Veiculo(); 
	 	 		List<Veiculo> list1 = new ArrayList<>();
	 	 				
	 	 		try {
		 	 		BufferedReader brVei = new BufferedReader(new FileReader(pathVei));
		 			String lineVei = brVei.readLine();
	 	 	 		while (lineVei != null) { 
		 	 			String[] campos = lineVei.split(";");
		 	 			vei.setNumeroVei(null);
		 	 			vei.setPlacaVei(campos[0]);
		 	 			vei.setKmInicialVei(Integer.parseInt(campos[2]));
		 	 			vei.setKmFinalVei(Integer.parseInt(campos[3]));
		 	 			vei.setModeloVei(campos[1]);
		 	 			vei.setAnoVei(0);
		 	 			int nada = 0;
						for (Veiculo v : list1) {
							if (v.getPlacaVei().equals(vei.getPlacaVei())) {
								nada = 1;
							}
						}	
						if (nada == 0) {
							list1.add(new Veiculo(vei.getNumeroVei(), vei.getPlacaVei(),
									vei.getKmInicialVei(), vei.getKmFinalVei(), 
		 	 					vei.getModeloVei(), vei.getAnoVei()));
						}
		 	 			lineVei = brVei.readLine();
	 	 	 		}
	 	 	 		brVei.close();
	 	 	 		for (Veiculo v1 : list1) {
		 	 			if (v1 != null) {
		 	 				vei = (Veiculo) v1;
System.out.println(vei);		 	 				
		 	 				insertVei(vei);
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
		 
		public static void insertVei(Veiculo vei) {
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				conn = DB.getConnection();	
				st = conn.prepareStatement(
						"INSERT INTO veiculo " +
		 	 			      "(PlacaVei, KmInicialVei, KmFinalVei, ModeloVei, " +
		 	 			        "AnoVei )" + 
		 	 				    "VALUES " +
		 	 			      "(?, ?, ?, ?, ?)",
		 	 	 					 Statement.RETURN_GENERATED_KEYS); 
		 	 	 
		 				st.setString(1, vei.getPlacaVei());
		 				st.setInt(2, vei.getKmInicialVei());
		 				st.setInt(3, vei.getKmFinalVei());
		 				st.setString(4, vei.getModeloVei());
		 				st.setInt(5, vei.getAnoVei());
		 	 	   			
//		 	 	   		st.executeUpdate();
		 	 			int rowsaffectad = st.executeUpdate();
		 				
		 				if (rowsaffectad > 0)
		 				{	rs = st.getGeneratedKeys();
		 					if (rs.next())
		 					{	int codigo = rs.getInt(1);
		 						vei.setNumeroVei(codigo);
		 						System.out.println("New key inserted: " + vei.getNumeroVei());
		 					}
		 					else
		 					{	throw new DbException("Erro!!! " + "Veiculo" + " sem inclusão" );
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
}
