package sgo.gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
import gui.util.Mascaras;
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
import model.services.CompromissoService;
import model.services.TipoFornecedorService;
import sgo.model.entities.Adiantamento;
import sgo.model.entities.Balcao;
import sgo.model.entities.DadosFechamento;
import sgo.model.entities.Funcionario;
import sgo.model.entities.Orcamento;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.AnosService;
import sgo.model.services.BalcaoService;
import sgo.model.services.DadosFechamentoService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.MesesService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.OrcamentoService;
import sgo.model.services.OrdemServicoService;
 
public class DadosFechamentoListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private DadosFechamentoService service;
	
	@FXML
 	private TableView<DadosFechamento> tableViewMensal;

	@FXML
	private Label labelTitulo;
	
// c/ entidade e coluna	
	
 	@FXML
 	private TableColumn<DadosFechamento, Integer> tableColumnOsMensal;

 	@FXML
 	private TableColumn<DadosFechamento, Integer> tableColumnBalMensal;

   	@FXML
 	private TableColumn<DadosFechamento, Date> tableColumnDataMensal;
 	
   	@FXML
 	private TableColumn<DadosFechamento, String> tableColumnClienteMensal;
 	
   	@FXML
 	private TableColumn<DadosFechamento, String> tableColumnFuncionarioMensal;
 	
   	@FXML
   	private TableColumn<DadosFechamento, Double> tableColumnValorOsMensal;
   	
   	@FXML
   	private TableColumn<DadosFechamento, Double> tableColumnValorMaterialMensal;
   	
   	@FXML
   	private TableColumn<DadosFechamento, Double> tableColumnValorComissaoMensal;
   	
   	@FXML
   	private TableColumn<DadosFechamento, Double> tableColumnValorResultadoMensal;

   	@FXML
   	private TableColumn<DadosFechamento, Double> tableColumnValorAcumuladoMensal;   	
   	
   	@FXML
   	private Button btMesesMensal;
   	 	
	@FXML
	private Label labelUser;

	public String user = "";
 	 		
// carrega aqui lista Updatetableview (metodo)
 	private ObservableList<DadosFechamento> obsList;
 
// auxiliar
 	String classe = "Dados Fechamento Mensal";
 	 
 	// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setServices(DadosFechamentoService service) {
 		this.service = service;
 	}
 	
	@FXML
	public void onBtMesesMensalAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		DadosFechamento obj = new DadosFechamento();
		Adiantamento objAdi = new Adiantamento();
		Orcamento objOrc = new Orcamento();
		Funcionario objFun = new Funcionario();
		Balcao objBal = new Balcao();
		classe = "Meses ";
  		createDialogOpcao("/sgo/gui/MesAnoMForm.fxml", parentStage, 
  				(DadosFechamentoFormController contF) -> {
			contF.setDadosEntityes(obj, objOrc, objAdi, objFun, objBal);
			contF.setServices(new DadosFechamentoService(),
						      new AdiantamentoService(),
							  new MesesService(),
							  new AnosService(),
							  new OrdemServicoService(),
							  new OrcamentoService(),
							  new OrcVirtualService(),
							  new FuncionarioService(),
							  new BalcaoService(),
							  new TipoFornecedorService(),
							  new CompromissoService());
			contF.loadAssociatedObjects();
			contF.updateFormData();
 		});
		updateTableView();
	}
	 	 	
 	private <T> void createDialogOpcao(String absoluteName, Stage parentStage, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe, e.getMessage(), AlertType.ERROR);
		}
	}
 	
 	 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
			initializeNodes();
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
		labelTitulo.setText("Fechamento Mensal ");
		tableColumnOsMensal.setCellValueFactory(new PropertyValueFactory<>("OsMensal"));
		tableColumnBalMensal.setCellValueFactory(new PropertyValueFactory<>("BalMensal"));
		tableColumnDataMensal.setCellValueFactory(new PropertyValueFactory<>("DataMensal"));
		Utils.formatTableColumnDate(tableColumnDataMensal, "dd/MM/yyyy");
		tableColumnClienteMensal.setCellValueFactory(new PropertyValueFactory<>("ClienteMensal"));
		tableColumnFuncionarioMensal.setCellValueFactory(new PropertyValueFactory<>("FuncionarioMensal"));
		tableColumnValorOsMensal.setCellValueFactory(new PropertyValueFactory<>("ValorOsMensal"));
		Utils.formatTableColumnDouble(tableColumnValorOsMensal, 2);
		tableColumnValorMaterialMensal.setCellValueFactory(new PropertyValueFactory<>("ValorMaterialMensal"));
		Utils.formatTableColumnDouble(tableColumnValorMaterialMensal, 2);
		tableColumnValorComissaoMensal.setCellValueFactory(new PropertyValueFactory<>("ValorComissaoMensal"));
		Utils.formatTableColumnDouble(tableColumnValorComissaoMensal, 2);
		tableColumnValorResultadoMensal.setCellValueFactory(new PropertyValueFactory<>("ValorResultadoMensal"));
		Utils.formatTableColumnDouble(tableColumnValorResultadoMensal, 2);
		tableColumnValorAcumuladoMensal.setCellValueFactory(new PropertyValueFactory<>("ValorAcumuladoMensal"));
		Utils.formatTableColumnDouble(tableColumnValorAcumuladoMensal, 2);
  		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) Main.getMainScene().getWindow();
 		tableViewMensal.prefHeightProperty().bind(stage.heightProperty());
 	}

/* 	
 * carregar o obsList para atz tableview	
 * tst de segurança p/ serviço vazio
 *  criando uma lista para receber os services
 *  instanciando o obsList
 *  acrescenta o botao edit e remove
 */  
 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Serviço Dados está vazio");
 		}
 		labelUser.setText(user);
		classe = "Dados Folha";
		List<DadosFechamento> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewMensal.setItems(obsList);
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
