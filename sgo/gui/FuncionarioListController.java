package sgo.gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sgo.model.entities.Funcionario;
import sgo.model.services.CargoService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.SituacaoService;
 
public class FuncionarioListController implements Initializable, DataChangeListener {

// inje��o de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private FuncionarioService service;
	
	@FXML
 	private TableView<Funcionario> tableViewFuncionario;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Funcionario, Integer>  tableColumnCodigoFun;
 	
 	@FXML
 	private TableColumn<Funcionario, String> tableColumnNomeFun;

 	@FXML
 	private TableColumn<Funcionario, Integer> tableColumnDddFun;
 	
   	@FXML
 	private TableColumn<Funcionario, Integer> tableColumnTelefoneFun;
 	
  	@FXML
 	private TableColumn<Funcionario, String> tableColumnPixFun;
 	
   	@FXML
 	private TableColumn<Funcionario, String> tableColumnCargoFun;
 	
   	@FXML
   	private TableColumn<Funcionario, String> tableColumnSituacaoFun;
   	
 	@FXML
 	private TableColumn<Funcionario, Funcionario> tableColumnEDITA;

 	@FXML
 	private Button btNewFun;

 	@FXML
 	private Label labelUser;

 // auxiliar
  	String classe = "Funcion�rio ";
 	public String user = "usu�rio";		
	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Funcionario> obsList;
 
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
 	

 	@FXML
  	public void onBtNewFunAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 Funcionario obj = new Funcionario();
 		 createDialogForm(obj, "/sgo/gui/FuncionarioForm.fxml", parentStage);
   	}
 	
// injeta a dependencia com set (invers�o de controle de inje�ao)	
 	public void setFuncionarioService(FuncionarioService service) {
 		this.service = service;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padr�o para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnCodigoFun.setCellValueFactory(new PropertyValueFactory<>("codigoFun"));;
		tableColumnNomeFun.setCellValueFactory(new PropertyValueFactory<>("nomeFun"));
 		tableColumnDddFun.setCellValueFactory(new PropertyValueFactory<>("dddFun"));
 		tableColumnTelefoneFun.setCellValueFactory(new PropertyValueFactory<>("telefoneFun"));
  		tableColumnPixFun.setCellValueFactory(new PropertyValueFactory<>("pixFun"));
  		tableColumnCargoFun.setCellValueFactory(new PropertyValueFactory<>("cargoFun"));
  		tableColumnSituacaoFun.setCellValueFactory(new PropertyValueFactory<>("situacaoFun"));
  		// para tableview preencher o espa�o da tela scroolpane, referencia do stage		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFuncionario.prefHeightProperty().bind(stage.heightProperty());
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
		List<Funcionario> list = service.findAll();
  		obsList = FXCollections.observableArrayList(list);
  		tableViewFuncionario.setItems(obsList);
 		initEditButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (s� sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/
	private void createDialogForm(Funcionario obj, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			FuncionarioFormController controller = loader.getController();
			controller.user = user;
 // injetando passando parametro obj 			
			controller.setFuncionario(obj);
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setServices(new FuncionarioService(), new CargoService(), new SituacaoService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Funcion�rio                                             ");
 			dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
// quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
// travada enquanto n�o sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
 	} 

// *  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

/* * metodo p/ botao edit do frame
 * ele cria bot�o em cada linha 
 * o cell instancia e cria
*/  
	private void initEditButtons() {
		  tableColumnEDITA.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnEDITA.setCellFactory(param -> new TableCell<Funcionario, Funcionario>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(Funcionario obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
		 
		      setGraphic(button); 
		      button.setOnAction( 
		      event -> createDialogForm( 
		        obj, "/sgo/gui/FuncionarioForm.fxml",Utils.currentStage(event)));
 		    } 
		  }); 
		}	
}
