package application;

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
import java.util.HashSet;
import java.util.Set;

import db.DB;
import db.DbException;
import sgo.model.entities.Cargo;
import sgo.model.entities.Funcionario;
import sgo.model.entities.Situacao;
import sgo.model.services.CargoService;
import sgo.model.services.SituacaoService;

public class ConvFun implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 	 		String pathFc = "c:\\ARQS\\CGOTXT\\ATXTFUN.TXT";
 	 		Funcionario fun = new Funcionario(); 
 	 		Set<Funcionario> set = new HashSet<>();
 	 		Cargo cargo =  new Cargo();
 	 		CargoService carService = new CargoService();
 	 		Situacao sit = new Situacao();
 	 		SituacaoService sitService = new SituacaoService();
 	 		setServiceFun(carService, sitService);
 	 				
 	 		try {
	 	 		BufferedReader brFun = new BufferedReader(new FileReader(pathFc));
	 			String lineFun = brFun.readLine();
 	 	 		while (lineFun != null) { 
	 	 			String[] campos = lineFun.split(";");
	 	 			fun.setCodigoFun(Integer.parseInt(campos[0]));
	 	 			fun.setNomeFun(campos[1]);
	 	 			fun.setEnderecoFun(campos[2]);
	 	 			fun.setBairroFun(campos[3]);
	 	 			fun.setCidadeFun("Contagem");
	 	 			fun.setUfFun("MG");
  	 	 			fun.setCepFun("32260470");
  	 	 			fun.setDddFun(31);
 	 				fun.setTelefoneFun(Integer.parseInt("999999999"));
  	 	 			fun.setCpfFun(campos[8]);
	 	 			fun.setPixFun("pix@pix.com");
	 	 			cargo = carService.findById(Integer.parseInt(campos[9]));
	 	 			fun.setCargoFun(cargo.getNomeCargo());
	 	 			fun.setSituacaoFun("Ativo");
	 	 			fun.setComissaoFun(0.00);
	 	 			fun.setAdiantamentoFun(0.00);
	 	 			fun.setCargo(cargo);
	 	 			sit = sitService.findById(1);
	 	 			fun.setSituacao(sit);
					if (set.contains(fun.getCodigoFun())) {
						int nada = 1;
					}
					else {
						set.add(new Funcionario(fun.getCodigoFun(), fun.getNomeFun(), fun.getEnderecoFun(), 
	 	 					fun.getBairroFun(), fun.getCidadeFun(), fun.getUfFun(),
	 	 					fun.getCepFun(), fun.getDddFun(), fun.getTelefoneFun(),
	 	 					fun.getCpfFun(), fun.getCargoFun(), fun.getPixFun(),
	 	 					fun.getSituacaoFun(), fun.getComissaoFun(), fun.getAdiantamentoFun(),
	 	 					fun.getCargo(), fun.getSituacao()));
					}
	 	 			lineFun = brFun.readLine();
 	 	 		}
 	 	 		brFun.close();
 	 	 		for (Object o : set) {
	 	 			if (o != null) {
	 	 				fun = (Funcionario) o;
	 	 				insertFun(fun);
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
	 
	public static void insertFun(Funcionario fun) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DB.getConnection();	
			st = conn.prepareStatement(
					"INSERT INTO funcionario " +
	 	 			      "(CodigoFun, NomeFun, EnderecoFun, BairroFun, CidadeFun, " +
	 	 			       "UfFun, CepFun, DddFun, TelefoneFun, " +
	 	 			       "CpfFun, PixFun, CargoFun, SituacaoFun, ComissaoFun, AdiantamentoFun," +
	 	 			       "CargoId, SituacaoId )" + 
	 	 				    "VALUES " +
	 	 			      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
	 	 	 					 Statement.RETURN_GENERATED_KEYS); 
	 	 	 
	 				st.setInt(1, fun.getCodigoFun());
	 				st.setString(2, fun.getNomeFun());
	 	 			st.setString(3, fun.getEnderecoFun());
	 	 			st.setString(4, fun.getBairroFun());
	 	 			st.setString(5, fun.getCidadeFun());
	 	 			st.setString(6, fun.getUfFun());
	 	 			st.setString(7, fun.getCepFun());
	 	 			st.setInt(8, fun.getDddFun());
	 	 			st.setInt(9, fun.getTelefoneFun());
	 	 			st.setString(10, fun.getCpfFun());
	 	 			st.setString(11, fun.getPixFun());
	 	 	   		st.setString(12, fun.getCargoFun());
	 	 	   		st.setString(13, fun.getSituacaoFun());
	 	 	   		st.setDouble(14, fun.getComissaoFun());
	 	 	   		st.setDouble(15, fun.getAdiantamentoFun());
	 	 	   		st.setInt(16, fun.getCargo().getCodigoCargo());
	 	 	   		st.setInt(17,  fun.getSituacao().getNumeroSit());
	 	 	   			
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
