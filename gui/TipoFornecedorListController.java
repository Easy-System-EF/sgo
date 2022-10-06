package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbException;
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
import model.services.TipoFornecedorService;
import sgcp.model.entityes.TipoFornecedor;
 
public class TipoFornecedorListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private TipoFornecedorService service;
	
	@FXML
 	private TableView<TipoFornecedor> tableViewTipoFornecedor;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<TipoFornecedor, Integer>  tableColumnCodigoTipo;
 	
 	@FXML
 	private TableColumn<TipoFornecedor, String> tableColumnNomeTipo;

  	@FXML
 	private TableColumn<TipoFornecedor, TipoFornecedor> tableColumnEDITA;

 	@FXML
 	private TableColumn<TipoFornecedor, TipoFornecedor> tableColumnREMOVE ;

 	@FXML
 	private Button btNew;

	@FXML
	private Label labelUser;

	String classe = "Tipo Fornecedor ";
	public String user = "usu�rio";	
	
 	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<TipoFornecedor> obsList;
 
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
  	@FXML
  	public void onBtNewAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 TipoFornecedor obj = new TipoFornecedor();
 		 createDialogForm(obj, "/gui/TipoFornecedorForm.fxml", parentStage);
// 		System.out.println("novo");
  	}
 	
// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setTipoFornecedorService(TipoFornecedorService service) {
 		this.service = service;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnCodigoTipo.setCellValueFactory(new PropertyValueFactory<>("codigoTipo"));;
		tableColumnNomeTipo.setCellValueFactory(new PropertyValueFactory<>("nomeTipo"));
// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewTipoFornecedor.prefHeightProperty().bind(stage.heightProperty());
 	}

/* 	
 * carregar o obsList para atz tableview	
 * tst de seguran�a p/ servi�o vazio
 *  criando uma lista para receber os services
 *  instanciando o obsList
 *  acrescenta o botao edit e remove
 */  
 	public void updateTableView() {
 		if (service == null) {
			throw new IllegalStateException("Servi�o est� vazio");
 		}
 		labelUser.setText(user);
		List<TipoFornecedor> list = service.findAll();
 		obsList = FXCollections.observableArrayList(list);
		tableViewTipoFornecedor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (s� sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/ 	
	private void createDialogForm(TipoFornecedor obj, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			TipoFornecedorFormController controller = loader.getController();
			controller.user = user;
// injetando passando parametro obj 			
			controller.setTipoFornecedor(obj);
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setTipoFornecedorService(new TipoFornecedorService());
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite TipoFornecedor                                             ");
 			dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
// quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
// travada enquanto n�o sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
 	}

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

/*
 * metodo p/ botao edit do frame
 * ele cria bot�o em cada linha 
 * o cell instancia e cria
 */  
	private void initEditButtons() {
		  tableColumnEDITA.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnEDITA.setCellFactory(param -> new TableCell<TipoFornecedor, TipoFornecedor>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(TipoFornecedor obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
		 
		      setGraphic(button); 
		      button.setOnAction( 
		      event -> createDialogForm( 
		        obj, "/gui/TipoFornecedorForm.fxml",Utils.currentStage(event)));
 		    } 
		  }); 
		}
	

	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<TipoFornecedor, TipoFornecedor>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(TipoFornecedor obj, boolean empty) { 
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

	private void removeEntity(TipoFornecedor obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirma��o", "Tem certeza que deseja excluir");
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Servi�o est� vazio");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbException e) {
				Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
 			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
