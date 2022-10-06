package sgo.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import application.Main;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.exception.ValidationException;
import sgo.model.entities.ReposicaoVeiculo;
import sgo.model.services.ReposicaoVeiculoService;

public class ReposicaoDadosFormController implements Initializable {

 	private ReposicaoVeiculo entity;
/*
 *  dependencia service com metodo set
 */
 	private ReposicaoVeiculoService service;

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField  textPlacaDado; 
	
	@FXML
	private TextField  textKmDado; 
	
  	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
 	private ObservableList<ReposicaoVeiculo> obsList;
 	
// auxiliares	
	String classe = "ReposicaoDados";
	String placa = "";
	int km = 1;
	
 	public void setReposicaoVeiculo(ReposicaoVeiculo entity) {		
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setService(ReposicaoVeiculoService service) {
 		this.service = service;
	}
  	
//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtOkAction(ActionEvent event) {
		try {
			getFormData();
   	    	notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	public void getFormData() {
		placa = textPlacaDado.getText().toUpperCase();
		km = Utils.tryParseToInt(textKmDado.getText());
		ReposicaoVeiculoListController.placaDado =  placa;
		ReposicaoVeiculoListController.kmDado = km;
	}

// *   um for p/ cada listener da lista, eu aciono o metodo onData no DataChangListner...   
	private void notifyDataChangeListerners() {
		for (DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}	
	}

 	public void updateTableView() {
  		textPlacaDado.setText("placaDado");
  		textKmDado.setText(String.valueOf("999999"));
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
		Constraints.setTextFieldInteger(textKmDado);
		Constraints.setTextFieldMaxLength(textKmDado, 6);
		Constraints.setTextFieldMaxLength(textPlacaDado, 07);
    	}
}	