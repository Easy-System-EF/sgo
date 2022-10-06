package sgo.gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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
import sgo.model.entities.Grupo;
import sgo.model.entities.Material;
import sgo.model.services.GrupoService;
import sgo.model.services.MaterialService;
 
public class MaterialListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private MaterialService service;
	private GrupoService gruService;
	
	@FXML
 	private TableView<Material> tableViewMaterial;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Material, Integer>  tableColumnCodigoMat;
 	
 	@FXML
 	private TableColumn<Material, String> tableColumnNomeMat;

   	@FXML
 	private TableColumn<Material, Double> tableColumnSaldoMat;
 	
   	@FXML
 	private TableColumn<Material, Double> tableColumnVendaMat;
 	
   	@FXML
 	private TableColumn<Material, Double> tableColumnCmmMat;
 	
  	@FXML
 	private TableColumn<Material, Material> tableColumnEDITA;

 	@FXML
 	private TableColumn<Material, Material> tableColumnREMOVE ;

 	@FXML
 	private Button btNewMat;

	@FXML
	private Label labelUser;

	// Auxiliar
 	String classe = "Material";
	public String user = "usuário";		
 	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Material> obsList;
 
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */

 	@FXML
  	public void onBtNewMatAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 Material obj = new Material();
 		 createDialogForm(obj, "/sgo/gui/MaterialForm.fxml", parentStage);
   	}
 	
// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setMaterialService(MaterialService service) {
 		this.service = service;
 	}

 	public void setGrupoService(GrupoService gruService) {
 		this.gruService = gruService;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnCodigoMat.setCellValueFactory(new PropertyValueFactory<>("codigoMat"));
  		tableColumnNomeMat.setCellValueFactory(new PropertyValueFactory<>("nomeMat"));
		tableColumnSaldoMat.setCellValueFactory(new PropertyValueFactory<>("saldoMat"));
		Utils.formatTableColumnDouble(tableColumnSaldoMat, 2);
		tableColumnVendaMat.setCellValueFactory(new PropertyValueFactory<>("vendaMat"));
		Utils.formatTableColumnDouble(tableColumnVendaMat, 2);
 		tableColumnCmmMat.setCellValueFactory(new PropertyValueFactory<>("cmmMat"));
 		Utils.formatTableColumnDouble(tableColumnCmmMat, 2);
 		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewMaterial.prefHeightProperty().bind(stage.heightProperty());
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
		List<Material> list = service.findAll();
 		obsList = FXCollections.observableArrayList(list);
		tableViewMaterial.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (só sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/

	private void createDialogForm(Material obj, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			MaterialFormController controller = loader.getController();
			controller.user = user;
 // injetando passando parametro obj 			
			controller.setMaterial(obj);
 // injetando tb o forn service vindo da tela de formulario fornform
			controller.setMaterialService(new MaterialService());
			controller.setGrupoService(new GrupoService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Material                                             ");
 			dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
// quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
// travada enquanto não sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 
  	
//  atualiza minha lista dataChanged com dados novos 	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

/*
 * metodo p/ botao edit do frame
 * ele cria botão em cada linha 
 * o cell instancia e cria
*/
 	
	private void initEditButtons() {
		  tableColumnEDITA.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnEDITA.setCellFactory(param -> new TableCell<Material, Material>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(Material obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
		 
		      setGraphic(button); 
		      button.setOnAction( 
		      event -> createDialogForm( 
		        obj, "/sgo/gui/MaterialForm.fxml",Utils.currentStage(event)));
 		    } 
		  }); 
		}
	

	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<Material, Material>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(Material obj, boolean empty) { 
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

	private void removeEntity(Material obj) {
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
