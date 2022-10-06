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
import model.services.ParPeriodoService;
import sgcp.model.entityes.consulta.ParPeriodo;
import sgo.model.entities.Adiantamento;
import sgo.model.entities.Grupo;
import sgo.model.entities.Material;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Orcamento;
import sgo.model.entities.OrdemServico;
import sgo.model.entities.Receber;
import sgo.model.entities.ReposicaoVeiculo;
import sgo.model.entities.Veiculo;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.EmpresaService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.MaterialService;
import sgo.model.services.NotaFiscalService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.OrcamentoService;
import sgo.model.services.OrdemServicoService;
import sgo.model.services.ReceberService;
import sgo.model.services.ReposicaoVeiculoService;
import sgo.model.services.VeiculoService;
 
public class OrdemServicoListController implements Initializable, DataChangeListener {

// injeção de dependenia sem implementar a classe (instanciat)
// acoplamento forte - implementa via set
	private OrdemServicoService service;
	private ReceberService recService;
	private ReposicaoVeiculoService repService;
	private OrcamentoService orcService;
	private OrcVirtualService virService;
	private MaterialService matService;
	private VeiculoService veiService;
	private AdiantamentoService adiService;
 	
	@FXML
 	private TableView<OrdemServico> tableViewOrdemServico;
 	
// c/ entidade e coluna	
 	@FXML
 	private TableColumn<OrdemServico, Integer>  tableColumnNumeroOS;
 	
 	@FXML
 	private TableColumn<OrdemServico, Date>  tableColumnDataOS;
 	
 	@FXML
 	private TableColumn<OrdemServico, Integer>  tableColumnOrcamentoOS;

 	@FXML
 	private TableColumn<OrdemServico, String> tableColumnClienteOS;

   	@FXML
 	private TableColumn<OrdemServico, String> tableColumnPlacaOS;
 	 	
   	@FXML
 	private TableColumn<OrdemServico, Double> tableColumnValorOS;
 	
  	@FXML
 	private TableColumn<OrdemServico, OrdemServico> tableColumnEDITA;

 	@FXML
 	private TableColumn<OrdemServico, OrdemServico> tableColumnREMOVE ;

 	@FXML
 	private TableColumn<OrdemServico, OrdemServico> tableColumnList ;

 	@FXML
 	private Button btNewOS;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	int codOrd = 0;
 	int flagD = 0;
 	int flagR = 0;
 	String classe = "";
 	public static Integer numEmp = 0;
 	
// carrega aqui os fornecedores Updatetableview (metodo)
 	private ObservableList<OrdemServico> obsList;
 
 /* 
   * ActionEvent - referencia p/ o controle q receber o evento c/ acesso ao stage
  * com currentStage -
  * janela pai - parentstage
  * vamos abrir o forn form	
  */
	 Orcamento objOrc = new Orcamento();
	 OrcVirtual objVir = new OrcVirtual();
	 Material objMat = new Material();
	 Receber objRec = new Receber();
	 ReposicaoVeiculo objRep = new ReposicaoVeiculo();
	 Grupo objGru = new Grupo();
	 ParPeriodo objPer = new ParPeriodo();
	 Veiculo objVei = new Veiculo();
	 Adiantamento adiantamento = new Adiantamento();
	 
	 private void setOrcamento(Orcamento objOrc) {
		 this.objOrc = objOrc;
	 }
	 private void setOrcVirtual(OrcVirtual objVir) {
		 this.objVir = objVir;
	 }
	 private void setMaterial(Material objMat) {
		 this.objMat = objMat;
	 }
	 private void setReceber(Receber objRec) {
		 this.objRec = objRec;
	 }
	 private void setVeiculo(Veiculo objVei) {
		 this.objVei = objVei;
	 }

	@FXML
  	public void onBtNewOSAction(ActionEvent event) {
 		 Stage parentStage = Utils.currentStage(event);
 		 OrdemServico obj = new OrdemServico();
 		 createDialogForm(obj, objOrc, objVir, objMat, objRec, objRep, objGru, objPer, 
			 "/sgo/gui/OrdemServicoForm.fxml", parentStage);
   	}
 	
// injeta a dependencia com set (inversão de controle de injeçao)	
 	public void setOrdemServicoService(OrdemServicoService service) {
 		this.service = service;
 	}
 	public void setReceberService(ReceberService recService) {
 		this.recService = recService;
 	}
 	public void setReposicaoVeiculoService(ReposicaoVeiculoService repService) {
 		this.repService = repService;
 	}
 	public void setOrcamentoService(OrcamentoService orcService) {
 		this.orcService = orcService;
 	}
 	public void setOrcVirtualService(OrcVirtualService virService) {
 		this.virService = virService;
 	}
 	public void setMaterialService(MaterialService matService) {
 		this.matService = matService;
 	}
 	public void setVeiculoService(VeiculoService veiService) {
 		this.veiService = veiService;
 	}
 	public void setAdiantamentoService(AdiantamentoService adiService) {
 		this.adiService = adiService;
 	}

 // inicializar as colunas para iniciar nossa tabela initializeNodes
 	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}

// comportamento padrão para iniciar as colunas 	
 	private void initializeNodes() {
		tableColumnNumeroOS.setCellValueFactory(new PropertyValueFactory<>("numeroOS"));
		tableColumnDataOS.setCellValueFactory(new PropertyValueFactory<>("dataOS"));
		Utils.formatTableColumnDate(tableColumnDataOS, "dd/MM/yyyy");
		tableColumnOrcamentoOS.setCellValueFactory(new PropertyValueFactory<>("orcamentoOS"));
   		tableColumnClienteOS.setCellValueFactory(new PropertyValueFactory<>("clienteOS"));
		tableColumnPlacaOS.setCellValueFactory(new PropertyValueFactory<>("placaOS"));
 		tableColumnValorOS.setCellValueFactory(new PropertyValueFactory<>("valorOS"));
		Utils.formatTableColumnDouble(tableColumnValorOS, 2);
 		// para tableview preencher o espaço da tela scroolpane, referencia do stage		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrdemServico.prefHeightProperty().bind(stage.heightProperty());
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
			throw new IllegalStateException("Serviço OS está vazio");
 		}
 		labelUser.setText(user);
		List<OrdemServico> list = service.findAll();
  		obsList = FXCollections.observableArrayList(list);
  		tableViewOrdemServico.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		initListButtons();
	}

/* 	
* parametro informando qual stage criou essa janela de dialogo - stage parent
* nome da view - absolutename
* carregando uma janela de dialogo modal (só sai qdo sair dela, tem q instaciar um stage e dps a janela dialog
*/
 	private synchronized void createDialogForm(OrdemServico obj, Orcamento objOrc, 
 			OrcVirtual objVir, Material objMat, Receber objRec, ReposicaoVeiculo objRep, 
 			Grupo objGru, ParPeriodo objPer,
 			String absoluteName, Stage parentStage) {
 		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada ListForm			
			OrdemServicoFormController controller = loader.getController();
			controller.user = user;
// injetando passando parametro obj 		
// injetando serviços vindo da tela de formulario form
			controller.setOrdemServico(obj);
			controller.setOrcamento(objOrc);
			controller.setMaterial(objMat);
			controller.setPeriodo(objPer);
			controller.setServices(new OrdemServicoService(), 
					new OrcamentoService(), new OrcVirtualService(), new MaterialService(), 
					new ReceberService(), new ReposicaoVeiculoService(), new ParPeriodoService(),
					new VeiculoService(), new FuncionarioService(), new AdiantamentoService(),
					new NotaFiscalService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (FormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Ordem de Serviço                                      ");
 			dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
// quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
// travada enquanto não sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
 		catch (ParseException p) {
 			p.printStackTrace();
 		}
 	} 
 	
 // Imprimir	
  	private void createDialogImprime(OrdemServico obj, String absoluteName, Stage parentStage) {
  			try {
  	 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
  				Pane pane = loader.load();
  				classe = "OrdemServiço";
  	 			OrdemServicoImprimeController controller = loader.getController();
  	 			controller.setOrdemServico(obj);
  	 			controller.setOSImprime(new OrdemServicoService(),
  	 									new OrcamentoService(), 
  	 									new OrcVirtualService(),
  	 									new ReceberService(),
  	 									new EmpresaService());
  	 			controller.codOs = obj.getNumeroOS();
  	 			controller.numEmp = numEmp;
  	   			Stage dialogStage = new Stage();
  	 			dialogStage.setTitle("Lista Ordem de Serviço                                             ");
  	 			dialogStage.setScene(new Scene(pane));
  	 			dialogStage.setResizable(false);
  	  			dialogStage.initOwner(parentStage);
  	 			dialogStage.initModality(Modality.WINDOW_MODAL);
  				dialogStage.showAndWait();
  			}
  			catch (IOException e) {
  				e.printStackTrace();
  				Alerts.showAlert("IO Exception", classe + "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
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

/*
 * metodo p/ botao edit do frame
 * ele cria botão em cada linha 
 * o cell instancia e cria
*/	
	private void initEditButtons() {
		  tableColumnEDITA.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnEDITA.setCellFactory(param -> new TableCell<OrdemServico, OrdemServico>() { 
		    private final Button button = new Button("edita"); 
		    @Override 
		    protected void updateItem(OrdemServico obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      } 
		      setGraphic(button); 
		      button.setOnAction( 
						event -> createDialogForm(obj, objOrc, objVir, objMat, objRec, objRep, objGru, objPer, 
								"/sgo/gui/OrdemServicoForm.fxml", Utils.currentStage(event)));
			}
		});
	}
		  
	private void initRemoveButtons() {		
		  tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnREMOVE.setCellFactory(param -> new TableCell<OrdemServico, OrdemServico>() { 
		        private final Button button = new Button("exclui"); 
		 
		        @Override 
		        protected void updateItem(OrdemServico obj, boolean empty) { 
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

	private void removeEntity(OrdemServico obj) {
		conferePagamento(flagD, obj);
		if (flagD == 0) {
			Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
			if (result.get() == ButtonType.OK) {
				if (service == null) {
					throw new IllegalStateException("Serviço está vazio");
				}
				try {
					if (flagD == 0) {
						classe = "Receber";
						Receber rec = new Receber();
						setReceberService(new ReceberService());
						rec.setOsRec(obj.getNumeroOS());
						recService.removeOS(rec);
						classe = "Reposição";
						ReposicaoVeiculo rep = new ReposicaoVeiculo();
						setReposicaoVeiculoService(new ReposicaoVeiculoService());
						rep.setOsRep(obj.getNumeroOS());
						repService.remove(rep);
						updateOsOrcamento(obj);
						updateEstoqueMaterial(obj);
						updateAdiantamento(obj);						
						classe = "Ordem de serviço";
						service.remove(obj);
						updateKmVeiculo(obj);
						updateTableView();
					}	
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

	private void updateAdiantamento(OrdemServico obj) {
		adiantamento = adiService.findByOs(obj.getNumeroOS());
		adiService.remove(adiantamento.getNumeroAdi());
	}

	private void updateKmVeiculo(OrdemServico obj) {
		classe = "Veiculo";
		setVeiculo(objVei);
		setVeiculoService(new VeiculoService());
		objOrc = orcService.findById(obj.getOrcamentoOS());		
		objVei = veiService.findByPlaca(obj.getPlacaOS());
		objVei.setKmInicialVei(objOrc.getKmInicialOrc());
		objVei.setKmFinalVei(objOrc.getKmFinalOrc());
		veiService.saveOrUpdate(objVei);
	}
	private void updateEstoqueMaterial(OrdemServico obj) {
		setMaterial(objMat);
		setMaterialService(new MaterialService());
		setOrcVirtual(objVir);
		setOrcVirtualService(new OrcVirtualService());
		List<OrcVirtual> vir = new ArrayList<>();
		classe = "Virtual";
		vir = virService.findAll();
		for (OrcVirtual v : vir) {
			 if (v.getNumeroOrcVir() == obj.getOrcamentoOS()) {
				 classe = "Material"; 
				 objMat = matService.findById(v.getMaterial().getCodigoMat());
				 objMat.setSaidaMat(objMat.getSaidaMat() - v.getQuantidadeMatVir());
				 matService.saveOrUpdate(objMat);
			 }
		}		
	}
	
	private void updateOsOrcamento(OrdemServico obj) {
		classe = "Orçamento";
		setOrcamento(objOrc);
		setOrcamentoService(new OrcamentoService());
		objOrc = orcService.findById(obj.getOrcamentoOS());
		objOrc.setOsOrc(0);
		orcService.saveOrUpdate(objOrc);
	}

	private Integer conferePagamento(Integer flag, OrdemServico obj) {
		flagD = 0;
		classe = "Receber";
		setReceber(objRec);
		setReceberService(new ReceberService());
		List<Receber> list = new ArrayList<>(); 		
		list = recService.findByAllOs(obj.getNumeroOS());
		if (list.size() > 0) {
			List<Receber> st = list.stream()
				.filter(p -> p.getOsRec() == (obj.getNumeroOS()))
				.filter(p -> p.getDataPagamentoRec().after(p.getDataOsRec()))
				.collect(Collectors.toList());	
			if (st.size() > 0) {
				Alerts.showAlert("Erro!!! ", "Exclusão inválida " + classe, "Existe(m) parcela(s) paga(s) para OS !!!", AlertType.ERROR);
				flagD = 1;
			}
		}	
		return flagD;
	}	

	private void initListButtons() {
		  tableColumnList.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnList.setCellFactory(param -> new TableCell<OrdemServico, OrdemServico>() { 
		    private final Button button = new Button("lista");	    

		    @Override 
		    protected void updateItem(OrdemServico obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		      if (obj == null) { 
		        setGraphic(null); 
		        return; 
		      }
		    
		      setGraphic(button); 
		      button.setOnAction( 
		    		  event -> createDialogImprime(obj, "/sgo/gui/OrdemServicoImprime.fxml",Utils.currentStage(event)));
		    }
		 });  
	}
 }
