package sgo.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sgo.model.entities.ReposicaoVeiculo;
import sgo.model.services.ReposicaoVeiculoService;
 
public class ReposicaoVeiculoListController implements Initializable, DataChangeListener {

	@FXML
 	private TableView<ReposicaoVeiculo> tableViewReposicaoVeiculo;
 	
 	@FXML
 	private Label labelTitulo;
 	
 	@FXML
 	private TableColumn<ReposicaoVeiculo, Integer>  tableColumnNumeroOsRep;
 	
 	@FXML
 	private TableColumn<ReposicaoVeiculo, Date>  tableColumnDataOsRep;
 	
 	@FXML
 	private TableColumn<ReposicaoVeiculo, String> tableColumnClienteRep;

 	@FXML
 	private TableColumn<ReposicaoVeiculo, String> tableColumnPlacaRep;

   	@FXML
 	private TableColumn<ReposicaoVeiculo, String> tableColumnDDDRep;
 	 	
   	@FXML
 	private TableColumn<ReposicaoVeiculo, Integer> tableColumnTelefoneRep;
 	 	
   	@FXML
 	private TableColumn<ReposicaoVeiculo, String> tableColumnMaterialRep;
 	 	
   	@FXML
 	private TableColumn<ReposicaoVeiculo, Integer> tableColumnKmRep;
 	 	
   	@FXML
 	private TableColumn<ReposicaoVeiculo, Date> tableColumnDataRep;

  	@FXML
 	private TableColumn<ReposicaoVeiculo, ReposicaoVeiculo> tableColumnREMOVE;

	@FXML
	private Button btDados;

	@FXML
	private Label labelUser;

	public String user = "";
 	 		
// auxiliar 	
 	String classe = "Reposição";
 	String nomeTitulo = "Consulta Reposição do Veículo";
 	String opcao = "a";
 	static Integer kmDado = null;
 	static String placaDado = null;
 	
// carrega aqui os dados Updatetableview (metodo)
 	private ObservableList<ReposicaoVeiculo> obsList;
 
 // injeção de dependenia sem implementar a classe (instanciat)
 // acoplamento forte - implementa via set
 	private ReposicaoVeiculoService service;
  	
// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setReposicaoVeiculoService(ReposicaoVeiculoService service) {
	 		this.service = service;
 	}
 	
 	public void onBtDadosAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); 
		nomeTitulo = "Dados da Reposição de Veículo ";
		classe = "Reposição";
  		createDialogOpcao("/sgo/gui/ReposicaoDadosForm.fxml", (parentStage), 
  				(ReposicaoDadosFormController contRD) -> {
			contRD.subscribeDataChangeListener(this);
			contRD.updateTableView();
 		});
  	 	nomeTitulo = "Consulta Reposição do Veículo ";
  	 	opcao = "p";
		updateTableView();
	}
 	
 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
  		tableColumnNumeroOsRep.setCellValueFactory(new PropertyValueFactory<>("osRep"));
		tableColumnDataOsRep.setCellValueFactory(new PropertyValueFactory<>("dataRep"));
		Utils.formatTableColumnDate(tableColumnDataOsRep, "dd/MM/yyyy");
   		tableColumnClienteRep.setCellValueFactory(new PropertyValueFactory<>("clienteRep"));
   		tableColumnPlacaRep.setCellValueFactory(new PropertyValueFactory<>("placaRep"));
  		tableColumnDDDRep.setCellValueFactory(new PropertyValueFactory<>("dddClienteRep"));
  		tableColumnTelefoneRep.setCellValueFactory(new PropertyValueFactory<>("telefoneClienteRep"));
  		tableColumnMaterialRep.setCellValueFactory(new PropertyValueFactory<>("materialRep"));
  		tableColumnKmRep.setCellValueFactory(new PropertyValueFactory<>("proximaKmRep"));
  		tableColumnDataRep.setCellValueFactory(new PropertyValueFactory<>("proximaDataRep"));
		Utils.formatTableColumnDate(tableColumnDataRep, "dd/MM/yyyy");
 		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewReposicaoVeiculo.prefHeightProperty().bind(stage.heightProperty());
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
			throw new IllegalStateException("Serviço Rep está vazio");
 		}
 		labelUser.setText(user);
 		List<ReposicaoVeiculo> list = new ArrayList<>();
 		if (opcao == "a") {
 			list = service.findAllData();
 		} else {
 			if (placaDado != null && kmDado != null) {
 				list = service.findByPlaca(placaDado, kmDado);
 			}	
 		}
  		labelTitulo.setText(String.format("%s ", nomeTitulo));
  		obsList = FXCollections.observableArrayList(list);
  		tableViewReposicaoVeiculo.setItems(obsList);
		initRemoveButtons();
	}
 	
	private synchronized <T> void createDialogOpcao(String absoluteName,  
					Stage parentStag, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			 
			T cont = loader.getController();
 			initializeAction.accept(cont);

 			Stage dialogStage = new Stage();
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
 	
// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
		private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
		private void notifyDataChangeListerners() {
			for (DataChangeListener listener : dataChangeListeners) {
				listener.onDataChanged();
			}
		}

//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
		public void subscribeDataChangeListener(DataChangeListener listener) {
				dataChangeListeners.add(listener);
		}
		
//  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<ReposicaoVeiculo, ReposicaoVeiculo>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(ReposicaoVeiculo obj, boolean empty) { 
		            super.updateItem(obj, empty); 
		 
		            if (obj == null) { 
		                setGraphic(null); 
		                return; 
		            } 
		 
		            setGraphic(button); 
		            button.setOnAction(event -> removeEntity(obj)); 
		        } 
		    });
		} 

	private void removeEntity(ReposicaoVeiculo obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serviço está vazio");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
			}
		}
	}
 }
