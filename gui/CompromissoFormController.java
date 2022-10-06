package gui;

import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.exception.ValidationException;
import model.services.CompromissoService;
import model.services.FornecedorService;
import model.services.ParPeriodoService;
import model.services.ParcelaService;
import model.services.TipoFornecedorService;
import sgcp.model.entityes.Compromisso;
import sgcp.model.entityes.Fornecedor;
import sgcp.model.entityes.Parcela;
import sgcp.model.entityes.TipoFornecedor;
import sgcp.model.entityes.consulta.ParPeriodo;
import sgo.model.entities.Cliente;
 
public class CompromissoFormController implements Initializable {

 //	carrega os dados do formulario	
	private Compromisso entity;
	
 //	carrega dados do banco na cri��o do formulario - inje��o de dependencia
	private CompromissoService service;
 	private FornecedorService fornecedorService;
	private TipoFornecedorService tipoService;
	private ParcelaService parService;
	private ParPeriodoService perService;
 
//	lista da classe subject - armazena os dados a serem atz no formulario 
//	recebe e emite eventos
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

   	@FXML
	private TextField textNnfCom;

	@FXML
	private DatePicker dpDataCom;
 
	@FXML
	private DatePicker dpDataVencimentoCom;
 
	@FXML
	private TextField textValorCom;

  	@FXML
	private TextField textParcelaCom;

  	@FXML 
	private TextField textPrazoCom;
  	
  	@FXML
  	private TextField textIniciais;

	@FXML
	private ComboBox<Fornecedor> comboBoxFornecedor;

	@FXML
	private ComboBox<TipoFornecedor> comboBoxTipoFornecedor;

	@FXML
	private Label labelErrorNnf;

	@FXML
	private Label labelErrorDataCompra;

	@FXML
	private Label labelErrorDataVencimentoCompra;

	@FXML
	private Label labelErrorValor;

	@FXML
	private Label labelErrorParcela;

	@FXML
	private Label labelErrorPrazo;
	
	@FXML
	private Button btPesq;

	@FXML
	private Button btOk;

	@FXML
	private Button btCancel;

	@FXML
	private Label labelUser;

	String classe = "Compromisso ";
	String pesquisa = "";
	public String user = "usu�rio";	
	
 	Parcela parcela = new Parcela();
 	ParPeriodo periodo = new ParPeriodo();
 	Compromisso comp = new Compromisso();
  
//	carrega a lista de dados p/ mostrar comboBox
	private ObservableList<Fornecedor> obsList;
	private ObservableList<TipoFornecedor> obsListTipo;
 
	public void setCompromisso(Compromisso entity) {
		this.entity = entity;
	}
 	
// carregando parcelas, se houver 	
	public void setParcela(Parcela parcela) {
		this.parcela = parcela;
	}
	
	public void setPeriodo(ParPeriodo periodo) {
		this.periodo = periodo;
	}
 	
//	busca os dados no bco de dados	
	public void setServices (CompromissoService service, FornecedorService fornecedorService, 
							TipoFornecedorService tipoService, ParcelaService parService,
							ParPeriodoService perService) {
		this.service = service;
		this.fornecedorService = fornecedorService;
		this.tipoService = tipoService;
		this.parService = parService;
		this.perService = perService;
	}
//	armazena dados a serem atz no bco de dados	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
 		if (service == null) {
			throw new IllegalStateException("Servi�o esta nulo");
		} 
		try {
// 	carrega os dados do formulario	p/ atz bco dados
   			if (comp.getNnfCom() != null && comp.getNnfCom() != 0)
   			{	service.remove(comp);
 				parcela.setNnfPar(comp.getNnfCom());
				parcela.setCodigoFornecedorPar(comp.getCodigoFornecedorCom());
 				parService.removeNnf(parcela);
   			}
				entity = getFormData();
				List<ParPeriodo> listP = perService.findAll();
				for (ParPeriodo p : listP)
				{	periodo.setFornecedor(p.getFornecedor());
					periodo.setTipoFornecedor(p.getTipoFornecedor());
					periodo.setIdPeriodo(p.getIdPeriodo());
				}
  				entity.setParPeriodo(periodo);
				comp = entity;
 				service.saveOrUpdate(entity);
  				criaParcela(entity);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
   				
 		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando (compromisso) objeto ", null, e.getMessage(), AlertType.ERROR);
		}
 	}
	
	@FXML
	public void onBtPesquisaAction(ActionEvent event) {
		classe = "Fornecedor";
		try {
	  		pesquisa = textIniciais.getText().toUpperCase().trim();
	  		if (pesquisa != "") {
	  			List<Fornecedor> list = fornecedorService.findPesquisa(pesquisa);
	  			obsList = FXCollections.observableArrayList(list);
	  			comboBoxFornecedor.setItems(obsList);
	  			notifyDataChangeListeners();
	  			updateFormData();
	  		}	
		} catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}
	
 //	p/ cada listener da lista, eu aciono o metodo onData...	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

  	public void criaParcela(Compromisso entity) {
   		Locale ptBR = new Locale("pt", "BR");
   		Calendar cal = Calendar.getInstance(); 
		cal.setTime(entity.getDataVencimentoCom());
 		int par = entity.getParcelaCom() + 1;
// gerando (parcela(s) / conforme parcela(s) do compromisso 
   		for (int i = 1; i < par; i ++)
		{	if (i > 1)
   			{	cal.add(Calendar.DAY_OF_MONTH, (entity.getPrazoCom()));
   			}
 			parcela.setCodigoFornecedorPar(entity.getCodigoFornecedorCom());
			parcela.setNomeFornecedorPar(entity.getNomeFornecedorCom());
 			parcela.setNnfPar(entity.getNnfCom());
  			parcela.setNumeroPar(i);
 			parcela.setDataVencimentoPar(cal.getTime());
  			if (entity.getParcelaCom() > 1)
  			{	parcela.setValorPar(parcela.calculaParcelas(entity.getValorCom(), entity.getParcelaCom(), i ));
  			}	else
  				{	 parcela.setValorPar(entity.getValorCom());
  				}
   		    parcela.setDescontoPar(0.00);
 		    parcela.setJurosPar(0.00);
 		    parcela.setTotalPar(parcela.getValorPar()); 
		    parcela.setPagoPar(0.00);
		    if (entity.getPrazoCom() == 1 )
		    {	if (entity.getParcelaCom() == 1)
		    	{	parcela.setPagoPar(entity.getValorCom());
		    	}
 		    }
		    parcela.setDataPagamentoPar(cal.getTime());
  		    parcela.setFornecedor(entity.getFornecedor());
 		    parcela.setTipoFornecedor(entity.getTipoFornecedor());
 		    parcela.setPeriodo(entity.getParPeriodo());
   			cal.setTime(parcela.getDataVencimentoPar());
   		    parService.insert(parcela); 
   		}
  	}
   
// carrega o obj com os dados do formulario retornando	obj
	 private Compromisso getFormData() {
		Compromisso obj = new Compromisso();
 
		ValidationException exception = new ValidationException("Validation error");
		 
// p/ transf string do formulario p/ bco em int \\ ou null		
		if (textNnfCom.getText() != null || textNnfCom.getText().trim().contentEquals("")) {
			obj.setNnfCom(Utils.tryParseToInt(textNnfCom.getText()));
		} else {
				exception.addErros("nnf", "NNF n�o pode ser 0");	
		}
  
		if (dpDataCom.getValue() == null) {
			exception.addErros("dataCom", "data � obrigat�ria");
		}
		else {
			Instant instant = Instant.from(dpDataCom.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataCom(Date.from(instant));
		}

		if (dpDataVencimentoCom.getValue() == null) {
			exception.addErros("dataVencimentoCom", "data � obrigat�ria");
		}
		else {
			Instant instant = Instant.from(dpDataVencimentoCom.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataVencimentoCom(Date.from(instant));
		}
		if (obj.getDataCom() != null) {
			Date dtc = obj.getDataCom();
			Date dtv = obj.getDataVencimentoCom();
			if (dtv.before(dtc)) {
				exception.addErros("dataVencimentoCom", "data menor que data da compra");
			}
		}
		
  		if (textValorCom.getText() == null || textValorCom.getText().trim().contentEquals("")) {
			exception.addErros("valor", "Valor n�o pode ser 0");
		} 
  		else {
  			obj.setValorCom(Utils.formatDecimalIn(textValorCom.getText().replace(".", "")));
  		}
  		

   		if (textParcelaCom.getText() == null || textParcelaCom.getText().trim().contentEquals("")) {
 			exception.addErros("parcela", "Parcela n�o pode ser 0");	
 		}
   		else {
   			obj.setParcelaCom(Utils.tryParseToInt(textParcelaCom.getText()));
   		}
   		if (obj.getParcelaCom() != null) {
   			int par = obj.getParcelaCom();
   			if (par == 0 || par == 04 || par == 5 || par == 7 || par == 8 || par == 9 || 
  				par == 10 || par == 11 || par > 12) { 
   				exception.addErros("parcela", "Parcela = 1, 2, 3, 6 ou 12");	
   			}
   		}	

  		if (textPrazoCom.getText() == null || textPrazoCom.getText().trim().contentEquals("")) { 
			exception.addErros("prazo", "Prazo n�o pode ser nulo");	
		}
  		else {
  			obj.setPrazoCom(Utils.tryParseToInt(textPrazoCom.getText()));
  			if (obj.getParcelaCom() == 1 && obj.getPrazoCom() != 1) {
  				exception.addErros("prazo", "Parcela �nica, prazo tem que ser = 1"); 
  			}	
  			if (obj.getParcelaCom() > 1 && obj.getPrazoCom() == 1) { 
  				exception.addErros("prazo", "Parcela n�o � �nica, prazo tem que ser > 1"); 
  			}
  		}	
  
  		if (obj.getPrazoCom() != null) {
  			int prz = obj.getPrazoCom();
  			if (prz == 1 || prz == 10 || prz == 14 || prz == 21 || prz == 30 || prz == 60) {
  				int flagp = 0;
  			}
  			else {
  				exception.addErros("prazo", "Prazo = 1(a vista), 10, 14, 21, 30 ou 60(dias)");	
  			}
  		}
  		
 		obj.setFornecedor(comboBoxFornecedor.getValue());
 // pego dados do forn, coloco no list, pego nome e atz fornCom 		
		List<Fornecedor> listFor = fornecedorService.findAll();
		for (Fornecedor f : listFor)
		{	if (f.getCodigo().equals(obj.getFornecedor().getCodigo()))
			{	obj.setCodigoFornecedorCom(f.getCodigo());
				obj.setNomeFornecedorCom(f.getRazaoSocial());
			}
		}	
 
		obj.setTipoFornecedor(comboBoxTipoFornecedor.getValue());
  		if (exception.getErros().size() > 0) {
			throw exception;
		}
  		
  		obj.setParPeriodo(comp.getParPeriodo());
  		return obj;
	}
	 
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
 	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		initializeComboBoxFornecedor();
		initializeComboBoxTipoFornecedor();
	}
 	
	private void initializeNodes() {
 		Constraints.setTextFieldInteger(textNnfCom);
//		Constraints.setTextFieldDouble(textValorCom);
 		Constraints.setTextFieldInteger(textParcelaCom);
		Constraints.setTextFieldInteger(textPrazoCom);
 		Constraints.setTextFieldMaxLength(textNnfCom, 06);
 		Constraints.setTextFieldMaxLength(textParcelaCom, 02);
 		Constraints.setTextFieldMaxLength(textPrazoCom, 02);
 		Constraints.setTextFieldMaxLength(textIniciais, 7);
		Utils.formatDatePicker(dpDataCom, "dd/MM/yyyy");
		Utils.formatDatePicker(dpDataVencimentoCom, "dd/MM/yyyy");
   	}

	private void initializeComboBoxFornecedor() {
		Callback<ListView<Fornecedor>, ListCell<Fornecedor>> factory = lv -> new ListCell<Fornecedor>() {
			@Override
			protected void updateItem(Fornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getRazaoSocial());
 			}
		};
		
		comboBoxFornecedor.setCellFactory(factory);
		comboBoxFornecedor.setButtonCell(factory.call(null));
	}		

	private void initializeComboBoxTipoFornecedor() {
		Callback<ListView<TipoFornecedor>, ListCell<TipoFornecedor>> factory = lv -> new ListCell<TipoFornecedor>() {
			@Override
			protected void updateItem(TipoFornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeTipo());
			}
		};
		
		comboBoxTipoFornecedor.setCellFactory(factory);
		comboBoxTipoFornecedor.setButtonCell(factory.call(null));
	}		

//  dados do obj p/ preencher o formulario		 
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta vazia ");
		}
		labelUser.setText(user);
// aq pego compromisso p/ remover se trocar alguma coisa 		
		comp = entity;
//		limpaCampos(entity);
//  string value of p/ casting int p/ string
   		textNnfCom.setText(String.valueOf(entity.getNnfCom()));

   		if (entity.getDataCom() != null) {
			dpDataCom.setValue(LocalDate.ofInstant(entity.getDataCom().toInstant(), ZoneId.systemDefault()));
 		}
 
   		if (entity.getDataVencimentoCom() != null) {
			dpDataVencimentoCom.setValue(LocalDate.ofInstant(entity.getDataVencimentoCom().toInstant(), ZoneId.systemDefault()));
 		}

   		if (entity.getValorCom() != null)
   		{	String vlr = Mascaras.formataValor(entity.getValorCom());
   	   		textValorCom.setText(vlr);
   		}
  
   		textParcelaCom.setText(String.valueOf(entity.getParcelaCom()));
		textPrazoCom.setText(String.valueOf(entity.getPrazoCom()));
		textIniciais.setText(pesquisa);
 		
  // se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
 		if (entity.getFornecedor() == null) {
			comboBoxFornecedor.getSelectionModel().selectFirst();
		} else {
 			comboBoxFornecedor.setValue(entity.getFornecedor());
		}
		if (entity.getTipoFornecedor() == null) {
			comboBoxTipoFornecedor.getSelectionModel().selectFirst();
		} else {
			comboBoxTipoFornecedor.setValue(entity.getTipoFornecedor());
		}
 	}

	private void limpaCampos(Compromisso entity) {
		if (entity.getNnfCom() == null) {
			entity.setNnfCom(0);
		}
		if (entity.getValorCom() == null) {
			entity.setValorCom(0.00);
		}
		if (entity.getParcelaCom() == null) {
			entity.setParcelaCom(0);
		}	
		if (entity.getPrazoCom() == null) {
			entity.setPrazoCom(0);
		}
	 }	
   
//	carrega dados do bco fornecedor dentro obslist
	public void loadAssociatedObjects() {
		if (fornecedorService == null) {
			throw new IllegalStateException("FornecedorServi�o esta nulo");
		}
// buscando (carregando) os forn q est�o no bco de dados		
		List<Fornecedor> list = fornecedorService.findAll();
// transf p/ obslist		
		obsList = FXCollections.observableArrayList(list);
		comboBoxFornecedor.setItems(obsList);
		if (tipoService == null) {
			throw new IllegalStateException("TipoFornecedorServi�o esta nulo");
		}
// buscando (carregando) os tipos q est�o no bco de dados		
		List<TipoFornecedor> listTipo = tipoService.findAll();
// transf p/ obslist		
		obsListTipo = FXCollections.observableArrayList(listTipo);
		comboBoxTipoFornecedor.setItems(obsListTipo);
	}

// informa��o de campos labelErro(msg)	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
//	condicional temporal (?), ou seja, se contains � V, entra 1a op��o erros.get
//	se n�o, entra branco ""  -- fica mais enxuto
		labelErrorNnf.setText((fields.contains("nnf") ? erros.get("nnf") : ""));
		labelErrorDataCompra.setText((fields.contains("dataCom") ? erros.get("dataCom") : ""));
		labelErrorDataVencimentoCompra.setText((fields.contains("dataVencimentoCom") ? erros.get("dataVencimentoCom") : ""));
		labelErrorValor.setText((fields.contains("valor") ? erros.get("valor") : ""));
		labelErrorParcela.setText((fields.contains("parcela") ? erros.get("parcela") : ""));
		labelErrorPrazo.setText((fields.contains("prazo") ? erros.get("prazo") : ""));
	}
}
