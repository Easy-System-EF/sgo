package sgo.gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import application.Main;
import db.DbException;
import db.DbIntegrityException;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Mascaras;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;
import model.services.ParPeriodoService;
import sgcp.model.entityes.consulta.ParPeriodo;
import sgo.model.entities.Adiantamento;
import sgo.model.entities.Balcao;
import sgo.model.entities.Funcionario;
import sgo.model.entities.Material;
import sgo.model.entities.NotaFiscal;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Receber;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.BalcaoService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.MaterialService;
import sgo.model.services.NotaFiscalService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.ReceberService;

/*
 * Monta formaulário do balcão e virtual
 */

public class BalcaoFormController implements Initializable, DataChangeListener  {

	private Balcao entity;
	private OrcVirtual virtual;
	private Material mat = new Material();
	private ParPeriodo periodo;
	private NotaFiscal nota = new NotaFiscal();
	Calendar cal = Calendar.getInstance();

	/*
	 * dependencia service com metodo set
	 */
	private BalcaoService service;
	private OrcVirtualService virService;
	private FuncionarioService funService;
	private MaterialService matService;
	private ParPeriodoService perService;
	private ReceberService recService;
	private AdiantamentoService adiService;
	private NotaFiscalService nfService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textNumeroBal;

	@FXML
	private DatePicker dpDataBal;

 	@FXML
 	private ComboBox<Funcionario> comboBoxFunBal;
 	
 	@FXML
 	private RadioButton rbPagamentoDinheiro;

 	@FXML
 	private RadioButton rbPagamentoPix;

 	@FXML
 	private RadioButton rbPagamentoDebito;

 	@FXML
 	private RadioButton rbPagamentoCC;

 	@FXML
 	private DatePicker dpData1oBal;
 	
 	@FXML
 	private TextField textNnfBal;
 	
	@FXML
	private TextField textDescontoBal;

	@FXML
	private Label labelTotalBal;

 	@FXML
 	private TextField textObservacaoBal;
 	
	@FXML
	private TableView<OrcVirtual> tableViewOrcVirtual;

	@FXML
	private TableColumn<OrcVirtual, String> tableColumnNomeMatVir;

	@FXML
	private TableColumn<OrcVirtual, Double> tableColumnQtdMatVir;

	@FXML
	private TableColumn<OrcVirtual, Double> tableColumnPrecoMatVir;

	@FXML
	private TableColumn<OrcVirtual, Double> tableColumnTotalMatVir;

	@FXML
	private TableColumn<OrcVirtual, OrcVirtual> tableColumnEditaVir;

	@FXML
	private TableColumn<OrcVirtual, OrcVirtual> tableColumnRemoveVir;

	@FXML
	private Button btSaveBal;

	@FXML
	private Button btCancelBal;

	@FXML
	private Label labelErrorDataBal;

	@FXML
	private Label labelErrorPagamentoBal;

	@FXML
	private Label labelErrorDataPrimeiroBal;

	@FXML
	private Label labelErrorNFBal;

	@FXML
	private Button btNewvVir;

 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "Balcão ";
	Double vlrAnt = 0.00;
	Integer numBal = null;
	int ultimaNF = 0;
// deetermina se mostra total atz ou grava	
	int flagGrw = 0;
// = 1 mostra alerts de balcao vazio	
	int flagSave = 0;
	int flagPg = 0;
	double maoObra = 0.00;
	
	private ObservableList<OrcVirtual> obsListVir;
 	private ObservableList<Funcionario> obsListFun;

	@FXML
	public void onBtNewVirAction(ActionEvent event) {
		if (numBal == null) {
			saveBal();
		} 
		if (entity.getNumeroBal() != null) {
			numBal = entity.getNumeroBal();
			Stage parentStage = Utils.currentStage(event);
// instanciando novo obj depto e injetando via
			OrcVirtual virtual = new OrcVirtual();
			createDialogForm(numBal, virtual, "/sgo/gui/OrcVirtualForm.fxml", parentStage);
		}
	}

	@FXML
	public void saveBal() {
		if (entity == null) {
			throw new IllegalStateException("Entidade balcão nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço balcão nulo");
		}
		try {
			ValidationException exception = new ValidationException("Validation exception");
			flagSave = 1;
			entity = getFormData();
			numBal = entity.getNumeroBal();
			classe = "Balcão ";
			service.saveOrUpdate(entity);
			throw exception;
		} 
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} 
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} 
		catch (ParseException p) {
			p.printStackTrace();
		}
	}

	public void setBalcao(Balcao entity) {
		this.entity = entity;
	}
	public void setVirtual(OrcVirtual virtual) {
		this.virtual = virtual;
	}

 	// * metodo set /p service
	public void setServices(BalcaoService service,
							OrcVirtualService virService,
							FuncionarioService funService,
							MaterialService matService,
							NotaFiscalService nfService) {
		this.service = service;
		this.virService = virService; 
 		this.funService = funService;
 		this.matService = matService;
 		this.nfService = nfService;
	}
	
	public void setBalcaoServices(ParPeriodoService perService,
									ReceberService recService,
									AdiantamentoService adiService) {
		this.perService = perService;
		this.recService = recService;
		this.adiService = adiService;
	}

	/*
	 * vamos instanciar um forn e salvar no bco de dados meu obj entity (lá em cima)
	 * vai receber uma chamada do getformdata metodo q busca dados do formulario
	 * convertidos getForm (string p/ int ou string) pegou no formulario e retornou
	 * (convertido) p/ jogar na variavel entity chamo o service na rotina saveupdate
	 * e mando entity vamos tst entity e service = null -> não foi injetado para
	 * fechar a janela, pego a referencia para janela atual (event) e close
	 * DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar
	 * uma lista qdo ela salvar obj com sucesso, é só notificar (juntar) recebe lá
	 * no listController
	 */ 
	@FXML
	public void onBtSaveBalAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade balcão nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço balcão nulo");
		}
		try {
			ValidationException exception = new ValidationException("Validation exception");
//flagSave = 2 não mosta alerts balcão vazio			
			flagSave = 2;
			entity = getFormData();
			List<OrcVirtual> virtual = virService.findByBalcao(numBal);
			entity.calculaTotalBal(virtual);
			maoObra = 0.0;
			flagGrw = 0;
			if (entity.getTotalBal() > 0) {
				if (entity.getTotalBal().equals(vlrAnt)) {
					flagGrw = 0;
				} else {
					flagGrw = 1;
				}
				classe = "Balcão";
				service.saveOrUpdate(entity);
				if (entity.getFuncionario().getCargo().getComissaoCargo() > 0) {
					for (OrcVirtual v : virtual) {
						mat = matService.findById(v.getMaterial().getCodigoMat());
						if (mat.getGrupo().getNomeGru() == "Mão de obra") {
							maoObra += mat.getVendaMat();
						}	
					}
				}
				setBalcaoServices(new ParPeriodoService(),
						new ReceberService(),
						new AdiantamentoService());
				if (flagGrw == 1) {
					vlrAnt = entity.getTotalBal();
					flagGrw = 0;
					String vlr = Mascaras.formataValor(entity.getTotalBal());
					labelTotalBal.setText(vlr);
					exception.addErros("confirma", "Confirma?");
					throw exception;
				} 
				if (flagGrw == 0) {
					updateMaterialBalcao(virtual); 
					if (maoObra > 0) {
						gravaComissaoBalcao(entity);; 
					}
					porPeriodo(); 
					gravaAReceberBalcao(entity);
				}	
			}
			notifyDataChangeListerners();
			Utils.currentStage(event).close();	
		}	
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} 
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} 
		catch (ParseException p) {
			p.printStackTrace();
		}
	}

	private void updateMaterialBalcao(List<OrcVirtual> virtual) {
		classe = "Material";
		ValidationException exception = new ValidationException("Validation exception");
		for (OrcVirtual v : virtual) {
			v.getMaterial().getCodigoMat();
				mat = matService.findById(v.getMaterial().getCodigoMat());
				if (mat.getGrupo().getNomeGru() == "Mão de Obra") {
					mat.setEntradaMat(v.getQuantidadeMatVir());
				}
				mat.setSaidaMat(v.getQuantidadeMatVir());
				mat.setCmmMat(mat.calculaCmm());
				matService.saveOrUpdate(mat);
		}	
	}	
	
	private void gravaComissaoBalcao(Balcao balcao) {
		classe = "Adiantamento ";
		Adiantamento adiantamento = new Adiantamento();
		Funcionario fun = funService.findById(balcao.getFuncionario().getCodigoFun());
		adiantamento.setDataAdi(balcao.getDataBal());
		adiantamento.setCodFunAdi(fun.getCodigoFun());
		adiantamento.setNomeFunAdi(fun.getNomeFun());
		adiantamento.setCargoAdi(fun.getCargoFun());
		adiantamento.setSituacaoAdi(fun.getSituacaoFun());
		adiantamento.setValorAdi(0.00);
		adiantamento.setComissaoAdi(0.0);
		cal.setTime(adiantamento.getDataAdi());
		adiantamento.setMesAdi(cal.get(Calendar.MONTH) + 1);
		adiantamento.setAnoAdi(cal.get(Calendar.YEAR));
		adiantamento.setBalcaoAdi(balcao.getNumeroBal());
		adiantamento.setOsAdi(0);
		adiantamento.setTipoAdi("B");
		adiantamento.setFuncionario(fun);
		adiantamento.setCargo(fun.getCargo());
		adiantamento.setSituacao(fun.getSituacao());
		adiantamento.calculaComissao(maoObra);
		adiService.saveOrUpdate(adiantamento);		
	}

	public ParPeriodo porPeriodo() {
		try {
			classe = "Período ";
			SimpleDateFormat sdfi = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dti = sdfi.parse("01/01/2001 00:00:00");
			Date dtf = sdff.parse("31/01/2041 00:00:00");

			Integer cod = 1;
			periodo = perService.findById(cod);
			periodo.setIdPeriodo(1);
			periodo.setDtiPeriodo(dti);
			periodo.setDtfPeriodo(dtf);
			perService.update(periodo);
		} 
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException e) {
			Alerts.showAlert("ParseException ", "Erro Data Periodo ", e.getMessage(), AlertType.ERROR);
		}
		return periodo;
	}
	
	private void gravaAReceberBalcao(Balcao entity1) {
		try {
			classe = "Receber ";
			Receber rec = new Receber();
			rec.setFuncionarioRec(entity1.getFuncionario().getCodigoFun());
			rec.setClienteRec(0);
			rec.setNomeClienteRec("Balcão");
			rec.setOsRec(entity1.getNumeroBal());
			rec.setDataOsRec(entity1.getDataBal());
			rec.setPlacaRec("Balcão");
			rec.setParcelaRec(1);
			rec.setPeriodo(periodo);
			if (entity1.getPagamentoBal() == 1) {
				rec.setFormaPagamentoRec("Dinheiro");
			} else {
				if (entity1.getPagamentoBal() == 2) {
					rec.setFormaPagamentoRec("Pix");
				} else {
					if (entity1.getPagamentoBal() == 3) {
						rec.setFormaPagamentoRec("Débito");
					} else {
						if (entity1.getPagamentoBal() == 4) {
							rec.setFormaPagamentoRec("CC");					
						}
					}	
				}
			}	
			rec.setDataPagamentoRec(entity1.getDataPrimeiroPagamentoBal());
			rec.setDataVencimentoRec(entity1.getDataPrimeiroPagamentoBal());
			rec.setValorRec(entity1.getTotalBal());
			rec.setNumeroRec(null);
			recService.insert(rec);
		}	
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

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

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se não for atz tb verificamos se cpos
	 * obrigatórios estão preenchidos, para informar erro(s) para cpos string não
	 * precisa tryParse
	 */
	private Balcao getFormData() throws ParseException {
		Balcao obj = new Balcao();
		// instanciando uma exceção, mas não lançado - validation exc....
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null
		if (numBal != null) {
			obj.setNumeroBal(numBal);
		}
		else {
			obj.setNumeroBal(Utils.tryParseToInt(textNumeroBal.getText()));
		}	
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro

		if (dpDataBal.getValue() != null) {
			Instant instant = Instant.from(dpDataBal.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataBal(Date.from(instant));
		}
		else {	
			if (obj.getDataBal() == null) {
				exception.addErros("data", "Data é obrigatória");
			}
		}

		obj.setFuncionario(comboBoxFunBal.getValue());
		obj.setFuncionarioBal(comboBoxFunBal.getValue().getNomeFun());

		flagPg = 0;
 		if (rbPagamentoDinheiro.isSelected()) {
 		 	obj.setPagamentoBal(1);
 		 	flagPg += 1;
 		}	
 		if (rbPagamentoPix.isSelected()) {
 		 	obj.setPagamentoBal(2);			
 		 	flagPg += 1;
 		}
 		if (rbPagamentoDebito.isSelected()) {
 		 	obj.setPagamentoBal(3);			
 		 	flagPg += 1;
 		}
		if (rbPagamentoCC.isSelected()) {
			obj.setPagamentoBal(4);
 		 	flagPg += 1;
		}
 		if (flagPg == 0) { 
			exception.addErros("pagamento", "Pagamento obrigatório");
		} 		
 		if (flagPg > 1) { 
			exception.addErros("pagamento", "Só pode uma opção");
		}
 		
		if (dpData1oBal.getValue() != null) { 
			Instant instant = Instant.from(dpData1oBal.getValue().atStartOfDay(ZoneId.systemDefault())); 
			obj.setDataPrimeiroPagamentoBal(Date.from(instant));		
		}
		
		if (obj.getDataPrimeiroPagamentoBal() == null) { 		
			exception.addErros("data1o", "Data é obrigatória");
		}	
 
		if (obj.getDataPrimeiroPagamentoBal() != null) {
			if (obj.getDataPrimeiroPagamentoBal().before(obj.getDataBal())) {
				exception.addErros("data1o", "Data menor que Data da Bal");
			}	
 		}
 		
 		if (textNnfBal.getText() == null || textNnfBal.getText().trim().contentEquals("")) {
 			obj.setNnfBal(0);
 		}
 		else {
 			obj.setNnfBal(Utils.tryParseToInt(textNnfBal.getText()));
 		}

 		if (ultimaNF == 0 && obj.getNnfBal() > 0 && numBal != null) {
			List<NotaFiscal> nf = nfService.findAll();
			if (nf != null) {
				for (NotaFiscal n : nf) {
					if (n.getNumeroNF() > ultimaNF) {
						ultimaNF = n.getNumeroNF();
					}
				}	
				if (obj.getNnfBal() <= ultimaNF) {
					exception.addErros("nf", "NF menor/igual a última " + ultimaNF);
				} 
			}	
			nota.setCodigoNF(null);
			nota.setNumeroNF(obj.getNnfBal());
			nota.setBalcaoNF(numBal);
			nota.setOsNF(0);
			nfService.saveOrUpdate(nota);
 		}	
 		 		
		obj.setObservacaoBal(textObservacaoBal.getText());
			
		if (textDescontoBal.getText() == null || textDescontoBal.getText().trim().contentEquals("")) {
			obj.setDescontoBal(0.00);
		}	else {
				obj.setDescontoBal(Utils.tryParseToDouble(textDescontoBal.getText().replace(",", ".")));
			}

		if (obj.getTotalBal() == null) {
			obj.setTotalBal(0.00);
		}
		
		if (obj.getDataBal() != null) {
			cal.setTime(obj.getDataBal());
			obj.setMesBal(cal.get(Calendar.MONTH) + 1);
			obj.setAnoBal(cal.get(Calendar.YEAR));
		}
		
		// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			if (flagSave == 1) {
				Alerts.showAlert("Atenção", null, "O balcão tem que estar preenchido", AlertType.WARNING);	
			}
			throw exception;
		}
		return obj;
	}

	public void updateTableView() {
		if (virService == null) {
			throw new IllegalStateException("Serviço virtual está vazio");
		}
		labelUser.setText(user);
		List<OrcVirtual> listVir = new ArrayList<>();
		if (entity.getNumeroBal() != null) {
			listVir = virService.findByBalcao(entity.getNumeroBal());
			obsListVir = FXCollections.observableArrayList(listVir);
			tableViewOrcVirtual.setItems(obsListVir);
			initEditButtons();
			initRemoveButtons();
		}	
	}	

	// msm processo save p/ fechar
	@FXML
	public void onBtCancelBalAction(ActionEvent event) {
		if (numBal != null) {
			service.remove(entity);
		}
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textNumeroBal);
 		Utils.formatDatePicker(dpDataBal, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(textDescontoBal);
		initializeNodes();
		initializeComboBoxFuncionario();
	}

	// comportamento padrão para iniciar as colunas
	private void initializeNodes() {
		tableColumnNomeMatVir.setCellValueFactory(new PropertyValueFactory<>("NomeMatVir"));
		tableColumnQtdMatVir.setCellValueFactory(new PropertyValueFactory<>("QuantidadeMatVir"));
		Utils.formatTableColumnDouble(tableColumnQtdMatVir, 2);
		tableColumnPrecoMatVir.setCellValueFactory(new PropertyValueFactory<>("PrecoMatVir"));
		Utils.formatTableColumnDouble(tableColumnPrecoMatVir, 2);
		tableColumnTotalMatVir.setCellValueFactory(new PropertyValueFactory<>("TotalMatVir"));
		Utils.formatTableColumnDouble(tableColumnTotalMatVir, 2);
		// para tableview preencher o espaço da tela scroolpane, referencia do stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrcVirtual.prefHeightProperty().bind(stage.heightProperty());
	}

	private void initializeComboBoxFuncionario() {
		Callback<ListView<Funcionario>, ListCell<Funcionario>> factory = lv -> new ListCell<Funcionario>() {
			@Override
			protected void updateItem(Funcionario item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeFun());
			}
		};

		comboBoxFunBal.setCellFactory(factory);
		comboBoxFunBal.setButtonCell(factory.call(null));
	}

	private void createDialogForm(Integer numBal, OrcVirtual virtual, 
						String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			classe = "OrcVirtual";
//referencia para o controlador = controlador da tela carregada fornListaForm			
			OrcVirtualFormController controller = loader.getController();
			controller.user = user;
// injetando passando parametro obj 			
			controller.setOrcVirtual(virtual);
			controller.numBal = numBal;
			controller.numOrc = 0;

// injetando serviços vindo da tela de formulario fornform
			controller.setOrcVirtualService(new OrcVirtualService());
			controller.setBalcaoService(new BalcaoService());
			controller.setMaterialService(new MaterialService());
			controller.loadAssociatedObjects();
//inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListener(this);
//carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite Material                                             ");
			dialogStage.setScene(new Scene(pane));
//pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
//quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
//travada enquanto não sair da tela
 			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela " + classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void initEditButtons() {
		tableColumnEditaVir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditaVir.setCellFactory(param -> new TableCell<OrcVirtual, OrcVirtual>() {
			private final Button button = new Button("edita");

			@Override
			protected void updateItem(OrcVirtual virtual, boolean empty) {
				super.updateItem(virtual, empty);

				if (virtual == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(numBal, virtual, 
						"/sgo/gui/OrcVirtualForm.fxml", Utils.currentStage(event)));
			}
	 	});
	}

	private void initRemoveButtons() {
		tableColumnRemoveVir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemoveVir.setCellFactory(param -> new TableCell<OrcVirtual, OrcVirtual>() {
			private final Button button = new Button("exclui");

			@Override
			protected void updateItem(OrcVirtual virtual, boolean empty) {
				super.updateItem(virtual, empty);

				if (virtual == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(virtual));
			}
		});
	}

	private void removeEntity(OrcVirtual virtual) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
		if (result.get() == ButtonType.OK) {
			if (virService == null) {
				throw new IllegalStateException("Serviço virtual está vazio");
			}
			try {
				acertaBal(virtual);
				classe = "OrcVirtual";
				virService.remove(virtual);
				updateFormData();
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Erro removendo objeto", classe, e.getMessage(), AlertType.ERROR);
			} catch (ParseException p) {
				p.getStackTrace();
			}
		}
	}

	private void acertaBal(OrcVirtual obj) {
			Balcao bal = service.findById(obj.getNumeroBalVir());
			bal.setTotalBal(0.0);
			List<OrcVirtual> listVir = virService.findByOrcto(bal.getNumeroBal());
			bal.setTotalBal(bal.calculaTotalBal(listVir));
  			service.saveOrUpdate(bal);
	}

	/*
	 * transforma string da tela p/ o tipo no bco de dados
	 */
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade balcão esta nula");
		}
		//  string value of p/ casting int p/ string
			textNumeroBal.setText(String.valueOf(entity.getNumeroBal()));
	// se for uma inclusao, vai posicionar no 1o depto//tipo (First)
			
		if (entity.getDataBal() != null) {
			entity.setDataBal(new Date());
		}
			
		if (entity.getDataBal() != null) {
			dpDataBal.setValue(LocalDate.ofInstant(entity.getDataBal().toInstant(), ZoneId.systemDefault()));
		}

		if (entity.getFuncionario() == null) {
			comboBoxFunBal.getSelectionModel().selectFirst();
		} else {
			comboBoxFunBal.setValue(entity.getFuncionario());
		}

  	 	rbPagamentoDinheiro.setSelected(false);
  	 	rbPagamentoPix.setSelected(false);
  	 	rbPagamentoDebito.setSelected(false);
  	 	rbPagamentoCC.setSelected(false);
  	 	if (entity.getPagamentoBal() != null) {
 			if (entity.getPagamentoBal() == 1) {
 				rbPagamentoDinheiro.setSelected(true);
 			}
 			else {
 				if (entity.getPagamentoBal() == 2) {
 					rbPagamentoPix.setSelected(true);
 				}
 				else {
 					if (entity.getPagamentoBal() == 3) {
 						rbPagamentoDebito.setSelected(true);
 					} else {
 						if (entity.getPagamentoBal() == 4) {
 							rbPagamentoCC.setSelected(true);
 						}
 					}
 				}
 			}	
 		}
 		if (entity.getDataPrimeiroPagamentoBal() == null) {
 	  	 	entity.setDataPrimeiroPagamentoBal(entity.getDataBal());
  	 	}

		if (entity.getDataPrimeiroPagamentoBal() != null) {
			entity.setDataPrimeiroPagamentoBal(new Date());
		}
			
		if (entity.getDataPrimeiroPagamentoBal() != null) {
			dpData1oBal.setValue(LocalDate.ofInstant(entity.getDataPrimeiroPagamentoBal().toInstant(), ZoneId.systemDefault()));
		}

		String vlr = "0.00";
		if (entity.getDescontoBal() == null) {
			entity.setDescontoBal(0.00);
		}
		if (entity.getTotalBal() == null) {
			entity.setTotalBal(0.00);
		} 
		if (entity.getNnfBal() == null) {
			entity.setNnfBal(0);
		}

		textNnfBal.setText(String.valueOf(entity.getNnfBal()));
 		textObservacaoBal.setText(entity.getObservacaoBal());
 		
		vlr = Mascaras.formataValor(entity.getDescontoBal());
		textDescontoBal.setText(vlr);

		vlrAnt = 0.00;
		vlrAnt = entity.getTotalBal();
		vlr = Mascaras.formataValor(vlrAnt);
		labelTotalBal.setText(vlr);
	}	

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (funService == null) {
			throw new IllegalStateException("Funcionário Serviço esta nulo");
		}
		// buscando (carregando) bco de dados		
				List<Funcionario> listFun = funService.findByAtivo("Ativo");
		// transf p/ obslist		
				obsListFun = FXCollections.observableArrayList(listFun);
				comboBoxFunBal.setItems(obsListFun);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorDataBal.setText((fields.contains("data") ? erros.get("data") : ""));
		labelErrorDataPrimeiroBal.setText((fields.contains("data1o") ? erros.get("data1o") : ""));
		labelErrorPagamentoBal.setText((fields.contains("pagamento") ? erros.get("pagamento") : ""));
		labelErrorNFBal.setText((fields.contains("nf") ? erros.get("nf") : ""));
		if (fields.contains("confirma")) {
			Alerts.showAlert("Fechamento", null, "Conferindo total", AlertType.INFORMATION);
			labelTotalBal.viewOrderProperty();
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
