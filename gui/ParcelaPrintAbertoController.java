package gui;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.exception.ValidationException;
import model.services.FornecedorService;
import model.services.ParPeriodoService;
import model.services.ParcelaService;
import model.services.TipoFornecedorService;
import sgcp.model.entityes.Fornecedor;
import sgcp.model.entityes.Parcela;
import sgcp.model.entityes.TipoFornecedor;
import sgcp.model.entityes.consulta.ParPeriodo;
import sgo.model.services.EmpresaService;
 
public class ParcelaPrintAbertoController implements Initializable, DataChangeListener {

	@FXML
	private Label labelTitulo;
	
	@FXML
 	private TableView<Parcela> tableViewParcelaAberto;

  	@FXML
 	private TableColumn<Parcela, String>  tableColumnFornecedorAberto;
 	
 	@FXML
 	private TableColumn<Parcela, Integer>  tableColumnNNFAberto;

  	@FXML
 	private TableColumn<Parcela, Date>  tableColumnVencimentoAberto;

  	@FXML 
 	private TableColumn<Parcela, Integer>  tableColumnParcelaAberto;

   	@FXML
 	private TableColumn<Parcela, Double>  tableColumnValorAberto;

 	@FXML
 	private TableColumn<Parcela, Double>  tableColumnJurosAberto;

 	@FXML
 	private TableColumn<Parcela, Double>  tableColumnDescontoAberto;

 	@FXML
 	private TableColumn<Parcela, Double>  tableColumnAPagarAberto;

  	@FXML
 	private TableColumn<Parcela, Double>  tableColumnTotalAberto;

   	@FXML
 	private Button btPeriodoAberto;

   	@FXML
 	private Button btFornecedorAberto;

  	@FXML
 	private Button btTipoAberto;

  	@FXML
  	private Button btImprimeAberto;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usu�rio";		
 	public static Integer codigo = null;
 	public static Integer numEmp = null;
	int flagg = 0;
	String classe = "Parcela emAbrto ";
	Double maoObra = 0.00;

  	/*  	
* parametros de find... na updateTable
* 1 = geral aberto
* 2 = geral pago
*/
/* periodo, fornecedor, tipo
* a = aberto geral
* b = pago geral 	
* p = periodo aberto
* q = periodo pago
* f = fornecedor aberto
* g = fornecedor pago
* t = tipo aberto
* u = tipo pago
*/
  	
  	String nomeTitulo = "Lista Contas a Pagar ";
  	char opcao = 'o';
  	int tipo = 0; 
	int codFor = 0;
	int codTipo = 0;
 // carrega aqui Updatetableview (metodo)
   	private ObservableList<Parcela> obsListPar;
  
// inje��o de dependenia sem implementar a classe (instanciar)
// acoplamento forte - implementa via set
 	
	private ParcelaService parService;
 	Parcela parcela = new Parcela();
 	
  	public void setParcela (Parcela parcela) {
  		this.parcela = parcela;
 	}
 
// 	busca os dados no bco de dados	
 	public void setParcelaService (ParcelaService parService) {
  		this.parService = parService;
 	}

// 	busca os dados no bco de dados	
 	public void setFornecedorService (FornecedorService forService) {
  		this.forService = forService;
 	}

 	private FornecedorService forService;
 	Fornecedor fornecedor = new Fornecedor();
 	 
  	public void setFornecedor (Fornecedor fornecedor) {
  		this.fornecedor = fornecedor;
 	}
 
 	public void setTipoFornecedorService (TipoFornecedorService tipoService) {
  		this.tipoService = tipoService;
 	}

 	private TipoFornecedorService tipoService;
 	TipoFornecedor tipofornecedor = new TipoFornecedor();
 	 
  	public void setTipoFornecedor (TipoFornecedor tipofornecedor) {
  		this.tipofornecedor = tipofornecedor;
 	}
 
 	private ParPeriodoService perService;
 	ParPeriodo parPeriodo = new ParPeriodo();
 	
	public void setParPeriodoService(ParPeriodoService perService) {
   		this.perService = perService;
 	}

  	public void setParPeriodo (ParPeriodo parPeriodo) {
  		this.parPeriodo = parPeriodo;
 	}
 
   /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
   */

 	ParPeriodo obj1 = new ParPeriodo();
	Fornecedor obj3 = new Fornecedor();
	TipoFornecedor obj4 = new TipoFornecedor();
 	
	@FXML
	public void onBtPeriodoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
// instanciando novo obj  e injetando via
 		setOpcao('p');
		nomeTitulo = "Lista Contas a Pagar no Periodo";
  		createDialogForms("/gui/ParPeriodoForm.fxml", obj1, obj3, obj4, (parentStage), 
 		(ParPeriodoFormController contP) -> {
		contP.setPeriodo(obj1, obj1);
		contP.setPeriodoService(new ParPeriodoService());
		contP.loadAssociatedObjects();
		contP.subscribeDataChangeListener(this);
		contP.updateFormData();
   		});
 	}
 	public void onBtFornecedorAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); 
  		setOpcao('f');
		nomeTitulo = "Lista Contas a Pagar por Fornecedor";
  		createDialogForms("/gui/ParFornecedorForm.fxml", obj1, obj3, obj4, (parentStage), 
  				(ParFornecedorFormController contF) -> {
			contF.setFornecedor(obj3);
			contF.setService(new FornecedorService());
			contF.loadAssociatedObjects();
			contF.subscribeDataChangeListener(this);
			contF.updateFormData();
 		});
	}
 	
	@FXML
	public void onBtTipoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
// instanciando novo obj  e injetando via
 		setOpcao('t');
		nomeTitulo = "Lista Contas a Pagar por Tipo";
  		createDialogForms("/gui/ParTipoForm.fxml", obj1, obj3, obj4, (parentStage), 
  				(ParTipoFormController contP) -> {
  		contP.setPeriodo(obj1, obj1);
  		contP.setTipoFornecedor(obj4);
  		contP.setPeriodoService(new ParPeriodoService(), new TipoFornecedorService());
		contP.loadAssociatedObjects();
		contP.subscribeDataChangeListener(this);
		contP.updateFormData();
  		});
  		}
 
  	@FXML
  	public void onBtImprimeAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
  		 Parcela obj = new Parcela();
  	 	 createDialogPrintAberto(codFor, codTipo, opcao, obj, "/gui/ParcelaImprimeAberto.fxml", parentStage);
  		}
 
 	// inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
 		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
   		labelTitulo.setText(String.format(nomeTitulo));
  		tableColumnFornecedorAberto.setCellValueFactory(new PropertyValueFactory<>("nomeFornecedorPar"));
		tableColumnNNFAberto.setCellValueFactory(new PropertyValueFactory<>("nnfPar"));
		tableColumnVencimentoAberto.setCellValueFactory(new PropertyValueFactory<>("dataVencimentoPar"));
 		Utils.formatTableColumnDate(tableColumnVencimentoAberto, "dd/MM/yyyy");
		tableColumnParcelaAberto.setCellValueFactory(new PropertyValueFactory<>("numeroPar"));
		tableColumnValorAberto.setCellValueFactory(new PropertyValueFactory<>("valorPar"));
		Utils.formatTableColumnDouble(tableColumnValorAberto, 2);
   		tableColumnJurosAberto.setCellValueFactory(new PropertyValueFactory<>("jurosPar"));
		Utils.formatTableColumnDouble(tableColumnJurosAberto, 2);
  		tableColumnDescontoAberto.setCellValueFactory(new PropertyValueFactory<>("descontoPar"));
		Utils.formatTableColumnDouble(tableColumnDescontoAberto, 2);
  		tableColumnAPagarAberto.setCellValueFactory(new PropertyValueFactory<>("totalPar"));
		Utils.formatTableColumnDouble(tableColumnAPagarAberto, 2);
   		tableColumnTotalAberto.setCellValueFactory(new PropertyValueFactory<>("ResultadoParStr"));
//		Utils.formatTableColumnDouble(tableColumnTotalAberto, 2);
   // para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewParcelaAberto.prefHeightProperty().bind(stage.heightProperty());
    	}

   	char setOpcao(char letra) {
 		return opcao = letra;
 	}
 
/* 	
 * carregar o obsList para atz tableview	
 * tst de seguran�a p/ servi�o vazio
 *  criando uma lista para receber os  s
 *  instanciando o obsList
 *  acrescenta o botao edit e remove
 */  
 	
 	public void updateTableViewAberto() {
 		if (parService == null) {
 			throw new IllegalStateException("Servi�o est� vazio");
 		}
 		labelUser.setText(user);
 		ValidationException exception = new ValidationException("Validation error");
 		if (opcao == 'o')
   		{	porPeriodo();	
   		}
  		somaTotal();
   		tableViewParcelaAberto.setItems(obsListPar);
     }
 	
 	private void porPeriodo() {
 		try {
 			SimpleDateFormat sdfi = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 			SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 			Date dti = sdfi.parse("01/01/2001 00:00:00");  
 			Date dtf = sdff.parse("31/01/2041 00:00:00");

 			ParPeriodo per = new ParPeriodo();
 			per = parPeriodo;
 			List<ParPeriodo> listPerio = perService.findAll();
 			for (ParPeriodo pe : listPerio)
 			{	per.setFornecedor(pe.getFornecedor());
 				per.setTipoFornecedor(pe.getTipoFornecedor());
 			}
 			per.setIdPeriodo(1);
 			per.setDtiPeriodo(dti);
 			per.setDtfPeriodo(dtf);
 			perService.update(per);
  		}	catch (ParseException e) {
 			e.printStackTrace();
 			Alerts.showAlert("ParseException ", "Erro Data ", e.getMessage(), AlertType.ERROR);
 		}
   	}

 	private void somaTotal() {
  		double soma = 0.00;
  		List<ParPeriodo> listPer = perService.findAll();
		for (ParPeriodo per : listPer) {
			codFor = per.getFornecedor().getCodigo();
		    codTipo = per.getTipoFornecedor().getCodigoTipo();
		}    
 		List<Parcela> list = new ArrayList<>();
  		if (opcao == 'o') {
 			list = parService.findAllAberto();
			if (list.size() == 0) {
				Alerts.showAlert("Parcela ", null, "N�o h� parcela em aberto ", AlertType.INFORMATION);
			}	
  		}
  		if (opcao == 'p') {
 			list = parService.findPeriodoAberto();
			if (list.size() == 0) {
				Alerts.showAlert("Parcela ", null, "N�o h� parcela em aberto ", AlertType.INFORMATION);
			}	
 		}
  		if (opcao == 'f') {
  			if (codigo != null) {
  				list = parService.findByIdFornecedorAberto(codigo);
  				if (list.size() == 0) {
  					Alerts.showAlert("Parcela ", null, "N�o h� parcela em aberto ", AlertType.INFORMATION);
  				}	
  			}
 		}
  		if (opcao == 't') {
			list = parService.findByIdTipoAberto(codTipo);
			if (list.size() == 0) {
				Alerts.showAlert("Parcela ", null, "N�o h� parcela em aberto ", AlertType.INFORMATION);
			}	
 		}

 		DecimalFormat df = new DecimalFormat("##,##0.00");
 	   	String resultadoParStr = ""; 

   		for (Parcela p : list)
		{	if (p.getResultadoPar() == null)
			{	p.setResultadoPar(0.00);
			}
 			soma = soma + p.getTotalPar();
 			p.setResultadoPar(soma);
 	 	   	resultadoParStr = df.format(p.getResultadoPar());
 	 	   	p.setResultadoParStr(resultadoParStr);
 		}
		obsListPar = FXCollections.observableArrayList(list);
  		tableViewParcelaAberto.setItems(obsListPar);
    }
 	
	private synchronized <T> void createDialogForms(String absoluteName, ParPeriodo obj1, 
   			Fornecedor obj3, TipoFornecedor obj4, Stage parentStag, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			if (opcao == 'f')
			{	dialogStage.setTitle("Selecione Fornecdor                                             ");
			}
			if (opcao == 't')
			{	dialogStage.setTitle("Selecione Tipo                                             ");
			}
			if (opcao == 'p')
			{	dialogStage.setTitle("Selecione Periodo                                             ");
			}
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStag);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
	}
 	
 	private void createDialogPrintAberto(Integer CodFor, Integer codTipo, char op, Parcela obj, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
 			ParcelaImprimeAbertoController controller = loader.getController();
 			controller.setParcela(obj);
 			controller.setServices(new ParcelaService(), new EmpresaService());
 			controller .setOpcao(op);
  			controller.setcodTipo(codTipo);
 			controller.setFor(CodFor);
 			controller.numEmp = numEmp;
   			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Lista Contas(s) em Aberto                                             ");
 			dialogStage.setScene(new Scene(pane));
 			dialogStage.setResizable(false);
  			dialogStage.initOwner(parentStage);
 			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
  	}

//  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
   		labelTitulo.setText(String.format(nomeTitulo));
 		updateTableViewAberto();
	}
}  