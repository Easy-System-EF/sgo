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
import java.util.HashSet;
import java.util.Set;

import db.DB;
import db.DbException;
import sgcp.model.entityes.Fornecedor;
import sgo.model.services.CargoService;
import sgo.model.services.SituacaoService;

public class ConvFor implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 	 		String pathFo = "c:\\ARQS\\CGOTXT\\ATXTFOR.TXT";
 	 		Fornecedor forn = new Fornecedor(); 
 	 		Set<Fornecedor> set = new HashSet<>();
 	 				
 	 		try {
	 	 		BufferedReader brForn = new BufferedReader(new FileReader(pathFo));
	 			String lineForn = brForn.readLine();
 	 	 		while (lineForn != null) { 
	 	 			String[] campos = lineForn.split(";");
	 	 			forn.setCodigo(Integer.parseInt(campos[0]));
	 	 			forn.setRazaoSocial(campos[1]);
	 	 			forn.setRua(campos[2]);
	 	 			forn.setNumero(Integer.parseInt(campos[3]));
	 	 			forn.setComplemento(campos[4]);
	 	 			forn.setBairro(campos[5]);
	 	 			forn.setCidade(campos[6]);
	 	 			forn.setUf(campos[7]);
  	 	 			forn.setCep(Integer.parseInt(campos[8]));
  	 	 			forn.setDdd01(Integer.parseInt(campos[9]));
  	 	 			forn.setTelefone01(0);
  	 	 			forn.setDdd02(0);
  	 	 			forn.setTelefone02(0);
  	 	 			forn.setContato(campos[11]);
  	 	 			forn.setDddContato(Integer.parseInt(campos[12]));
 	 				forn.setTelefoneContato(0);
  	 	 			forn.setEmail(campos[14]);
  	 	 			forn.setObservacao(campos[15]);
  	 	 			forn.setPix("pix@pix.com");
  	 	  
					if (set.contains(forn.getCodigo())) {
						int nada = 1;
					}
					else {
						set.add(new Fornecedor(forn.getCodigo(), forn.getRazaoSocial(), 
							forn.getRua(), forn.getNumero(), forn.getComplemento(), 
	 	 					forn.getBairro(), forn.getCidade(), forn.getUf(),
	 	 					forn.getCep(), forn.getDdd01(), forn.getTelefone01(),
	 	 					forn.getDdd02(), forn.getTelefone02(), forn.getContato(),
	 	 					forn.getDddContato(), forn.getTelefoneContato(), forn.getEmail(),
	 	 					forn.getPix(), forn.getObservacao()));
					}
	 	 			lineForn = brForn.readLine();
 	 	 		}
 	 	 		brForn.close();
 	 	 		for (Fornecedor f : set) {
	 	 			if (f != null) {
	 	 				forn = (Fornecedor) f;
	 	 				insertFun(forn);
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
	 
	public static void insertFun(Fornecedor forn) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DB.getConnection();	
			st = conn.prepareStatement(
					"INSERT INTO fornecedor " +
	 	 			      "(Codigo, RazaoSocial, Rua, Numero, Complemento, Bairro, Cidade, " +
	 	 			       "Uf, Cep, Ddd01, Telefone01, Ddd02, Telefone02, Contato, DddContato, " +
	 	 			       "TelefoneContato, Email, Pix, Observacao)" + 
	 	 				    "VALUES " +
	 	 			      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
	 	 	 					 Statement.RETURN_GENERATED_KEYS); 
	 	 	 
	 				st.setInt(1, forn.getCodigo());
	 				st.setString(2, forn.getRazaoSocial());
	 	 			st.setString(3, forn.getRua());
	 	 			st.setInt(4, forn.getNumero());
	 	 			st.setString(5, forn.getComplemento());
	 	 			st.setString(6, forn.getBairro());
	 	 			st.setString(7, forn.getCidade());
	 	 			st.setString(8, forn.getUf());
	 	 			st.setInt(9, forn.getCep());
	 	 			st.setInt(10, forn.getDdd01());
	 	 			st.setInt(11, forn.getTelefone01());
	 	 			st.setInt(12, forn.getDdd02());
	 	 			st.setInt(13, forn.getTelefone02());
	 	 			st.setString(14, forn.getContato());
	 	 			st.setInt(15, forn.getDddContato());
	 	 			st.setInt(16, forn.getTelefoneContato());
	 	 			st.setString(17, forn.getEmail());
	 	 			st.setString(18, forn.getPix());
	 	 	   		st.setString(19, forn.getObservacao());
	 	 	   			
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
