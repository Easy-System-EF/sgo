package sgo.gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
import sgo.model.entities.Adiantamento;
import sgo.model.entities.Balcao;
import sgo.model.entities.Empresa;
import sgo.model.entities.Material;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Receber;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.BalcaoService;
import sgo.model.services.EmpresaService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.MaterialService;
import sgo.model.services.NotaFiscalService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.ReceberService;
 
public class BalcaoListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private BalcaoService service;
	private EmpresaService empService;
	private MaterialService matService;
	private OrcVirtualService virService;
	private ReceberService recService;
	private AdiantamentoService adiService;

	@FXML
 	private TableView<Balcao> tableViewBalcao;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<Balcao, Integer>  tableColumnNumeroBal;
 	
 	@FXML
 	private TableColumn<Balcao, Date>  tableColumnDataBal;
 	
   	@FXML
 	private TableColumn<Balcao, String> tableColumnFuncionarioBal;
 	
   	@FXML
 	private TableColumn<Balcao, Double> tableColumnTotalBal;
 	
 	@FXML
 	private TableColumn<Balcao, Balcao> tableColumnREMOVE ;

 	@FXML
 	private TableColumn<Balcao, Balcao> tableColumnList ;

 	@FXML
 	private Button btNewBal;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	public static Integer numEmp = 0;
 	String classe = "Balcão ";
 	int flagD = 0; 	
 	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<Balcao> obsList;

 /* 
   * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
 	OrcVirtual objVir = new OrcVirtual();
 	Material objMat = new Material();
	Receber rec = new Receber();
 	
	@FXML
  	public void onBtNewBalAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
 		 Balcao obj = new Balcao();
 		 createDialogForm(obj, objVir, "/sgo/gui/BalcaoForm.fxml", parentStage);
 		 initializeNodes();
   	}
 	
// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setServices(BalcaoService service,
 							MaterialService matService,
 							OrcVirtualService virService,
 							ReceberService recService,
 							AdiantamentoService adiService) {
 		this.service = service;
 		this.matService = matService;
 		this.virService = virService;
 		this.recService = recService;
 		this.adiService = adiService;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnNumeroBal.setCellValueFactory(new PropertyValueFactory<>("numeroBal"));
		tableColumnDataBal.setCellValueFactory(new PropertyValueFactory<>("dataBal"));
		Utils.formatTableColumnDate(tableColumnDataBal, "dd/MM/yyyy");
		tableColumnFuncionarioBal.setCellValueFactory(new PropertyValueFactory<>("FuncionarioBal"));
 		tableColumnTotalBal.setCellValueFactory(new PropertyValueFactory<>("totalBal"));
		Utils.formatTableColumnDouble(tableColumnTotalBal, 2);
 		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		 tableViewBalcao.prefHeightProperty().bind(stage.heightProperty());
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
		List<Balcao> list = service.findAll();
 		obsList = FXCollections.observableArrayList(list);
		tableViewBalcao.setItems(obsList);
		notifyDataChangeListerners();
		initRemoveButtons();
		initListButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (só sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/
	private void createDialogForm(Balcao obj, OrcVirtual objVir,
						String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "Balcão ";
// referencia para o controlador = controlador da tela carregada fornListaForm			
			BalcaoFormController controller = loader.getController();
			controller.user = user;
 // injetando passando parametro obj 		
			controller.setBalcao(obj);
			controller.setVirtual(objVir);

 // injetando serviços vindo da tela de formulario fornform
			controller.setServices(new BalcaoService(),
									new OrcVirtualService(),
			   						new FuncionarioService(),
			   						new MaterialService(),
			   						new NotaFiscalService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateTableView();
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite Balcão                                             ");
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
			Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 

// Imprimir	
 	private void createDialogImprime(Balcao obj, Empresa emp, Integer numBal,   
 					String absoluteName, Stage parentStage) {
 			try {
 	 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
 				Pane pane = loader.load();
 				classe = "Balcão Impr";
 	 			BalcaoImprimeController controller = loader.getController();
 	 			controller.setBalcao(obj);
 	 			controller.numEmp = numEmp;
 	 			controller.numBal = numBal;
 	 			controller.setBalcaoService(new BalcaoService(), new EmpresaService());
 	 			controller.setOrcVirtualService(new OrcVirtualService());
 	   			Stage dialogStage = new Stage();
 	 			dialogStage.setTitle("Lista Balcão                                             ");
 	 			dialogStage.setScene(new Scene(pane));
 	 			dialogStage.setResizable(false);
 	  			dialogStage.initOwner(parentStage);
 	 			dialogStage.initModality(Modality.WINDOW_MODAL);
 				dialogStage.showAndWait();
 			}
 			catch (IOException e) {
 				e.printStackTrace();
 				Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
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

	private void initListButtons() {
		  tableColumnList.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnList.setCellFactory(param -> new TableCell<Balcao, Balcao>() { 
		  private final Button button = new Button("lista");
		 
		  @Override 
		  protected void updateItem(Balcao obj, boolean empty) { 
		     super.updateItem(obj, empty); 
		     if (obj == null) { 
		    	  setGraphic(null); 
		    	  return; 
		     }
		     Empresa emp = new Empresa();
		     setGraphic(button); 
		     button.setOnAction( 
		    	  event -> createDialogImprime(obj, emp, obj.getNumeroBal(), 
		    			  "/sgo/gui/BalcaoImprime.fxml", Utils.currentStage(event)));
		  }
		  }); 
		}
	
	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<Balcao, Balcao>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(Balcao obj, boolean empty) { 
		            super.updateItem(obj, empty); 
		 
		            if (obj == null) { 
		                setGraphic(null); 
		                return; 
		            } 
		 
		            setGraphic(button); 
		            button.setOnAction(eivent -> removeEntity(obj)); 
		        } 
		    });
 		} 

	private void removeEntity(Balcao obj) {
		if (obj.getTotalBal() > 0) {
			conferePagamento(obj);
		}	
		if (flagD == 0) {
			Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
			if (result.get() == ButtonType.OK) {
				if (service == null) {
					throw new IllegalStateException("Serviço está vazio");
				}
				try {
					if (flagD == 0 && obj.getTotalBal() > 0) {
						classe = "Receber";
						rec.setOsRec(obj.getNumeroBal());
						recService.removeOS(rec);
						updateEstoqueMaterial(obj);
						updateAdiantamento(obj);						
						classe = "Balcão ";
					}	
					service.remove(obj);
					updateTableView();
				}
				catch (DbIntegrityException e) {
					Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
				}
			}	
		}
	}	

		private Integer conferePagamento(Balcao obj) {
			flagD = 0;
			classe = "Receber";
			List<Receber> list = recService.findByAllOs(obj.getNumeroBal());
			if (list.size() > 0) {
				List<Receber> st = list.stream()
					.filter(p -> p.getOsRec() == (obj.getNumeroBal()))
					.filter(p -> p.getDataPagamentoRec().after(p.getDataOsRec()))
					.collect(Collectors.toList());	
				if (st.size() > 0) {
					Alerts.showAlert("Erro!!! ", "Exclusão inválida " + classe, "Parcela paga !!!", AlertType.ERROR);
					flagD = 1;
				}
			}	
			return flagD;
		}	

		
	private void updateEstoqueMaterial(Balcao obj) {
		Material mat = new Material();
		List<OrcVirtual> vir = new ArrayList<>();
		classe = "Virtual ";
		vir = virService.findAll();
		for (OrcVirtual v : vir) {
			 if (v.getNumeroBalVir() == obj.getNumeroBal()) {
				 classe = "Material "; 
				 mat = matService.findById(v.getMaterial().getCodigoMat());
				 mat.setSaidaMat(objMat.getSaidaMat() - v.getQuantidadeMatVir());
				 matService.saveOrUpdate(objMat);
			 }
		}		
	}

	private void updateAdiantamento(Balcao obj) {
		Adiantamento adianto = new Adiantamento();
		adianto = adiService.findByBal(obj.getNumeroBal());
		adiService.remove(adianto.getNumeroAdi());
	}
}