package sgo.gui;

import java.io.IOException;
import java.net.URL;
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
import sgo.model.entities.DadosFolhaMes;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.AnosService;
import sgo.model.services.DadosFolhaMesService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.MesesService;
 
public class DadosFolhaListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private DadosFolhaMesService service;
	
	@FXML
 	private TableView<DadosFolhaMes> tableViewDados;

	@FXML
	private Label labelTitulo;
	
// c/ entidade e coluna	
	
 	@FXML
 	private TableColumn<DadosFolhaMes, String> tableColumnNomeDados;

   	@FXML
 	private TableColumn<DadosFolhaMes, String> tableColumnCargoDados;
 	
   	@FXML
   	private TableColumn<DadosFolhaMes, String> tableColumnSituacaoDados;
   	
   	@FXML
   	private TableColumn<DadosFolhaMes, Double> tableColumnSalarioDados;
   	
   	@FXML
   	private TableColumn<DadosFolhaMes, Double> tableColumnComissaoDados;
   	
   	@FXML
   	private TableColumn<DadosFolhaMes, Double> tableColumnAdiantamentoDados;

   	@FXML
   	private TableColumn<DadosFolhaMes, Double> tableColumnReceberDados;   	
   	
   	@FXML
   	private TableColumn<DadosFolhaMes, Double> tableColumnTotalDados;   	
   	
   	@FXML
   	private Button btMeses;
   	 	
	@FXML
	private Label labelUser;

	public String user = "";
 	 		
// carrega aqui lista Updatetableview (metodo)
 	private ObservableList<DadosFolhaMes> obsList;
 
// auxiliar
 	String classe = "Dados Folha ";
 	 
 	// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setServices(DadosFolhaMesService service) {
 		this.service = service;
 	}
 	
	@FXML
	public void onBtMesesAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		DadosFolhaMes obj = new DadosFolhaMes();
		classe = "Meses ";
  		createDialogOpcao("/sgo/gui/MesAnoFForm.fxml", parentStage, (DadosFolhaFormController contM) -> {
			contM.setDadosFolhaMes(obj);		
			contM.setServices(new DadosFolhaMesService(),
						      new AdiantamentoService(),
						      new FuncionarioService(),
							  new MesesService(),
							  new AnosService());
			contM.loadAssociatedObjects();
			contM.updateFormData();
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
		labelTitulo.setText("Dados da Folha ");
		tableColumnNomeDados.setCellValueFactory(new PropertyValueFactory<>("NomeDados"));
		tableColumnCargoDados.setCellValueFactory(new PropertyValueFactory<>("CargoDados"));
		tableColumnSituacaoDados.setCellValueFactory(new PropertyValueFactory<>("SituacaoDados"));
		tableColumnSalarioDados.setCellValueFactory(new PropertyValueFactory<>("SalarioDados"));
		tableColumnComissaoDados.setCellValueFactory(new PropertyValueFactory<>("ComissaoDados"));
		tableColumnAdiantamentoDados.setCellValueFactory(new PropertyValueFactory<>("ValeDados"));
		tableColumnReceberDados.setCellValueFactory(new PropertyValueFactory<>("ReceberDados"));
		tableColumnTotalDados.setCellValueFactory(new PropertyValueFactory<>("TotalDados"));
  		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) Main.getMainScene().getWindow();
 		tableViewDados.prefHeightProperty().bind(stage.heightProperty());
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
		List<DadosFolhaMes> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDados.setItems(obsList);
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
