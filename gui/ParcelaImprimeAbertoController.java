package gui; 

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
import model.services.ParcelaService;
import sgcp.model.entityes.Parcela;
import sgo.model.entities.Empresa;
import sgo.model.services.EmpresaService;

public class ParcelaImprimeAbertoController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;
	
	private static String COURIER;
	private static Font getFont(String COURIER, float size) {
		return null;
	}

 	@FXML
	private Button btImprimeAberto;
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
 	String nomeCab = null;
 
  	String path = "c:\\Dataprol\\WINPRINT\\_parAberto.prn";
	String pathI = "C:\\Arqs\\impr\\relatorio.txt";

	int contf = 0;
	int contl = 90;
	int i = 0;
	int flag = 0;
	int tam = 0;

	double totVlr = 0.00;
	double totDes = 0.00;
	double totJur = 0.00;
	double totTot = 0.00;

	Parcela parcela = new Parcela();
	private ParcelaService parService;
	private Empresa empresa;
	private EmpresaService empService;
 	private char opcao;
	private Integer codFor;
	private Integer codTipo;

	String nomeEmp = null;
	String linha01 = "";
	String linha02 = "";
	String linha03 = "******************************************************************"; 
	String linha04 = "Fornecedor                                  NNF Parcela Vencimento";
	String linha41 = "         Valor         Desconto            Juros             Total";
	String linha05 = "";
	String linha51 = "";
	String linha52 = "";

	List<Parcela> list = new ArrayList<>();
 	
   	char setOpcao(char letra) {
 		return opcao = letra;
 	}
  	int setFor(Integer num) {
 		return codFor = num;
 	}  	
  	int setcodTipo(Integer num) {
 		return codTipo = num;
 	}
  	
  	
	public void setParcela(Parcela parcela) {
		this.parcela = parcela;
	}
	public void setServices(ParcelaService parService, EmpresaService empService) {
		this.parService = parService;
		this.empService = empService;
	}
	
	@FXML
	public void onBtImprimeAbertoAction(ActionEvent event) throws ParseException, IOException {
		flag = 0;
		empresa = empService.findById(numEmp);
		nomeEmp = empresa.getNomeEmp();
  		grava();
  		Imprimir.relatorio(pathI);
     	Utils.currentStage(event).close();
   	}

	public void grava() {
	 		try
// 			{	BufferedWriter bwP = new BufferedWriter(new FileWriter(pathI));
 			{	OutputStreamWriter bwP = new OutputStreamWriter(
 					new FileOutputStream(pathI), "UTF-8");
 					if (flag == 0) {
 						list = titulo(parcela, codFor, codTipo, opcao);	
 	 					for (Parcela p : list) { 
 	 						i += 1;
							if (i == 1) {
 								if (opcao == 't') {
 									nomeCab += p.getTipoFornecedor().getNomeTipo();
 								}
 								if (opcao == 'p') {
 									dtpI = sdf.format(p.getPeriodo().getDtiPeriodo());
									dtpF = sdf.format(p.getPeriodo().getDtfPeriodo());
 									nomeCab +=  dtpI + " a " + dtpF; 
 								}
 								linha02 = String.format("%s", nomeCab);
							}	
 							if (contl > 49) {
 								contf += 01;
 								cabecalho();
 								bwP.write(linha01 +"\n");
 								bwP.write(linha02 +"\n");
 								bwP.write("\n");
 								bwP.write(linha03 +"\n");
 								bwP.write(linha04 +"\n");
 								bwP.write(linha41 +"\n");
 								bwP.write(linha03 +"\n");
 								bwP.write("\n");
 								contl = 8;
 							}	
								
 	 						totVlr = totVlr + p.getValorPar();
    						totDes = totDes + p.getDescontoPar();
 	 						totJur = totJur + p.getJurosPar();
 	 						totTot = totTot + p.getTotalPar();
 	 						dtv = sdf.format(p.getDataVencimentoPar());

 	 						linha05 = String.format("%-41s", p.getNomeFornecedorPar()) +
 	 								  String.format("%6d", p.getNnfPar()) +
  									  String.format("%8d", p.getNumeroPar()) +
  									  String.format("%11s",  dtv);
 	 						
							linha51 = String.format("%s%12s", "  ", df.format(p.getValorPar())) +
 	 								  String.format("%s%12s", complementa(5), df.format(p.getDescontoPar())) +
 	 								  String.format("%s%12s", complementa(5), df.format(p.getJurosPar())) +
 	 								  String.format("%s%12s", complementa(6), df.format(p.getTotalPar()));
 							linha52 = 
	 	 	 					"------------------------------------------------------------------";
 							if (contl > 49) {
 								contf += 01;
 								cabecalho();
 								bwP.write(linha01 +"\n");
 								bwP.write(linha02 +"\n");
 								bwP.write("\n");
 								bwP.write(linha03 +"\n");
 								bwP.write(linha04 +"\n");
 								bwP.write(linha41 +"\n");
 								bwP.write(linha03 +"\n");
 								bwP.write("\n");
 								contl = 8;
 							}	

 							bwP.write(linha05 +"\n");
 	 						bwP.write(linha51 +"\n");
	 	 	 				bwP.write(linha52 +"\n");

	 	 	 				totVlr += p.getValorPar();
							totJur += p.getJurosPar();
							totDes += p.getDescontoPar();
							totTot += p.getValorPar() + p.getJurosPar() + p.getDescontoPar();
 				 			contl += 3;
 							if (contl > 49) {
 								contf += 01;
 								cabecalho();
 								bwP.write(linha01 +"\n");
 								bwP.write(linha02 +"\n");
 								bwP.write("\n");
 								bwP.write(linha03 +"\n");
 								bwP.write(linha04 +"\n");
 								bwP.write(linha41 +"\n");
 								bwP.write(linha03 +"\n");
 								bwP.write("\n");
 								contl = 8;
 							}	
 				 			if (i == list.size()) {
 								String linha06 = String.format("%s%12s", "  ", df.format(totVlr)) +
 	 	 								  String.format("%s%12s", complementa(5), df.format(totDes)) +
 	 	 								  String.format("%s%12s", complementa(5), df.format(totJur)) +
 	 	 								  String.format("%s%12s", complementa(6), df.format(totTot));
 	 							if (contl > 49) {
 	 								contf += 01;
 	 								cabecalho();
 	 								bwP.write(linha01 +"\n");
 	 								bwP.write(linha02 +"\n");
 	 								bwP.write("\n");
 	 								bwP.write(linha03 +"\n");
 	 								bwP.write(linha04 +"\n");
 	 								bwP.write(linha41 +"\n");
 	 								bwP.write(linha03 +"\n");
 	 								bwP.write("\n");
 	 								contl = 8;
 								} 
 	 							bwP.write(linha06 + "\n");
	 	 					}	
 	 					}
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

	private List <Parcela> titulo(Parcela parcela, Integer codFor, Integer codTipo, char opcao) {
   		if (opcao == 'o')
 		{	list = parService.findAllAberto();
 			nomeCab = "Contas a Pagar";
  		}
  		if (opcao == 'p')
 		{	list = parService.findPeriodoAberto();
 			nomeCab = "Contas a Pagar no Periodo: ";
 		}
  		if (opcao == 'f')
 		{ 	list = parService.findByIdFornecedorAberto(codFor);
 			nomeCab = "Contas a Pagar por Fornecedor ";
 		}
  		if (opcao == 't')
		{	list = parService.findByIdTipoAberto(codTipo);
			nomeCab = "Contas a Pagar por Tipo: ";
		}
 		return list;	
	}	
	
	private void cabecalho(){
		linha01 = String.format("%-30s", nomeEmp) +
				  String.format("%s", "Data: ") + 
				  String.format("%-24s", dtHj) +
				  String.format("%s", "Fl- ") +
				  String.format("%2s", contf);
	}

 	private String complementa(int tam) {
 		String compl = "";
 		for (int ii = 1; ii < tam + 1; ii ++) {
 			compl += " ";
 		}
 		return compl;
 	}
 	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
