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
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.exception.ValidationException;
import model.services.TipoFornecedorService;
import sgcp.model.entityes.TipoFornecedor;

public class TipoFornecedorFormController implements Initializable {

	private TipoFornecedor entity;
	
/*
 *  dependencia service com metodo set
 */
	private TipoFornecedorService service;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textCodigoTipo;
	
	@FXML
	private TextField textNomeTipo;
	
 	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Label labelErrorNomeTipo;

	@FXML
	private Label labelUser;

	String classe = "Tipo Fornecedor ";
	public String user = "usu�rio";	
	  
 	public void setTipoFornecedor(TipoFornecedor entity) {
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setTipoFornecedorService(TipoFornecedorService service) {
		this.service = service;
	}
	
//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

/* 
 * vamos instanciar um tipoforn e salvar no bco de dados
 * meu obj entity (l� em cima) vai receber uma chamada do getformdata
 *  metodo q busca dados do formulario convertidos getForm (string p/ int ou string)		
 *  pegou no formulario e retornou (convertido) p/ jogar na variavel entity
 *  chamo o service na rotina saveupdate e mando entity
 *  vamos tst entity e service = null -> n�o foi injetado
 *  para fechar a janela, pego a referencia para janela atual (event) e close
 *  DataChangeListner classe subjetc - q emite o evento q muda dados, vai guardar uma lista
 *  qdo ela salvar obj com sucesso, � s� notificar (juntar)
 *  recebe l� no  listController    		 
 */
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		if (service == null)
		{	throw new IllegalStateException("Servi�o nulo");
		}
		try {
    		 entity = getFormData();
	    	 service.saveOrUpdate(entity);
	    	 notifyDataChangeListerners();
	    	 Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
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
 * se codigo for nulo insere, se n�o for atz
 * tb verificamos se cpos obrigat�rios est�o preenchidos, para informar erro(s)
 * para cpos string n�o precisa tryParse	
 */
	private TipoFornecedor getFormData() {
		TipoFornecedor obj = new TipoFornecedor();
 // instanciando uma exce��o, mas n�o lan�ado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setCodigoTipo(Utils.tryParseToInt(textCodigoTipo.getText()));
// tst name (trim elimina branco no principio ou final
// lan�a Erros - nome do cpo e msg de erro
		if (textNomeTipo.getText() == null || textNomeTipo.getText().trim().contentEquals("")) {
			exception.addErros("descri�ao", "Descri��o � obrigat�rio");
		}
		else {
			if (textNomeTipo.getText().length() < 5) {
				exception.addErros("descri�ao", "Descri��o � inv�lida");
			}
		}
			obj.setNomeTipo(textNomeTipo.getText());
// tst se houve algum (erro com size > 0)		
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
 		Constraints.setTextFieldInteger(textCodigoTipo);
  		Constraints.setTextFieldMaxLength(textNomeTipo, 20);
  	}

 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
 		labelUser.setText(user);
 //  string value of p/ casting int p/ string 		
 		textCodigoTipo.setText(String.valueOf(entity.getCodigoTipo()));
 		textNomeTipo.setText(entity.getNomeTipo());
   	}
 	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
 		labelErrorNomeTipo.setText((fields.contains("descri�ao") ? erros.get("descri�ao") : ""));		
	}
}	

