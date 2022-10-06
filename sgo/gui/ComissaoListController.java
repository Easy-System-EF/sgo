package sgo.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
import sgo.model.entities.Adiantamento;
import sgo.model.entities.DadosFechamento;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.AnosService;
import sgo.model.services.BalcaoService;
import sgo.model.services.MesesService;
 
public class ComissaoListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private AdiantamentoService service;
	private BalcaoService balService;
	
	@FXML
 	private TableView<Adiantamento> tableViewComissao;

	@FXML
	private Label labelTitulo;
	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Adiantamento, Integer> tableColumnOsAdi;

 	@FXML
 	private TableColumn<Adiantamento, Integer> tableColumnBalAdi;

 	@FXML
 	private TableColumn<Adiantamento, String> tableColumnNomeFunAdi;

 	@FXML
 	private TableColumn<Adiantamento, Date> tableColumnDataAdi;

   	@FXML
 	private TableColumn<Adiantamento, String> tableColumnCargoAdi;
 	
   	@FXML
   	private TableColumn<Adiantamento, String> tableColumnSituacaoAdi;
   	
   	@FXML
   	private TableColumn<Adiantamento, Double> tableColumnComissaoAdi;
   	
   	@FXML
   	private Button btMesesCom;
   	 	
	@FXML
	private Label labelUser;

	public String user = "";
 	 		
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Adiantamento> obsList;
 
// auxiliar
 	String classe = "Adiantamento";
 	static int mesConsulta = 0;
 	static int anoConsulta = 0; 	
 	
// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setServices(AdiantamentoService service,
 							BalcaoService balService) {
 		this.service = service;
 		this.balService = balService;
 	}

	@FXML
	public void onBtMesesComAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		DadosFechamento obj = new DadosFechamento();
		classe = "Meses ";
  		createDialogOpcao("/sgo/gui/MesAnoCForm.fxml", parentStage, (ComissaoFormController contM) -> {
			contM.setDadosFechamento(obj);		
			contM.setServices(new MesesService(),
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
		labelTitulo.setText("Comissão");
		tableColumnOsAdi.setCellValueFactory(new PropertyValueFactory<>("OsAdi"));
		tableColumnBalAdi.setCellValueFactory(new PropertyValueFactory<>("BalcaoAdi"));
		tableColumnNomeFunAdi.setCellValueFactory(new PropertyValueFactory<>("NomeFunAdi"));
		tableColumnDataAdi.setCellValueFactory(new PropertyValueFactory<>("dataAdi"));
		Utils.formatTableColumnDate(tableColumnDataAdi, "dd/MM/yyyy");
		tableColumnCargoAdi.setCellValueFactory(new PropertyValueFactory<>("CargoAdi"));
		tableColumnSituacaoAdi.setCellValueFactory(new PropertyValueFactory<>("SituacaoAdi"));
		tableColumnComissaoAdi.setCellValueFactory(new PropertyValueFactory<>("ComissaoAdi"));
		Utils.formatTableColumnDouble(tableColumnComissaoAdi, 2);
  		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
 		Stage stage = (Stage) Main.getMainScene().getWindow();
 		tableViewComissao.prefHeightProperty().bind(stage.heightProperty());
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
			throw new IllegalStateException("Serviço está vazio");
 		}
 		labelUser.setText(user);
 		List<Adiantamento> list = new ArrayList<>();
		list = service.findMes(mesConsulta, anoConsulta);
		List<Adiantamento> newList = list.stream()
				. filter(x -> x.getTipoAdi() != "A")
				. collect(Collectors.toList());
		
  		obsList = FXCollections.observableArrayList(newList);
  		tableViewComissao.setItems(obsList);
	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
