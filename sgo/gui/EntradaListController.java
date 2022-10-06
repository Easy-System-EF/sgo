package sgo.gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
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
import model.services.FornecedorService;
import sgcp.model.entityes.Fornecedor;
import sgo.model.entities.Entrada;
import sgo.model.entities.Grupo;
import sgo.model.entities.Material;
import sgo.model.services.EntradaService;
import sgo.model.services.GrupoService;
import sgo.model.services.MaterialService;
 
public class EntradaListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private EntradaService service;
	private MaterialService matService;
	private FornecedorService fornService;
	private GrupoService gruService;
	
	@FXML
 	private TableView<Entrada> tableViewEntrada;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Entrada, Date> tableColumnDataEnt; 
 	
 	@FXML
 	private TableColumn<Entrada, Integer>  tableColumnNumeroEnt;

 	@FXML
 	private TableColumn<Entrada, String>  tableColumnNomeFornEnt;

 	@FXML
 	private TableColumn<Entrada, String>  tableColumnNomeMatEnt;

   	@FXML
 	private TableColumn<Entrada, Double> tableColumnQtdMatEnt;
 	
   	@FXML
 	private TableColumn<Entrada, Double> tableColumnVlrMatEnt;
 	
  	@FXML
 	private TableColumn<Entrada, Entrada> tableColumnEDITA;

 	@FXML
 	private TableColumn<Entrada, Entrada> tableColumnREMOVE ;

 	@FXML
 	private Button btNewEnt;
 	
 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "Entrada ";
 	
// carrega aqui Updatetableview (metodo)
 	private ObservableList<Entrada> obsList;
  
 /* 
  * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
		Material objMat = new Material();
		Fornecedor objForn = new Fornecedor();
		Grupo objGru = new Grupo();

 	@FXML
  	public void onBtNewEntAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		Entrada obj = new Entrada();
 		createDialogForm(objGru, obj, "/sgo/gui/EntradaForm.fxml", parentStage);
   	}
 	
// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setMaterialService(MaterialService matService) {
 		this.matService = matService;
 	}

 	public void setFornecedorService(FornecedorService fornService) {
 		this.fornService = fornService;
 	}

 	public void setEntradaService(EntradaService service) {
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
		tableColumnNumeroEnt.setCellValueFactory(new PropertyValueFactory<>("numeroEnt"));
		tableColumnDataEnt.setCellValueFactory(new PropertyValueFactory<>("dataEnt"));
		Utils.formatTableColumnDate(tableColumnDataEnt, "dd/MM/yyyy");
		tableColumnNomeFornEnt.setCellValueFactory(new PropertyValueFactory<>("nomeFornEnt"));
		tableColumnNomeMatEnt.setCellValueFactory(new PropertyValueFactory<>("nomeMatEnt"));
 		tableColumnQtdMatEnt.setCellValueFactory(new PropertyValueFactory<>("quantidadeMatEnt"));
		Utils.formatTableColumnDouble(tableColumnQtdMatEnt, 2);
		tableColumnVlrMatEnt.setCellValueFactory(new PropertyValueFactory<>("valorMatEnt"));
		Utils.formatTableColumnDouble(tableColumnVlrMatEnt, 2);
  		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewEntrada.prefHeightProperty().bind(stage.heightProperty());
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
// aq vou buscar tds Id e madar pronto  		
 		List<Grupo> listGru = gruService.findAll();
 		listGru.add(objGru);
 		List<Material> listMat = matService.findAll();
 		List<Fornecedor> listForn = fornService.findAll();
 		List<Entrada> list = service.findAll(listGru);
   		obsList = FXCollections.observableArrayList(list);
 		tableViewEntrada.setItems(obsList);
 		initEditButtons();
		initRemoveButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (só sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/

	private void createDialogForm(Grupo objGru, 
			Entrada obj, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			EntradaFormController controller = loader.getController();
			controller.user = user;
 // injetando passando parametro obj 			
			controller.setEntrada(obj);
 
 // injetando tb o forn service vindo da tela de formulario fornform
			controller.setMaterialService(new MaterialService());
			controller.setFornecedorService(new FornecedorService());
			controller.setEntradaService(new EntradaService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Entrada                                             ");
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
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela Entrada", e.getMessage(), AlertType.ERROR);
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
		  tableColumnEDITA.setCellFactory(param -> new TableCell<Entrada, Entrada>() { 
		    private final Button button = new Button("edita"); 
		 
		    @Override 
		    protected void updateItem(Entrada obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
		 
		      setGraphic(button); 
		      button.setOnAction( 
		      event -> createDialogForm( 
		        objGru, obj, "/sgo/gui/EntradaForm.fxml",Utils.currentStage(event)));
 		    } 
		  }); 
		}
 
	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<Entrada, Entrada>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(Entrada obj, boolean empty) { 
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

	private void removeEntity(Entrada obj) {
		classe = "Entrada";
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serviço está vazio");
			}
			try {
				devolveMaterial(obj);
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	private void devolveMaterial(Entrada obj) {
		objMat = matService.findById(obj.getMat().getCodigoMat());
		objMat.setEntradaMat(objMat.getEntradaMat() - obj.getQuantidadeMatEnt());
 		matService.saveOrUpdate(objMat);
	}
 }
