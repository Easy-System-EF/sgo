package sgo.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
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
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.exception.ValidationException;
import sgcp.model.entityes.Fornecedor;
import sgo.model.entities.Cliente;
import sgo.model.services.ClienteService;

public class RecClienteFormController implements Initializable {

 	private Cliente entity;
/*
 *  dependencia service com metodo set
 */
 	private ClienteService service;

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textPesquisa; 
	
	@FXML
	private ComboBox<Cliente>  comboBoxCliente; 
	
  	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
  	@FXML
	private Button btPesquisa;
	
	@FXML
	private Label labelErrorComboBoxCliente;

 	private ObservableList<Cliente> obsList;
 	
	private Integer codigo;
	 
// auxiliares	
	String classe = "Cliente";
// destino identifica se list ou relatorio	
	static String destino = "";
// situacao identifica se geral aberto ou pago	
	static String situacao = "";
	
	String pesquisa = "";
	
	public static void setDestino(String strD) {
		destino = strD;
	}
	public static void setSituacao(String strS) {
		situacao = strS;
	}
 
 	public void setCliente(Cliente entity) {		
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setService(ClienteService service) {
 		this.service = service;
	}
  	
//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtPesquisaAction(ActionEvent event) {
		try {
		    pesquisa = textPesquisa.getText().toUpperCase().trim();
		    List<Cliente> list = service.findPesquisa(pesquisa);
			if (list.size() == 0) { 
				pesquisa = "";
				Alerts.showAlert("Cliente ", null, "Não encontrado ", AlertType.INFORMATION);
				codigo = null;
				list = service.findAll();
		 	}
	 		obsList = FXCollections.observableArrayList(list);
			comboBoxCliente.setItems(obsList);
			comboBoxCliente.getSelectionModel().selectFirst();
   	    	notifyDataChangeListerners();
   	    	updateFormData();
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		if (service == null)
		{	throw new IllegalStateException("Serviço nulo");
		}
		try {
			List<Cliente> list = new ArrayList<>();
			if (pesquisa != "") { 
				list = service.findPesquisa(pesquisa);
			} else {	
				list = service.findAll();
		 	}			
     		entity = getFormData();
			if (situacao == "Receber" && destino == "List") {
				ReceberListAbertoController.codCli = codigo;
			} 
			if (situacao == "Receber" && destino == "Relatorio") {
				ReceberRelatorioAbertoController.codCli = codigo;
			} 
			if (situacao == "Recebido" && destino == "List") {
				ReceberListPagoController.codCli = codigo;
			} 
			if (situacao == "Recebido" && destino == "Relatorio") {
				ReceberRelatorioPagoController.codCli = codigo;
			} 
   	    	notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
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
	private Cliente getFormData() {
		Cliente obj = new Cliente();
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
		codigo = comboBoxCliente.getValue().getCodigoCli();
		obj = comboBoxCliente.getValue();
		if (exception.getErros().size() > 0) {
			throw exception;
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
  		Constraints.setTextFieldMaxLength(textPesquisa,	7);
		initializeComboBoxCliente();
    	}

	private void initializeComboBoxCliente() {
		Callback<ListView<Cliente>, ListCell<Cliente>> factory = lv -> new ListCell<Cliente>() {
			@Override
			protected void updateItem(Cliente item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeCli());
 			}
		};
		
		comboBoxCliente.setCellFactory(factory);
		comboBoxCliente.setButtonCell(factory.call(null));
	}		
   	
 /*
  * transforma string da tela p/ o destino no bco de dados 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
// se for uma inclusao, vai posicionar no 1o depto//destino (First)
		comboBoxCliente.getSelectionModel().selectFirst();
     }
 	
//	carrega dados do bco  dentro 
	public void loadAssociatedObjects() {
		if (service == null) {
			throw new IllegalStateException("ClienteServiço esta nulo");
		}
// buscando (carregando) os forn q estão no bco de dados
		List<Cliente> list = new ArrayList<>();
		list = service.findAll();
 		obsList = FXCollections.observableArrayList(list);
		comboBoxCliente.setItems(obsList);
  	}
  	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
		labelErrorComboBoxCliente.setText((fields.contains("cliente") ? erros.get("cliente") : ""));
  	}
}	