package sgo.gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.exception.ValidationException;
import sgo.model.entities.Receber;
import sgo.model.services.ReceberService;
 
public class ReceberFormController implements Initializable {

//	lista da classe subject - armazena os dados a serem atz no formulario 
//	recebe e emite eventos
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

   	@FXML
	private TextField textOsRec;

	@FXML
	private DatePicker dpDataOsRec;
 
   	@FXML
	private TextField textClienteRec;

  	@FXML
	private TextField textPlacaRec;

  	@FXML
	private TextField textParcelaRec;

	@FXML
	private DatePicker dpDataVencimentoRec;
 
 	@FXML
	private TextField textValorRec; 

	@FXML
	private DatePicker dpDataPagamentoRec;

	@FXML
	private Label labelErrorDataPagamento;

 	@FXML
	private Button btOk;

	@FXML
	private Button btCancel;

	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	String dataf;
	String classe = "receber";
	 	
 	Receber receber = new Receber();
   
//	carrega a lista de dados p/ mostrar comboBox
	private ObservableList<Receber> obsList;
  
//	carrega os dados do formulario	
	private Receber entity;
	private Receber parAnterior;
		
//	carrega dados do banco na crição do formulario - injeção de dependencia
 	private ReceberService service;
	  
	public void setReceber(Receber entity) {
		this.entity = entity;
	}
  	
//	busca os dados no bco de dados	
	public void setService (ReceberService service) {
 		this.service = service;
 	}

//	armazena dados a serem atz no bco de dados	
	public void subscribeDataChangeListener(DataChangeListener listener) {
  		dataChangeListeners.add(listener);
	}
 	
	@FXML
	public void onBtOkAction(ActionEvent event) throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
 		if (service == null) {
			throw new IllegalStateException("Serviço esta nulo");
		} 
		try { 	entity = getFormData();
  				service.insert(entity);
 				notifyDataChangeListeners();
				Utils.currentStage(event).close();
   				
 		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando classe objeto ", null, e.getMessage(), AlertType.ERROR);
		}
 	}

//	p/ cada listener da lista, eu aciono o metodo onData...	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

 // carrega o obj com os dados do formulario retornando	obj
	 private Receber getFormData() throws ParseException {
			Receber obj = new Receber();
			obj = entity;
 			ValidationException exception = new ValidationException("Validation error");
		
			Instant instant = Instant.from(dpDataPagamentoRec.getValue().atStartOfDay(ZoneId.systemDefault()));
// 			instant = Instant.from(dpDataPagamentoRec.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataPagamentoRec(Date.from(instant));
			if (dpDataPagamentoRec.getValue() == null) { 
				exception.addErros("dataPagamento", "data é obrigatória");
 			}
			Date dto = obj.getDataOsRec();
			Date dtp = obj.getDataPagamentoRec();
			Date dtv = obj.getDataVencimentoRec();
 			if (dtp.before(dto)) {
				exception.addErros("dataPagamento", "data pagto é menor que data da OS");
				obj.setDataPagamentoRec(null);
			} else {
				if (dtp.before(dtv)) {
					exception.addErros("dataPagamento", "data pagto é menor que data vecto");
					obj.setDataPagamentoRec(null);
				}
			}	
    			
      		if (exception.getErros().size() > 0) {
			throw exception;
     		}
   		return obj;
 	}
	 
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
 	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
 	}
 	
	private void initializeNodes() {
 		Utils.formatDatePicker(dpDataOsRec, "dd/MM/yyyy");
 		Utils.formatDatePicker(dpDataVencimentoRec, "dd/MM/yyyy");
 		Utils.formatDatePicker(dpDataPagamentoRec, "dd/MM/yyyy");
 	}

 //  dados do obj p/ preencher o formulario		 
	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta vazia ");
		}
  //  string value of p/ casting int p/ string
		if (parAnterior == null)
		{	parAnterior = entity;
		}
  		textOsRec.setText(String.valueOf(entity.getOsRec()));
		dpDataOsRec.setValue(LocalDate.ofInstant(entity.getDataOsRec().toInstant(), ZoneId.systemDefault()));
		dpDataVencimentoRec.setValue(LocalDate.ofInstant(entity.getDataVencimentoRec().toInstant(), ZoneId.systemDefault()));
		textClienteRec.setText(entity.getNomeClienteRec());
		textPlacaRec.setText(entity.getPlacaRec());
		textParcelaRec.setText(String.valueOf(entity.getParcelaRec()));
		entity.setDataPagamentoRec(entity.getDataVencimentoRec());
		dpDataVencimentoRec.setValue(LocalDate.ofInstant(entity.getDataVencimentoRec().toInstant(), ZoneId.systemDefault()));
		
		double vlr = 0.0;
		String vlrM = null;
		vlr = entity.getValorRec();
   		vlrM = Mascaras.formataValor(vlr); 
   		textValorRec.setText(vlrM);
   		if (entity.getDataPagamentoRec() != null) {
			dpDataPagamentoRec.setValue(LocalDate.ofInstant(entity.getDataPagamentoRec().toInstant(), ZoneId.systemDefault()));
 		}
  	}
	
  //	carrega dados do bco fornecedor dentro obslist
	public void loadAssociatedObjects() {
		if (service == null) {
			throw new IllegalStateException("Receber Serviço esta nulo");
		}
  		List<Receber> list = service.findAllAberto();
 		obsList = FXCollections.observableArrayList(list);
}
 
 // informação de campos labelErro(msg)	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorDataPagamento.setText((fields.contains("dataPagamento") ? erros.get("dataPagamento") : ""));
  	}
}
