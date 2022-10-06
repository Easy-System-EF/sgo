package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.exception.ValidationException;
import model.services.TipoFornecedorService;
import model.services.ParPeriodoService;
import sgcp.model.entityes.TipoFornecedor;
import sgcp.model.entityes.consulta.ParPeriodo;

public class ParTipoFormController implements Initializable {

	private ParPeriodo entity;
	private ParPeriodo perAnterior;
 	private TipoFornecedor tipoFornecedor;
/*
 *  dependencia service com metodo set
 */
	private ParPeriodoService perService;
 	private TipoFornecedorService tipoService;

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private ComboBox<TipoFornecedor>  comboBoxTipoFornecedor; 
	
  	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Label labelErrorComboBoxTipoFornecedor;

	public void setPeriodo(ParPeriodo entity, ParPeriodo perAnterior) {
		this.entity = entity;
		this.perAnterior = perAnterior;
 	}	

 	private ObservableList<TipoFornecedor> obsListTipo;
	private Integer codigo;
 
 	public void setTipoFornecedor(TipoFornecedor tipoFornecedor) {		
		this.tipoFornecedor = tipoFornecedor;
	}

 // 	 * metodo set /p service
 	public void setPeriodoService(ParPeriodoService perService, TipoFornecedorService tipoService) {
		this.perService = perService;
 		this.tipoService = tipoService;
	}
  	
//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

/* 
 * vamos instanciar um tipoforn e salvar no bco de dados
 * meu obj entity (lá em cima) vai receber uma chamada do getformdata
 *  metodo q busca dados do formulario convertidos getForm (string p/ int ou string)		
 *  pegou no formulario e retornou (convertido) p/ jogar na variavel entity
 *  chamo o service na rotina saveupdate e mando entity
 *  vamos tst entity e service = null -> não foi injetado
 *  para fechar a janela, pego a referencia para janela atual (event) e close
 *  DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar uma lista
 *  qdo ela salvar obj com sucesso, é só notificar (juntar)
 *  recebe lá no  listController    		 
 */
	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		if (perService == null)
		{	throw new IllegalStateException("Serviço nulo");
		}
		try {
			List<ParPeriodo> listPer = perService.findAll();
     		entity = getFormData();
 			for (ParPeriodo p : listPer)
 			{ 	entity.setIdPeriodo(p.getIdPeriodo());
 				entity.setDtiPeriodo(p.getDtiPeriodo());
 				entity.setDtfPeriodo(p.getDtfPeriodo());
 				entity.setFornecedor(p.getFornecedor()); 
      		 	codigo = p.getTipoFornecedor().getCodigoTipo();
 			} 
 			perService.update(entity);
   	    	notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}	
	}

/*
 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int (la no util)
 * se codigo for nulo insere, se não for atz
 * tb verificamos se cpos obrigatórios estão preenchidos, para informar erro(s)
 * para cpos string não precisa tryParse	
 */
	private ParPeriodo getFormData() {
		ParPeriodo obj = new ParPeriodo();
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
 
 		obj.setTipoFornecedor(comboBoxTipoFornecedor.getValue());
 		if (obj.getTipoFornecedor() == null)
 		{ 	exception.addErros("tipoFornecedor", "tipoFornecedor inválido");
		}
  			
		List<TipoFornecedor> listFor = tipoService.findAll();
		for (TipoFornecedor f : listFor)
		{	if (f.getCodigoTipo().equals(obj.getTipoFornecedor().getCodigoTipo()))
			{	codigo = (f.getCodigoTipo());
 			}
 		}	
  		if (exception.getErros().size() > 0)
		{	throw exception;
		}
		return obj;
	}
	
  // msm processo save p/ fechar	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeComboBoxTipoFornecedor();
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
   	
 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
 		if (entity.getTipoFornecedor() == null) {
			comboBoxTipoFornecedor.getSelectionModel().selectFirst();
		} else {
 			comboBoxTipoFornecedor.setValue(entity.getTipoFornecedor());
		}
     }
 	
//	carrega dados do bco  dentro obslist
	public void loadAssociatedObjects() {
		if (perService == null) {
			throw new IllegalStateException("TipoFornecedorServiço esta nulo");
		}
// buscando (carregando) os forn q estão no bco de dados		
		List<TipoFornecedor> listFor = tipoService.findAll();
 		obsListTipo = FXCollections.observableArrayList(listFor);
		comboBoxTipoFornecedor.setItems(obsListTipo);
  	}
  	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
		labelErrorComboBoxTipoFornecedor.setText((fields.contains("tipoFornecedor") ? erros.get("tipoFornecedor") : ""));
  	}
}	

