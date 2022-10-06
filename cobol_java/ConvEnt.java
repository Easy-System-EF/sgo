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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import db.DB;
import db.DbException;
import model.services.FornecedorService;
import sgcp.model.entityes.Fornecedor;
import sgo.model.entities.Entrada;
import sgo.model.entities.Material;
import sgo.model.services.CargoService;
import sgo.model.services.MaterialService;
import sgo.model.services.SituacaoService;

public class ConvEnt implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
			Date dataHj = new Date();
 	 		String pathEnt = "c:\\ARQS\\CGOTXT\\ATXTENT.TXT";
 	 		Entrada ent = new Entrada();
 	 		Material mat = new Material();
 	 		MaterialService matService = new MaterialService();
 	 		Fornecedor forn = new Fornecedor(); 
 	 		FornecedorService forService = new FornecedorService();
 	 		Set<Entrada> set = new HashSet<>();
 	 		Calendar cal = Calendar.getInstance();
 	 		int dd = 0;
 	 		int mm = 0;
 	 		int mi = 2000;
 	 		int aa = 0;
 	 		int nnf = 0;
 	 		int flagf = 0;
 	 				
 	 		try {
	 	 		BufferedReader brEnt = new BufferedReader(new FileReader(pathEnt));
	 			String lineEnt = brEnt.readLine();
 	 	 		while (lineEnt != null) { 
	 	 			String[] campos = lineEnt.split(";");
	 	 			ent.setNumeroEnt(Integer.parseInt(campos[0]));
	 	 			ent.setNnfEnt(Integer.parseInt(campos[1]));
	 	 			if (ent.getNnfEnt() == 0) {
	 	 				nnf += 1;
	 	 				ent.setNnfEnt(nnf);
	 	 			}
	 	 			ent.setDataEnt(dataHj);
	 	 			dd = Integer.parseInt(campos[2].substring(0, 2));
	 	 			mm = Integer.parseInt(campos[2].substring(2, 4));
	 	 			aa = mi + Integer.parseInt(campos[2].substring(4, 6));
	 	 			cal.setTime(ent.getDataEnt());
	 	 			cal.set(aa, (mm - 1), dd);
	 	 			ent.setDataEnt(cal.getTime());
	 	 			ent.setNomeFornEnt(campos[3]);
	 	 			ent.setNomeMatEnt(campos[4]);
	 	 			ent.setQuantidadeMatEnt(Double.parseDouble(campos[5]) / 100);
	 	 			ent.setValorMatEnt(Double.parseDouble(campos[6]) / 100);
	 	 			forn = forService.findById(Integer.parseInt(campos[7]));
	 	 			ent.setForn(forn);
	 	 			mat = matService.findById(Integer.parseInt(campos[8]));
	 	 			ent.setMat(mat);
if (ent.getForn() == null) {
	flagf = 1;
}
	 	 				 	 	   			
					if (set.contains(ent.getNumeroEnt())) {
						int nada = 1;
					}
					else {
						set.add(new Entrada(ent.getNumeroEnt(), ent.getNnfEnt(), 
							ent.getDataEnt(), ent.getNomeFornEnt(), ent.getNomeMatEnt(), 
	 	 					ent.getQuantidadeMatEnt(), ent.getValorMatEnt(),
	 	 					ent.getForn(), ent.getMat()));
					}
	 	 			lineEnt = brEnt.readLine();
 	 	 		}
 	 	 		brEnt.close();
 	 	 		for (Entrada e : set) {
	 	 			if (e != null) {
	 	 				ent = (Entrada) e;
	 	 				insertEnt(ent);
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
	 
	public static void insertEnt(Entrada ent) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DB.getConnection();	
			st = conn.prepareStatement(
					"INSERT INTO entrada " +
	 	 			      "(NumeroEnt, NnfEnt, DataEnt, " +
							"NomeFornEnt, NomeMatEnt, QuantidadeMatEnt, ValorMatEnt, " +
	 	 			        "MaterialId, FornecedorId )" + 
	 	 				    " VALUES " +
	 	 			      "(?, ?, ?, ?, ?, ?, ?, ?, ? )",
	 	 	 					 Statement.RETURN_GENERATED_KEYS); 
			
	 				st.setInt(1, ent.getNumeroEnt());
	 	 			st.setInt(2, ent.getNnfEnt());
	 	 			st.setDate(3, new java.sql.Date(ent.getDataEnt().getTime()));
	 				st.setString(4, ent.getNomeFornEnt());
	 				st.setString(5, ent.getNomeMatEnt());
	 	 			st.setDouble(6, ent.getQuantidadeMatEnt());
	 	 			st.setDouble(7, ent.getValorMatEnt());
	 	 			st.setInt(8, ent.getMat().getCodigoMat());
	 	 			st.setInt(9, ent.getForn().getCodigo());
	 	 			
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
	 	 	
 	 		public static void setServiceFun(CargoService carService,
 	 										SituacaoService sitService) {
 	 			carService = carService;
 	 			sitService = sitService;
 	 		}

	}
