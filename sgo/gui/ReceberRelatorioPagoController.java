 package sgo.gui;

import java.io.IOException;
import java.net.URL;
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
import model.services.ParPeriodoService;
import sgcp.model.entityes.consulta.ParPeriodo;
import sgo.model.entities.Cliente;
import sgo.model.entities.Receber;
import sgo.model.services.ClienteService;
import sgo.model.services.EmpresaService;
import sgo.model.services.ReceberService;
 
public class ReceberRelatorioPagoController implements Initializable, DataChangeListener {

	@FXML
 	private TableView<Receber> tableViewRecebido;
 	
 	@FXML
 	private Label labelTitulo;
 	
 	@FXML
 	private TableColumn<Receber, Integer>  tableColumnNumeroOsRec;
 	
 	@FXML
 	private TableColumn<Receber, Date>  tableColumnDataOsRec;
 	
 	@FXML
 	private TableColumn<Receber, String> tableColumnClienteRec;

   	@FXML
 	private TableColumn<Receber, String> tableColumnPlacaRec;
 	 	
   	@FXML
 	private TableColumn<Receber, Integer> tableColumnParcelaRec;
 	 	
   	@FXML
 	private TableColumn<Receber, Date> tableColumnVencimentoRec;
 	 	
   	@FXML
 	private TableColumn<Receber, Double> tableColumnValorRec;
 	
   	@FXML
 	private TableColumn<Receber, Date> tableColumnPagamentoRec;
 	 	
   	@FXML
 	private TableColumn<Receber, String> tableColumnFormaRec;
 	 	
  	@FXML
 	private TableColumn<Receber, Receber> tableColumnEDITA;

	@FXML
	private Button btClienteRecebido;

 	@FXML
	private Button btPeriodoRecebido;

  	@FXML
  	private Button btImprimeRecebido;
	
	@FXML
	private Label labelUser;

	public String user = "";
  	String nomeTitulo = "Lista Contas a Recebidas ";
  	public static char opcao = 't';
	String classe = "Recebida";
	
 	static Integer codCli = null;
 	public static Integer numEmp = null;
	
// carrega aqui Updatetableview (metodo)
   	private ObservableList<Receber> obsList;
  
// injeção de dependenia sem implementar a classe (instanciar)
// acoplamento forte - implementa via set
 	
	private ReceberService service;
 	Receber receber = new Receber();
 	
  	public void setReceber (Receber receber) {
  		this.receber = receber;
 	}
 
// 	busca os dados no bco de dados	
 	public void setReceberService (ReceberService service) {
  		this.service = service;
 	}

   /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
   */

	Cliente objCli = new Cliente();
	ParPeriodo objPer = new ParPeriodo();

 	public void onBtClienteRecebidoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); 
		nomeTitulo = "Lista Contas Recebidas por Cliente";
		opcao = 'c';
		classe = "Cliente";
  		createDialogOpcao("/sgo/gui/PesquisaAlphaForm.fxml", objCli, objPer, (parentStage), 
  				(RecClienteFormController contC) -> {
  			contC.setDestino("Relatorio");
  			contC.setSituacao("Recebido");
			contC.setCliente(objCli);
			contC.setService(new ClienteService());
			contC.loadAssociatedObjects();
			contC.subscribeDataChangeListener(this);
			contC.updateFormData();
 		});
//		updateTableViewPago();
	}
 	
	@FXML
	public void onBtPeriodoRecebidoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		nomeTitulo = "Lista Contas Recebidas por Periodo";
		opcao = 'p';
		classe = "Periodo";
  		createDialogOpcao("/sgo/gui/RecPeriodoForm.fxml", objCli, objPer, (parentStage), 
  				(RecPeriodoFormController contP) -> {
  		contP.setPeriodo(objPer);
  		contP.setPeriodoService(new ParPeriodoService());
		contP.loadAssociatedObjects();
		contP.subscribeDataChangeListener(this);
		contP.updateFormData();
  		});
		updateTableViewPago();
  	}
  
  	@FXML
  	public void onBtImprimeRecebidoAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
  		 Receber obj = new Receber();
   		 createDialogOpcao("/sgo/gui/ReceberRelatorioImprimePago.fxml", objCli, objPer, (parentStage), 
  				(ReceberImprimePagoController contI) -> {
  			  		contI.setReceber(obj);
  			  		contI.setServices(new ReceberService(), new EmpresaService());
  			  		contI.setCli(codCli);
  			  		contI.setOpcao(opcao);	
  			  		contI.numEmp = numEmp;
  		});
//		updateTableViewPago();
  	}
  
 	// inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
 		initializeNodes();
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
 		tableColumnNumeroOsRec.setCellValueFactory(new PropertyValueFactory<>("osRec"));
		tableColumnDataOsRec.setCellValueFactory(new PropertyValueFactory<>("dataOsRec"));
		Utils.formatTableColumnDate(tableColumnDataOsRec, "dd/MM/yyyy");
   		tableColumnClienteRec.setCellValueFactory(new PropertyValueFactory<>("nomeClienteRec"));
		tableColumnPlacaRec.setCellValueFactory(new PropertyValueFactory<>("placaRec"));
		tableColumnParcelaRec.setCellValueFactory(new PropertyValueFactory<>("parcelaRec"));
		tableColumnVencimentoRec.setCellValueFactory(new PropertyValueFactory<>("dataVencimentoRec"));
		Utils.formatTableColumnDate(tableColumnVencimentoRec, "dd/MM/yyyy");
 		tableColumnValorRec.setCellValueFactory(new PropertyValueFactory<>("valorRec"));
 		Utils.formatTableColumnDouble(tableColumnValorRec, 2);
		tableColumnPagamentoRec.setCellValueFactory(new PropertyValueFactory<>("dataPagamentoRec"));
		Utils.formatTableColumnDate(tableColumnPagamentoRec, "dd/MM/yyyy");
   		tableColumnFormaRec.setCellValueFactory(new PropertyValueFactory<>("formaPagamentoRec"));
 		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewRecebido.prefHeightProperty().bind(stage.heightProperty());
 	}

/* 	
 * carregar o obsList para atz tableview	
 * tst de segurança p/ serviço vazio
 *  criando uma lista para receber os  s
 *  instanciando o obsList
 *  acrescenta o botao edit e remove
 */  
 	
 	public void updateTableViewPago() {
 		if (service == null) {
			throw new IllegalStateException("Serviço Rec está vazio");
 		}
 		labelUser.setText(user);
 		List<Receber> list = new ArrayList<>();
 		if (opcao == 't') {
 			list = service.findAllPago();
 			if (list.size() == 0) {
 				Alerts.showAlert("Contas Recebidas ", "Recebidas", "Não há pagamento ", AlertType.INFORMATION);
 			}	
 		} else {
 			if (opcao == 'c' && codCli != null) {
 				list = service.findByIdClientePago(codCli);
 	 			if (list.size() == 0) {
 	 				Alerts.showAlert("Contas Recebidas por Cliente ", "Cliente", "Não há pagamento para o Cliente ", AlertType.INFORMATION);
 	 			}	
 			} else {
 				if (opcao == 'p') {
 	 				list = service.findPeriodoPago();
 	 	 			if (list.size() == 0) {
 	 	 				Alerts.showAlert("Contas Recebidas por período ", "Período ", "Não há pagamento no período ", AlertType.INFORMATION);
 	 	 			}	
 				}
 			}
 		}
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
  		obsList = FXCollections.observableArrayList(list);
  		tableViewRecebido.setItems(obsList);
	} 	

	private synchronized <T> void createDialogOpcao(String absoluteName, Cliente objCli, 
   			ParPeriodo objPer, Stage parentStag, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			if (opcao == 'c')
			{	dialogStage.setTitle("Selecione Cliente                                             ");
			}
			if (opcao == 'p')
			{	dialogStage.setTitle("Selecione Período                                             ");
			}
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStag);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
		}
	}
 	
//  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
   		labelTitulo.setText(String.format(nomeTitulo));
 		updateTableViewPago();
	}
}  