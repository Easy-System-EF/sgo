package sgo.gui; 

import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import gui.util.Imprimir;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import sgo.model.entities.Empresa;
import sgo.model.entities.Receber;
import sgo.model.services.EmpresaService;
import sgo.model.services.ReceberService;

public class ReceberImprimePagoController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;
	
	private static String COURIER;
	private static Font getFont(String COURIER, float size) {
		return null;
	}

 	@FXML
	private Button btImprimeReceber;

//***
 	public static Integer numEmp = null;
 	
	Locale localeBr = new Locale("pt", "BR");
 	DecimalFormat df = new DecimalFormat("#,###,##0.00"); 
 	DecimalFormat dff = new DecimalFormat("00"); 
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date data1 = new Date();
	String dtHj = sdf.format(data1);
	String dtpI;
	String dtpF;
	String dtv;
	String dtp;
 	String nomeCab = null;
 	String nomeEmp = null;
 
  	String path = "c:\\Dataprol\\WINPRINT\\_cRecebedidas.prn";
	String pathI = "C:\\Arqs\\impr\\.cRecebidas.txt";
	String pathT = "c:\\arqs\\impr\\tst.txt";

	int contf = 0;
	int contl = 77;
	int i = 0;
	int flag = 0;
	int tam = 0;
	int cont = 0;

	double totVlr = 0.00;

	Receber receber = new Receber();
//***
	Empresa empresa = new Empresa();
	private ReceberService service;
//***
	private EmpresaService empService;
 	private char opcao = ' ';
	private Integer codCli = 0;

	String linha01 = "";
	String linha02 = "";
	String linha03 = "******************************************************************"; 
	String linha04 = "Cliente                                      Os    Data    Placa"; 
	String linha41 = "Forma    Parcela        Valor Vencimento Data Pagamento";
	String linha05 = "";
	String linha51 = "";
	String linha52 = "";
	String linhaNada = String.format("%s, %d", "********** ", cont);

	List<Receber> list = new ArrayList<>();
 	
   	char setOpcao(char letra) {
 		return opcao = letra;
 	}
  	int setCli(Integer num) {
 		return codCli = num;
 	}  	
  	
	public void setReceber(Receber receber) {
		this.receber = receber;
	}
//***	
	public void setServices(ReceberService service, EmpresaService empService) {
		this.service = service;
		this.empService = empService;
	}
	
	@FXML
	public void onBtImprimeReceberAction(ActionEvent event) throws ParseException, IOException {
		flag = 0;		
//***
		empresa = empService.findById(numEmp);
//***
		nomeEmp = empresa.getNomeEmp();
  		grava();
		Imprimir.relatorio(pathI);		
     	Utils.currentStage(event).close();
   	}

	public void teste() {
	 		try
// 			{	BufferedWriter bwP = new BufferedWriter(new FileWriter(path));
 			{	OutputStreamWriter bwP = new OutputStreamWriter(
 					new FileOutputStream(pathI), "UTF-8");
 					for (i = 1 ; i < 70 ; i++) {
 						cont = i;
 						bwP.write(linhaNada + cont + "\n");
 					}
 	 				bwP.close();
// 	 				PrintStream out = new PrintStream(System.out , true, "UTF-8");
	 			}	
 	 			catch(IllegalArgumentException e1) {
	 				System.out.println(e1.getMessage());
	 			}	catch(	IOException e2) {
	 				System.out.println(e2.getMessage());	 			
	 			}
	 		}

	public void grava() {
 		try
//			{	BufferedWriter bwP = new BufferedWriter(new FileWriter(path));
			{	OutputStreamWriter bwP = new OutputStreamWriter(
					new FileOutputStream(pathI), "UTF-8");
					if (flag == 0) {
						list = titulo(receber, codCli, opcao);	
	 					for (Receber r : list) {
	 						i += 1;
	 						if (i == 1) {
								if (opcao == 'p') {
									dtpI = sdf.format(r.getPeriodo().getDtiPeriodo());
									dtpF = sdf.format(r.getPeriodo().getDtfPeriodo());
									nomeCab +=  dtpI + " a " + dtpF; 
								}
								linha02 = String.format("%s", nomeCab);
	 						}	
	 						if (contl > 49) {
 								bwP.write("\n");
 								bwP.write("\n");
	 							contf += 01;								
	 							cabecalho();
	 							bwP.write(linha01 + "\n");
	 							bwP.write(linha02 + "\n");
	 							bwP.write("\n");
	 							bwP.write(linha03 + "\n");
	 							bwP.write(linha04 + "\n");
	 							bwP.write(linha41 + "\n");
	 							bwP.write(linha03 + "\n");
	 							bwP.write("\n");
	 							contl = 8;
	 						}
	 						totVlr = totVlr + r.getValorRec();
	 						dtv = sdf.format(r.getDataVencimentoRec());
	 						dtp = sdf.format(r.getDataPagamentoRec());
	 						String nome = r.getNomeClienteRec();
	 						linha05 = String.format("%-41s", nome) +
	 								  String.format("%6d", r.getOsRec()) +
	 								  String.format("%s", " ") +
	 								  String.format("%10s",  dtv) +
	 								  String.format("%s", " ") +
	 								  String.format("%s", r.getPlacaRec());
	 						linha51 = String.format("%-9s", r.getFormaPagamentoRec()) +
	 								  String.format("%7d", r.getParcelaRec()) +
	 								  String.format("%13s", df.format(r.getValorRec())) +
	 								  String.format("%s", " ") +
	 								  String.format("%s", dtv) +
	 								  String.format("%s", " ") +
	 								  String.format("%14s", dtp);
	 						linha52 = 
 	 	 					"------------------------------------------------------------------";
 							if (contl > 49) {
 								bwP.write("\n");
 								bwP.write("\n");
 								cabecalho();
 								contf += 01;
	 							bwP.write(linha01 + "\n");
	 							bwP.write(linha02 + "\n");
	 							bwP.write("\n");
	 							bwP.write(linha03 + "\n");
	 							bwP.write(linha04 + "\n");
	 							bwP.write(linha41 + "\n");
	 							bwP.write(linha03 + "\n");
	 							bwP.write("\n");
	 							contl = 8;
 							}	
	 						bwP.write(linha05 + "\n");
	 						bwP.write(linha51 + "\n");
	 						bwP.write(linha52 + "\n");
	 						contl += 3;
	 						if (i == list.size()) {
	 							String linha06 = String.format("%s", "Total---------->") +
	 											 String.format("%13s", df.format(totVlr));
	 							if (contl > 49) {
	 								bwP.write("\n");
	 								bwP.write("\n");
	 								cabecalho();
	 								contf += 01;
		 							bwP.write(linha01 + "\n");
		 							bwP.write(linha02 + "\n");
		 							bwP.write("\n");
		 							bwP.write(linha03 + "\n");
		 							bwP.write(linha04 + "\n");
		 							bwP.write(linha41 + "\n");
		 							bwP.write(linha03 + "\n");
		 							bwP.write("\n");
		 							contl = 8;
	 							} else {
	 								bwP.write("\n");
	 							}	 						
	 							bwP.write(linha06);
	 						}	
	 					}
					}	
	 				bwP.close();
//	 				PrintStream out = new PrintStream(System.out , true, "UTF-8");
 			}	
	 			catch(IllegalArgumentException e1) {
 				System.out.println(e1.getMessage());
 			}	catch(	IOException e2) {
 				System.out.println(e2.getMessage());	 			
 			}
 		}

	private List <Receber> titulo(Receber receber, Integer codCli, char opcao) {
   		if (opcao == 't') {
 			list = service.findAllPago();
 			nomeCab = "Contas Recebidas";
  		}
  		if (opcao == 'p') {
 			list = service.findPeriodoPago();
 			nomeCab = "Contas Recebidas no Periodo: ";
 		}
  		if (opcao == 'c') {
 		 	list = service.findByIdClientePago(codCli);
 			nomeCab = "Contas a Recebidas por Cliente ";
 		}
 		return list;	
	}	
	
	private void cabecalho(){
//***
			linha01 = String.format("%-30s", nomeEmp) +
					  String.format("%s", "Data: ") + 
					  String.format("%-24s", dtHj) +
					  String.format("%s", "Fl- ") +
					  String.format("%2s", dff.format(contf));
	}
 	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
