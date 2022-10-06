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
import sgo.model.entities.Cliente;
import sgo.model.services.CargoService;
import sgo.model.services.SituacaoService;

public class ConvCli implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
 	 		String pathVei = "c:\\ARQS\\CGOTXT\\ATXTCLI.TXT";
 	 		Cliente cli = new Cliente(); 
 	 		List<Cliente> list1 = new ArrayList<>();
 	 				
 	 		try {
	 	 		BufferedReader brCli = new BufferedReader(new FileReader(pathVei));
	 			String lineVei = brCli.readLine();
 	 	 		while (lineVei != null) { 
	 	 			String[] campos = lineVei.split(";");
	 	 			int nada = 0;
					for (Cliente c : list1) {
						if (c.getCodigoCli().equals(cli.getCodigoCli())) {
							nada = 1;
						}
					}	
System.out.println(nada + " " + cli.getCodigoCli());							
					if (nada == 0) {
						list1.add(new Cliente(cli.getCodigoCli(), cli.getNomeCli(), cli.getRuaCli(), 
	 	 					cli.getNumeroCli(), cli.getComplementoCli(), cli.getBairroCli(),
	 	 					cli.getCidadeCli(), cli.getUfCli(), cli.getCepCli(), cli.getDdd01Cli(),
	 	 					cli.getTelefone01Cli(), cli.getDdd02Cli(), cli.getTelefone02Cli(),
	 	 					cli.getEmailCli(), cli.getPessoaCli(), cli.getCpfCli(), cli.getCnpjCli()));
					}
	 	 			lineVei = brCli.readLine();
 	 	 		}
 	 	 		brCli.close();
 	 	 		for (Cliente c : list1) {
	 	 			if (c != null) {
	 	 				cli = (Cliente) c;
System.out.println("c " + c);	 	 				
	 	 				insertCli(cli);
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
	 
	public static void insertCli(Cliente cli) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DB.getConnection();	
			st = conn.prepareStatement(
					"INSERT INTO cliente " +
	 	 			      "(CodigoCli, NomeCli, RuaCli, Numerocli, ComplementoCli, " +
							"BairroCli, CidadeCli, UfCli, CepCli, Ddd01Cli, Telefone01Cli, " +
	 	 			        "Ddd02Cli, Telefone02Cli, EmailCli, PessoaCli, CpfCli, CnpjCli )" + 
	 	 				    "VALUES " +
	 	 			      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
	 	 	 					 Statement.RETURN_GENERATED_KEYS); 
	 	 	 
	 				st.setInt(1, cli.getCodigoCli());
	 				st.setString(2, cli.getNomeCli());
	 				st.setString(3, cli.getRuaCli());
	 				st.setInt(4, cli.getNumeroCli());
	 				st.setString(5, cli.getComplementoCli());
	 				st.setString(6, cli.getBairroCli());
	 				st.setString(7, cli.getCidadeCli());
	 				st.setString(8, cli.getUfCli());
	 				st.setString(9, cli.getCepCli());
	 				st.setInt(10, cli.getDdd01Cli());
	 				st.setInt(11, cli.getTelefone01Cli());
	 				st.setInt(12, cli.getDdd02Cli());
	 				st.setInt(13, cli.getTelefone02Cli());
	 				st.setString(14, cli.getEmailCli());
	 	   			st.setString(15, String.valueOf(cli.getPessoaCli()));
	 	   			st.setString(16, cli.getCpfCli());
	 	   			st.setString(17, cli.getCnpjCli());
	 	 	   			
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
