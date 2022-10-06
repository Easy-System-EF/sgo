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
import sgo.model.entities.Adiantamento;
import sgo.model.entities.Cargo;
import sgo.model.entities.Funcionario;
import sgo.model.entities.Situacao;
import sgo.model.services.CargoService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.SituacaoService;

public class ConvAdi implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dataHj = new Date();
 	 		String pathAd = "c:\\ARQS\\CGOTXT\\ATXTADI.TXT";
 	 		Adiantamento adi = new Adiantamento();
 	 		Set<Adiantamento> set = new HashSet<>();
 	 		Funcionario fun = new Funcionario();
 	 		FuncionarioService funService = new FuncionarioService(); 
 	 		Cargo cargo =  new Cargo();
 	 		CargoService carService = new CargoService();
 	 		Situacao sit = new Situacao();
 	 		SituacaoService sitService = new SituacaoService();
 	 		Calendar cal = Calendar.getInstance();
 	 		int dd = 0;
 	 		int mm = 0;
 	 		int mi = 2000;
 	 		int aa = 0;
 	 		int num = 0;
 	 				
 	 		try {
	 	 		BufferedReader brAdi = new BufferedReader(new FileReader(pathAd));
	 			String lineAdi = brAdi.readLine();
 	 	 		while (lineAdi != null) { 
	 	 			String[] campos = lineAdi.split(";");
	 	 			adi.setDataAdi(dataHj);
	 	 			dd = Integer.parseInt(campos[1].substring(0, 2));
	 	 			mm = Integer.parseInt(campos[1].substring(2, 4));
	 	 			aa = Integer.parseInt(campos[1].substring(4, 8));
	 	 			cal.setTime(adi.getDataAdi());
	 	 			cal.set(aa, (mm - 1), dd);
	 	 			adi.setDataAdi(cal.getTime());
	 	 			adi.setCodFunAdi(Integer.parseInt(campos[0]));
	 	 			adi.setNomeFunAdi(campos[2]);
	 	 			adi.setSituacaoAdi(campos[4]);
	 	 			adi.setValorAdi(0.00);
	 	 			adi.setMesAdi(mm);
	 	 			adi.setAnoAdi(aa);
	 	 			adi.setComissaoAdi(Double.parseDouble(campos[8]) / 100);
	 	 			adi.setOsAdi(Integer.parseInt(campos[9]));
	 	 			adi.setBalcaoAdi(0);
	 	 			adi.setTipoAdi(campos[11]);
	 	 			fun = funService.findById(Integer.parseInt(campos[12]));
	 	 			adi.setFuncionario(fun);
	 	 			cargo = carService.findById(6);
	 	 			adi.setCargo(cargo);
	 	 			sit = sitService.findById(1);
	 	 			adi.setSituacao(sit);
	 	 			adi.setCargoAdi(fun.getCargo().getNomeCargo());
System.out.println(adi.getNumeroAdi());	 	 			
					if (set.contains(adi.getNumeroAdi())) {
						int nada = 1;
					}
					else {
						if (aa > 2021) {
			 	 			num += 1;
			 	 			adi.setNumeroAdi(num);
			 	 			set.add(new Adiantamento(adi.getNumeroAdi(), adi.getDataAdi(), 
			 	 					adi.getCodFunAdi(), adi.getNomeFunAdi(), adi.getCargoAdi(),
			 	 					adi.getSituacaoAdi(), adi.getValorAdi(), adi.getMesAdi(),
			 	 					adi.getAnoAdi(), adi.getComissaoAdi(), adi.getOsAdi(),
			 	 					adi.getBalcaoAdi(), adi.getTipoAdi(), adi.getFuncionario(),
			 	 					adi.getCargo(), adi.getSituacao()));
						}
					}
	 	 			lineAdi = brAdi.readLine();
 	 	 		}
 	 	 		brAdi.close();
 	 	 		for (Adiantamento a : set) {
	 	 			if (a != null) {
	 	 				adi = (Adiantamento) a;
	 	 				insertAdi(adi);
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
	 
	public static void insertAdi(Adiantamento adi) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DB.getConnection();	
			st = conn.prepareStatement(
					"INSERT INTO adiantamento " +
	 	 			      "(NumeroAdi, DataAdi, CodFunAdi, NomeFunAdi, CargoAdi, SituacaoAdi,  " +
	 	 			       "ValorAdi, MesAdi, AnoAdi, ComissaoAdi, OsAdi, BalcaoAdi, TipoAdi, " +
	 	 			       "FuncionarioIdAdi, CargoIdAdi, SituacaoIdAdi )" + 
	 	 				    "VALUES " +
	 	 			      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
	 	 	 					 Statement.RETURN_GENERATED_KEYS); 

	 				st.setInt(1, adi.getNumeroAdi());
	 				st.setDate(2, new java.sql.Date(adi.getDataAdi().getTime()));
	 	 			st.setInt(3, adi.getCodFunAdi());
	 	 			st.setString(4, adi.getNomeFunAdi());
	 	 			st.setString(5, adi.getCargoAdi());
	 	 			st.setString(6, adi.getSituacaoAdi());
	 	 			st.setDouble(7, adi.getValorAdi());
	 	 			st.setInt(8, adi.getMesAdi());
	 	 			st.setInt(9, adi.getAnoAdi());
	 	 			st.setDouble(10, adi.getComissaoAdi());
	 	 			st.setInt(11, adi.getOsAdi());
	 	 	   		st.setInt(12, adi.getBalcaoAdi());
	 	 	   		st.setString(13, adi.getTipoAdi());
	 	 	   		st.setInt(14, adi.getFuncionario().getCodigoFun());
	 	 	   		st.setInt(15, adi.getCargo().getCodigoCargo());
	 	 	   		st.setInt(16,  adi.getSituacao().getNumeroSit());
	 	 	   			
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
