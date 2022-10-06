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
import java.util.List;
import java.util.Set;

import db.DB;
import db.DbException;
import sgo.model.entities.Cliente;
import sgo.model.entities.Funcionario;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Orcamento;
import sgo.model.entities.Veiculo;
import sgo.model.services.ClienteService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.VeiculoService;

public class ConvOrc implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dataHj = new Date();
 	 		String pathOrc = "c:\\ARQS\\CGOTXT\\ATXTORC.TXT";
 	 		Orcamento orc = new Orcamento();
 	 		Set<Orcamento> set = new HashSet<>();
 	 		Funcionario fun = new Funcionario();
 	 		FuncionarioService funService = new FuncionarioService(); 
 	 		Cliente cli =  new Cliente();
 	 		ClienteService cliService = new ClienteService();
 	 		Veiculo vei = new Veiculo();
 	 		VeiculoService veiService = new VeiculoService();
 	 		OrcVirtual vir = new OrcVirtual();
 	 		OrcVirtualService virService = new OrcVirtualService();
 	 		Calendar cal = Calendar.getInstance();
 	 		int dd = 0;
 	 		int mm = 0;
 	 		int mi = 2000;
 	 		int aa = 0;
 	 		int num = 0;
 	 				
 	 		try {
	 	 		BufferedReader brOrc = new BufferedReader(new FileReader(pathOrc));
	 			String lineOrc = brOrc.readLine();
 	 	 		while (lineOrc != null) { 
	 	 			String[] campos = lineOrc.split(";");
	 	 			orc.setNumeroOrc(Integer.parseInt(campos[0]));
	 	 			orc.setDataOrc(dataHj);
	 	 			dd = Integer.parseInt(campos[9].substring(0, 2));
	 	 			mm = Integer.parseInt(campos[9].substring(2, 4));
	 	 			aa = 2000 + Integer.parseInt(campos[9].substring(4, 6));
	 	 			cal.setTime(orc.getDataOrc());
	 	 			cal.set(aa, (mm - 1), dd);
	 	 			orc.setDataOrc(cal.getTime());
	 	 			orc.setClienteOrc(campos[7]);
	 	 			orc.setFuncionarioOrc(campos[5]);
	 	 			orc.setPlacaOrc(campos[1]);
	 	 			orc.setKmInicialOrc(0);
	 	 			orc.setKmFinalOrc(Integer.parseInt(campos[2]));
	 	 			orc.setDescontoOrc(Double.parseDouble(campos[12]) / 100);
	 	 			orc.setTotalOrc(0.00);
	 	 			orc.setOsOrc(Integer.parseInt(campos[10]));	 	 			
					int codcli = Integer.parseInt(campos[6]);
	 	 			cli = cliService.findById(codcli);
	 	 			orc.setCliente(cli);
	 	 			orc.setClienteOrc(cli.getNomeCli());
	 	 			double serv = Double.parseDouble(campos[3]) / 100;
	 	 			List<OrcVirtual> listVir = virService.findByOrcto(orc.getNumeroOrc());
	 	 			orc.calculaTotalOrc(listVir);
	 	 			fun = funService.findById(Integer.parseInt(campos[4]));
	 	 			orc.setFuncionario(fun);
	 	 			orc.setVeiculo(vei);
	 	 			orc.setVirtual(vir);
	 	 			set.add(new Orcamento(orc.getNumeroOrc(), orc.getDataOrc(), 
 	 					orc.getClienteOrc(), orc.getFuncionarioOrc(), orc.getPlacaOrc(),
 	 					orc.getKmInicialOrc(), orc.getKmFinalOrc(), orc.getDescontoOrc(),
 	 					orc.getTotalOrc(), orc.getOsOrc(), orc.getCliente(),
 	 					orc.getFuncionario(), orc.getVeiculo(), orc.getVirtual()));
	 	 			for (OrcVirtual ov : listVir) {
System.out.println("total " + orc.getTotalOrc());	
	 	 			}
	 	 			lineOrc = brOrc.readLine();
 	 	 		}
 	 	 		brOrc.close();
 	 	 		for (Orcamento o : set) {
	 	 			if (o != null) {
	 	 				orc = (Orcamento) o;
	 	 				insertOrc(orc);
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
	 
	
	public static void insertOrc(Orcamento orc) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = DB.getConnection();	
			st = conn.prepareStatement(
					"INSERT INTO orcamento " +
						      "(NumeroOrc, DataOrc, ClienteOrc, FuncionarioOrc, PlacaOrc, KmInicialOrc, " +
						       "KmFinalOrc, DescontoOrc, TotalOrc, OsOrc, ClienteId, FuncionarioId) " +
		   				       "VALUES " +
						       "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
		 					 Statement.RETURN_GENERATED_KEYS); 

System.out.println(orc.getNumeroOrc());			
					st.setInt(1, orc.getNumeroOrc());
					st.setDate(2, new java.sql.Date(orc.getDataOrc().getTime()));
					st.setString(3, orc.getClienteOrc());
					st.setString(4, orc.getFuncionarioOrc());
					st.setString(5,  orc.getPlacaOrc());
					st.setInt(6, orc.getKmInicialOrc());
					st.setInt(7,  orc.getKmFinalOrc());
					st.setDouble(8, orc.getDescontoOrc());
					st.setDouble(9, orc.getTotalOrc());
					st.setInt(10, orc.getOsOrc());
					st.setInt(11, orc.getCliente().getCodigoCli());
					st.setInt(12, orc.getFuncionario().getCodigoFun());

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
