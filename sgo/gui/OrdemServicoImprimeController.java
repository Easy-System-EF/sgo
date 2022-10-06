package sgo.gui;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import gui.util.FormataGabarito;
import gui.util.Imprimir;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sgo.model.entities.Empresa;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Orcamento;
import sgo.model.entities.OrdemServico;
import sgo.model.entities.Receber;
import sgo.model.services.EmpresaService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.OrcamentoService;
import sgo.model.services.OrdemServicoService;
import sgo.model.services.ReceberService;

public class OrdemServicoImprimeController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

	private static String COURIER;
	private static Font getFont(String COURIER, float size) {
		return null;
	}

	@FXML
	private Button btImprimeOS;

	public static Integer numEmp = 0;
	public static Integer codOs;

	Locale localeBr = new Locale("pt", "BR");
 	DecimalFormat df = new DecimalFormat("#,###,##0.00"); 
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	int tamS = 40;
 
	double valorOs1 = 0.00;
	double valorOs2 = 0.00;
	double valorOs3 = 0.00;

	String dataOs1;
	String dataOs2;
	String dataOs3;
	String numFn;
 	String nomeVai = "";
 	String nomeVem = "";
 	String pagamento = "";
 	String prazo = "";
 	String fS = "";
	
	String path = "c:\\Dataprol\\WINPRINT\\_os.prn";
	String pathI = "C:\\Arqs\\impr\\os.txt";
	
	String linhaNome = "";
	String linhaEndereco = "";
	String linhaTelMailPix = "";
	
	OrdemServico ordemServico = new OrdemServico();
	Orcamento orcamento = new Orcamento();
	private Empresa emp;
	StringBuilder sb = new StringBuilder();

	private OrdemServicoService osService;
	private OrcamentoService orcService;
	private OrcVirtualService virService;
	private ReceberService recService;
	private EmpresaService empService;

	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}
	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}
	public void setEmpresa(Empresa emp) {
		this.emp = emp;
	}

	public void setOSImprime(OrdemServicoService osService, 
									OrcamentoService orcService, 
									OrcVirtualService virService,
									ReceberService recService, 
									EmpresaService empService) {
		this.osService = osService;
		this.orcService = orcService;
		this.virService = virService;
		this.recService = recService;
		this.empService = empService;
	}

	@FXML
	public void onBtImprimeOSAction(ActionEvent event) throws ParseException, IOException {
		montaEmpresa();
   		grava();
  		Imprimir.relatorio(pathI);
     	Utils.currentStage(event).close();
   	}

	private void montaEmpresa() {
		emp = empService.findById(numEmp);
		linhaNome = emp.getNomeEmp();
		linhaEndereco = emp.getEnderecoEmp();
		linhaTelMailPix = "Tel.: "  + emp.getTelefoneEmp() + 
				" - Email: " + emp.getEmailEmp() + " - Pix: " + emp.getPixEmp();	
	}
	
	List<OrcVirtual> listVir = new ArrayList<>();
	List<Receber> rec = new ArrayList<>();
		
 	public void grava() {
			try {	
				BufferedWriter bwO = new BufferedWriter(new FileWriter(pathI));
 					{	ordemServico = osService.findById(codOs);
 						orcamento = orcService.findById(ordemServico.getOrcamentoOS());
					 	if (ordemServico.getPagamentoOS() == 1) {
 							pagamento = "Dinheiro";
 						}
 						if (ordemServico.getPagamentoOS() == 2) {
 							pagamento = "Pix     ";
 						}
 						if (ordemServico.getPagamentoOS() == 3) {
 							pagamento = "Débito  ";
 						}
 						if (ordemServico.getPagamentoOS() == 4) {
 							pagamento = "Cartão  ";
 						}
 						if (ordemServico.getPrazoOS() == 1) {
 							prazo = "A visa ";
 						}
 						if (ordemServico.getPrazoOS() == 10) {
 							prazo = "10 dias";
 						}
 						if (ordemServico.getPrazoOS() == 15) {
 							prazo = "15 dias";
 						}
 						if (ordemServico.getPrazoOS() == 30) {
 							prazo = "30 dias";
 						}
 						
 						rec = recService.findByAllOs(ordemServico.getNumeroOS());
 						for (Receber r : rec) {
 							if (r.getOsRec().equals(ordemServico.getNumeroOS())) {
 								if (r.getParcelaRec() == 1) {
 									dataOs1 = sdf.format(r.getDataVencimentoRec());
 									valorOs1 = r.getValorRec();
 								}
 								else {
 	 								if (r.getParcelaRec() == 2) {
 	 									dataOs2 = sdf.format(r.getDataVencimentoRec());
 	 									valorOs2 = r.getValorRec();
 	 								}
 	 								else {
 	 	 								if (r.getParcelaRec() == 3) {
 	 	 									dataOs3 = sdf.format(r.getDataVencimentoRec());
 	 	 									valorOs3 = r.getValorRec();
 	 	 								} 									
 	 								}
 								}
 							}	
 						}
					 	String lh = toLinha();
					 	bwO.write(lh);
					 	bwO.newLine();
					}
					bwO.close();
				}	
	 			catch(	IOException e2) {
	 				e2.getMessage();	 			
	 			}
				finally {		
				}
	 		}

	public String toLinha() {
		// linha01
					sb.append("                               Ordem de Serviço" + "\n" + "\n");
		// linhaNome
					sb.append(linhaNome + "\n" + "\n");
		//linha02
					numFn = FormataGabarito.formataNnf(ordemServico.getNumeroOS());
					sb.append("Número.....: " + numFn);
					sb.append(ordemServico.getNumeroOS());
					sb.append("                  ");
					sb.append("Data..: " + sdf.format(ordemServico.getDataOS()) + "\n");
		//linha03			
					sb.append("Placa......: " + ordemServico.getPlacaOS());
					sb.append("                 ");
					sb.append("Km....: " + orcamento.getKmFinalOrc() + "\n");
		//linha04
					sb.append("Cliente....: " + orcamento.getClienteOrc() + "\n");
		//linha05
					sb.append("Atendente..: " + orcamento.getFuncionarioOrc() + "\n");
		//linha06			
					sb.append("Pagamento..: " + pagamento + "                ");
					sb.append("Prazo: " + prazo + "\n" );
		//linha07			
					sb.append("Parcela....: " + ordemServico.getParcelaOS());
					sb.append("                       ");
					sb.append("Total.: " + df.format(ordemServico.getValorOS()) + "\n");
		//linha08			
					sb.append("NNF........: " + ordemServico.getNnfOS() + "\n" + "\n");
		//linha09			
					sb.append("                               Vencimento"  + "\n");
		//linha91
					sb.append("Data: " + dataOs1 + "                     ");
					sb.append("Valor: " + df.format(valorOs1) + "\n");
		//linha92
					if (valorOs2 > 0) {
						sb.append("Data: " + dataOs2 + "                     ");
						sb.append("Valor: " + df.format(valorOs2) + "\n");						
					} 
		//linha93
					if (valorOs3 > 0) {
						sb.append("Data: " + dataOs3 + "                     ");
						sb.append("Valor: " + df.format(valorOs3) + "\n" + "\n");
					}	
					else {
						sb.append("\n");
					}
		//linha10
					sb.append(	
							"                               Material" + "\n");
		//linha11
					sb.append(
						 	"****************************************************************" + "\n");
		//linha12			
					sb.append(
							"Nome                                 Qtd       Preço       Valor" + "\n");
		//linha13
					sb.append(
						 	"****************************************************************" + "\n");

					listVir = virService.findByOrcto(orcamento.getNumeroOrc());
					for (OrcVirtual v : listVir) {
						tamNome(v.getNomeMatVir());
						String qtdFd = FormataGabarito.formataQtd5(v.getQuantidadeMatVir());
						String precoFd = FormataGabarito.formataDouble(v.getPrecoMatVir());
						String totFd = FormataGabarito.formataDouble(v.getTotalMatVir());
		//linha14
// qdo for linha continua, com tamanho variado em q ter String.format
						String linha14 = "";
						nomeVem = v.getNomeMatVir();
						if (nomeVem.length() > 34) { 
							nomeVem = v.getNomeMatVir().substring(0, 34);
						}	
						linha14 = String.format("%-35s", nomeVem) +
								  String.format("%s", qtdFd) +
								  String.format("%s", df.format(v.getQuantidadeMatVir())) +
								  String.format("%s", precoFd) +
								  String.format("%s", df.format(v.getPrecoMatVir())) +
								  String.format("%s", totFd) +
								  String.format("%s", df.format(v.getTotalMatVir()));
						sb.append(linha14 + "\n");
					}
					if (orcamento.getDescontoOrc() > 0) {
						String descFd = FormataGabarito.formataDouble(orcamento.getDescontoOrc());
						String linha15 = "";
						linha15 = String.format("%-52s", "Desconto(-)") +
							  String.format("%s", descFd) +
							  String.format("%s", df.format(orcamento.getDescontoOrc()));
						sb.append(linha15 + "\n");
					}
					sb.append("\n" + "\n");
					sb.append("Obs.: " + ordemServico.getObservacaoOS() + "\n\n\n\n\n");					
// linhaEndereco
					sb.append(linhaEndereco);
					sb.append("\n");
// linhaTelMailPix
					sb.append(linhaTelMailPix);
					sb.append("\n");
				return sb.toString();
			}	
	
 	private String tamNome(String nomeVai) {
 		tamS = nomeVai.length();
 		if (tamS > 40) {
 			tamS = 39;	
 			nomeVem = nomeVai.substring(0, 39);
 		}	else {
 			nomeVem = nomeVai;
 		}
		return nomeVem;
 	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
}
