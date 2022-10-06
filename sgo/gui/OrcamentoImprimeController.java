package sgo.gui;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

import gui.util.FormataGabarito;
import gui.util.Imprimir;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sgo.model.entities.Empresa;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Orcamento;
import sgo.model.services.EmpresaService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.OrcamentoService;

public class OrcamentoImprimeController implements Initializable, Serializable {

	private static final long serialVersionUID = 1L;

	private static String COURIER;
	private static Font getFont(String COURIER, float size) {
		return null;
	}

 	@FXML
	private Button btImprimeOrcamento;

	public static Integer numEmp = 0;
	public static Integer codOrc;

	Locale localeBr = new Locale("pt", "BR");
 	DecimalFormat df = new DecimalFormat("##,##0.00"); 
 	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date data1 = new Date();
 
	double totalOrc = 0.00;
	double subTotalOrc = 0.00;
	double descontoOrc = 0.00;

 	String nomeMat = "";
	
	String path = "c:\\Dataprol\\WINPRINT\\_orcto.prn";
	String pathI = "C:\\Arqs\\impr\\orcto.txt";

	String linha01 = "";
	String linhaNome = "";
	String linhaEndereco = "";
	String linhaTelMailPix = "";
	String linha02 = "";
	String linha03 = "";
	String linha04 = "";
	String linha05 = "";
	String linha06 = "";
	String linha07 = "";
	String linha08 = "";
	String linha09 = "";
	String linha10 = "";
	String linha11 = "";
	String linha12 = "";
	
	private Orcamento orcamento;
	private Empresa emp;
	
	private OrcamentoService orcService;
	private EmpresaService empService;

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}
	public void setEmpresa(Empresa emp) {
		this.emp = emp;
	}

	public void setOrcamentoService(OrcamentoService orcService, EmpresaService empService) {
		this.orcService = orcService;
		this.empService = empService;
	}

	OrcVirtual virtual = new OrcVirtual();
	private OrcVirtualService virService;

	public void setOrcVirtual(OrcVirtual virtual) {
		this.virtual = virtual;
	}

	public void setOrcVirtualService(OrcVirtualService virService) {
		this.virService = virService;
	}

	@FXML
	public void onBtImprimeOrcamentoAction(ActionEvent event) throws ParseException, IOException {
		Stage parentStage = Utils.currentStage(event) ;
		montaEmpresa();
   		grava();
		Imprimir.relatorio(pathI);
//		File file = new File(pathI);
     	Utils.currentStage(event).close();
//		Utils.deleteArq(file);
   	}

	private void montaEmpresa() {
		emp = empService.findById(numEmp);
		linhaNome = emp.getNomeEmp();
		linhaEndereco = emp.getEnderecoEmp();
		linhaTelMailPix = "Tel.: " + emp.getTelefoneEmp() + 
				" - Email: " + emp.getEmailEmp() + " - Pix: " + emp.getPixEmp();	
	}
	
	List<OrcVirtual> listVir = new ArrayList<>();

/*	aqui gera uma stream p/ UTF-8 => windows/preferences/workSpace/UTF-8
 * 	 		try	{	BufferedWriter bwO = new BufferedWriter(new OutputStreamWriter(
 * 					new FileOutputStream(path), "UTF-8"));
 */
	
 	public void grava() {
//		 	try	{	OutputStreamWriter bwO = new OutputStreamWriter(
// 				 		new FileOutputStream(path), "UTF-8");
			try {	
				BufferedWriter bwO = new BufferedWriter(new FileWriter(pathI));
 					{	orcamento = orcService.findById(codOrc);
 					
 						linha01 = "                             Orçamento";
						linha02 = String.format("Número...: %6d", orcamento.getNumeroOrc()) +
						          String.format("%18s%s", "Data: ", sdf.format(orcamento.getDataOrc()));
						linha03 = String.format("Placa....: %s", orcamento.getPlacaOrc()) +
								  String.format("%17s%s", "Km..: ", orcamento.getKmFinalOrc());
						linha04 = String.format("Cliente..: %s", orcamento.getClienteOrc());
						linha05 = String.format("Atendente: %s", orcamento.getFuncionarioOrc());
 					 	linha06 = String.format("                             Material");
					 	linha07 = "*****************************************************************"; 
					 	linha08 = "Nome                                 Qtd        Preço       Valor";					 			

					 	bwO.write(linha01);
					 	bwO.newLine();
					 	bwO.write(linhaNome);
					 	bwO.newLine();
					 	bwO.newLine();
						bwO.write(linha02);
					 	bwO.newLine();
						bwO.write(linha03);
					 	bwO.newLine();
						bwO.write(linha04);
					 	bwO.newLine();
						bwO.write(linha05);
					 	bwO.newLine();
					 	bwO.newLine();
						bwO.write(linha06);
					 	bwO.newLine();
						bwO.write(linha07);
					 	bwO.newLine();
						bwO.write(linha08);
					 	bwO.newLine();
						bwO.write(linha07);
					 	bwO.newLine();
					 	bwO.newLine();
							
						listVir = virService.findByOrcto(codOrc);
						for (OrcVirtual v : listVir) {
							if (v.getMaterial().getNomeMat().length() > 35) {
								nomeMat = v.getMaterial().getNomeMat().substring(0, 35);
							}	else {
								nomeMat = v.getMaterial().getNomeMat();
							}
							String qtdFd = FormataGabarito.formataQtd5(v.getQuantidadeMatVir());
							totalOrc += v.getTotalMatVir();
							linha09 = String.format("%-36s", nomeMat) +
											 String.format("%s%s", qtdFd, v.getQuantidadeMatVir()) +
											 String.format("%13s", df.format(v.getPrecoMatVir())) +
											 String.format("%12s", df.format(v.getTotalMatVir()));
							bwO.write(linha09); 
						 	bwO.newLine();
 						}
						if (orcamento.getDescontoOrc() > 0) {
							descontoOrc = orcamento.getDescontoOrc();
							subTotalOrc = totalOrc;
							linha10 = String.format("%s%57s", "SubTotal", df.format(subTotalOrc));
							linha11 = String.format("%s%57s", "Desconto", df.format(descontoOrc));
							totalOrc -= descontoOrc;
							linha12 = String.format("%s%57s", "Total   ", df.format(totalOrc));
							bwO.write(linha10);
							bwO.newLine();
							bwO.write(linha11);
							bwO.newLine();
							bwO.write(linha12);
							bwO.newLine();
						} else {
							linha10 = String.format("%s57%s", "Total   ", df.format(totalOrc));
							bwO.write(linha10);
							bwO.newLine();
						}
 					}
				 	bwO.newLine();
				 	bwO.newLine();
				 	bwO.newLine();
				 	bwO.newLine();
				 	bwO.write(linhaEndereco);
				 	bwO.newLine();
				 	bwO.write(linhaTelMailPix);
				 	bwO.newLine();
 					bwO.close();
	 			}	
	 			catch(	IOException e2) {
	 				e2.getMessage();	 			
	 			}
				finally {
					
			}
	 	}
 	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
