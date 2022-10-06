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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import db.DB;
import db.DbException;
import model.services.FornecedorService;
import model.services.ParPeriodoService;
import model.services.TipoFornecedorService;
import sgcp.model.entityes.Compromisso;
import sgcp.model.entityes.Fornecedor;
import sgcp.model.entityes.TipoFornecedor;
import sgcp.model.entityes.consulta.ParPeriodo;
import sgo.model.services.CargoService;
import sgo.model.services.SituacaoService;

public class ConvCom implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
			Date dataHj = new Date();
 	 		String pathCom = "c:\\ARQS\\CGOTXT\\ATXTCOM.TXT";
 	 		Compromisso com = new Compromisso();
 	 		ParPeriodo per = new ParPeriodo();
 	 		ParPeriodoService perService = new ParPeriodoService();
 	 		TipoFornecedor tipo = new TipoFornecedor();
 	 		TipoFornecedorService tipoService = new TipoFornecedorService(); 
 	 		Fornecedor forn = new Fornecedor(); 
 	 		FornecedorService forService = new FornecedorService();
 	 		Set<Compromisso> set = new HashSet<>();
 	 		Calendar cal = Calendar.getInstance();
 	 		int dd = 0;
 	 		int mm = 0;
 	 		int aa = 0;
 	 				
 	 		try {
	 	 		BufferedReader brCom = new BufferedReader(new FileReader(pathCom));
	 			String lineCom = brCom.readLine();
 	 	 		while (lineCom != null) { 
	 	 			String[] campos = lineCom.split(";");
	 	 			com.setCodigoFornecedorCom(Integer.parseInt(campos[0]));
	 	 			com.setNomeFornecedorCom(campos[1]);
	 	 			com.setNnfCom(Integer.parseInt(campos[2]));
	 	 			dd = Integer.parseInt(campos[3].substring(0, 2));
	 	 			mm = Integer.parseInt(campos[3].substring(2, 4));
	 	 			aa = Integer.parseInt(campos[3].substring(4, 8));
	 	 			cal.setTime(dataHj);
	 	 			cal.set(aa, (mm - 1), dd);
	 	 			com.setDataCom(cal.getTime());
	 	 			dd = Integer.parseInt(campos[4].substring(0, 2));
	 	 			mm = Integer.parseInt(campos[4].substring(2, 4));
	 	 			aa = Integer.parseInt(campos[4].substring(4, 8));
	 	 			cal.setTime(dataHj);
	 	 			cal.set(aa, (mm - 1), dd);
	 	 			com.setDataVencimentoCom(cal.getTime());
	 	 			com.setValorCom(Double.parseDouble(campos[5]) / 100);
	 	 			com.setParcelaCom(Integer.parseInt(campos[6]));
	 	 			com.setPrazoCom(Integer.parseInt(campos[7]));
	 	 			if (com.getPrazoCom() == 0) {
	 	 				com.setPrazoCom(1);
	 	 			} 	 	 				
	 	 			if (com.getPrazoCom() == 14) {
	 	 				com.setPrazoCom(15);
	 	 			}
	 	 			if (com.getPrazoCom() == 25) {
	 	 				com.setPrazoCom(21);
	 	 			}
	 	 			if (com.getPrazoCom() == 28) {
	 	 				com.setPrazoCom(30);
	 	 			}
	 	 			forn = forService.findById(com.getCodigoFornecedorCom());
	 	 			com.setFornecedor(forn);
	 	 			int tp = Integer.parseInt(campos[8]);
	 	 			if (tp == 0) {
	 	 				tp = 1;
	 	 			}
	 	 			tipo = tipoService.findById(tp);
	 	 			com.setTipoFornecedor(tipo);
	 	 			per = perService.findById(1);
	 	 			com.setParPeriodo(per);

	 	 			int nada = 0;
	 	 			if (set.contains(com.getCodigoFornecedorCom())) {
							if (set.contains(com.getNnfCom())) {
									if (set.contains(com.getParcelaCom())) {
										nada = 1;
									}
							}		
					}
					if (nada == 0) {
						set.add(new Compromisso(com.getCodigoFornecedorCom(), com.getNomeFornecedorCom(), 
							com.getNnfCom(), com.getDataCom(), com.getDataVencimentoCom(), 
	 	 					com.getValorCom(), com.getParcelaCom(), com.getPrazoCom(),
	 	 					com.getFornecedor(), com.getTipoFornecedor(), com.getParPeriodo()));
					}
	 	 			lineCom = brCom.readLine();
 	 	 		}
 	 	 		brCom.close();
 	 	 		for (Compromisso c : set) {
	 	 			if (c != null) {
	 	 				com = (Compromisso) c;
	 	 				insertCom(com);
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
	 
	public static void insertCom(Compromisso com) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DB.getConnection();	
			st = conn.prepareStatement(
					"INSERT INTO compromisso " +
	 	 			      "(CodigoFornecedorCom, NomeFornecedorCom, NnfCom, DataCom, " +
							"DataVencimentoCom, ValorCom, ParcelaCom, PrazoCom, " +
	 	 			        "FornecedorId, TipoId, PeriodoId )" + 
	 	 				    " VALUES " +
	 	 			      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
	 	 	 					 Statement.RETURN_GENERATED_KEYS); 
			
	 				st.setInt(1, com.getCodigoFornecedorCom());
	 				st.setString(2, com.getNomeFornecedorCom());
	 	 			st.setInt(3, com.getNnfCom());
	 	 			st.setDate(4, new java.sql.Date(com.getDataCom().getTime()));
	 	 			st.setDate(5, new java.sql.Date(com.getDataVencimentoCom().getTime()));
	 	 			st.setDouble(6, com.getValorCom());
	 	 			st.setInt(7, com.getParcelaCom());
	 	 			st.setInt(8, com.getPrazoCom());
	 	 			st.setInt(9, com.getFornecedor().getCodigo());
	 	 			st.setInt(10, com.getTipoFornecedor().getCodigoTipo());
	 	 			st.setInt(11, com.getParPeriodo().getIdPeriodo());
	 	 	   			
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
