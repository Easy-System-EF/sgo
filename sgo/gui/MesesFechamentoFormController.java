package sgo.gui;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.exception.ValidationException;
import sgo.model.entities.Adiantamento;
import sgo.model.entities.Anos;
import sgo.model.entities.DadosFolhaMes;
import sgo.model.entities.Funcionario;
import sgo.model.entities.Meses;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.AnosService;
import sgo.model.services.DadosFolhaMesService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.MesesService;

public class MesesFechamentoFormController implements Initializable {

	private DadosFolhaMes entity;
	private Meses mesObj;
	private Anos anosObj;
/*
 *  dependencia service com metodo set
 */
	private DadosFolhaMesService service;
	private AdiantamentoService adService;
	private FuncionarioService funService;
	private MesesService mesService;
	private AnosService anoService;
	private Meses objMes;
	private Anos objAno;

 	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private ComboBox<Meses>  comboBoxMeses; 
	
	@FXML
	private ComboBox<Anos>  comboBoxAnos; 
	
  	@FXML
	private Button btOk;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Label labelErrorComboBoxMeses;

	@FXML
	private Label labelErrorComboBoxAnos;

 	private ObservableList<Meses> obsListMes;
 	private ObservableList<Anos> obsListAno;
 	
//auxiliar
 	String classe = "";
 
 	public void setDadosFolhaMes(DadosFolhaMes entity) {		
		this.entity = entity;
	}

 	public void setObjMeses(Meses mesObj) {		
		this.mesObj = mesObj;
	}
 	public void setObjAnos(Anos anosObj) {		
		this.anosObj = anosObj;
	}

 // 	 * metodo set /p service
 	public void setServices(DadosFolhaMesService service, 
 							AdiantamentoService adService,
 							FuncionarioService funService,
 							MesesService mesService,
 							AnosService anoService) {
 		this.service = service;
 		this.adService = adService;
 		this.funService = funService;
 		this.mesService = mesService;
 		this.anoService = anoService;
	}
  	
//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtOkAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		try {
     		entity = getFormData();
     		montaDados(entity);
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
	private DadosFolhaMes getFormData() {
		DadosFolhaMes obj = new DadosFolhaMes();
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");

		obj.setMeses(comboBoxMeses.getValue());
 		if (obj.getMeses() == null) {
 		 	exception.addErros("meses", "mes inválido");
		}

		obj.setAnos(comboBoxAnos.getValue());
 		if (obj.getAnos() == null) {
 		 	exception.addErros("anos", "ano inválido");
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

	private void montaDados(DadosFolhaMes entity2) {
 		if (adService == null) {
			throw new IllegalStateException("Serviço Adiantamento está vazio");
 		}
 		if (funService == null) {
			throw new IllegalStateException("Serviço Funcionarios está vazio");
 		}
 		if (service == null) { 
			throw new IllegalStateException("Serviço DadosFolha está vazio");
 		}
 		if (mesService == null) {
			throw new IllegalStateException("Serviço Meses está vazio");
 		}
 		if (anoService == null) {
			throw new IllegalStateException("Serviço Anos está vazio");
 		}
 		try {
			classe = "Dados Folha";
			DadosFolhaMes dados = new DadosFolhaMes();
			service.zeraAll();
			
			classe = "Funcionario Dados ";
 			Funcionario funcionario = new Funcionario();
 			List<Funcionario> fun = funService.findAll();

 			int lin = fun.size();
 			int col = 8;
 			int il = 0;
 			int ic = 0;
 			String salario = "";
 			String vale = "";
 			String comissao = "";
 			String receber = "";
 			String total = "";
 			Double[] sal = new Double[lin];
 			Double[] com = new Double[lin];
 			Double[] adi = new Double[lin];
 			Double[] rec = new Double[lin];
 			Double[] tot = new Double[lin];
 			
 			Double totGer = 0.00;
		
 			Double[][] mes = new Double[lin][col];
 			Integer[] cod = new Integer[lin];
 			
 			for (il = 0 ; il < mes.length ; il++) {
 				for (ic = 0 ; ic < mes[il].length ; ic++) {
 					mes[il][ic] = 0.00;
 				}
 			}
 			for (il = 0 ; il < sal.length ; il++) {
 				sal[il] = 0.00;
 				com[il] = 0.00;
 				adi[il] = 0.00;
 				rec[il] = 0.00;
 				tot[il] = 0.00;
 			}
 		
 			List<Adiantamento> list = new ArrayList<>();
 			classe = "Adiantamento Dados ";
 			list = adService.findMes(entity2.getMeses().getNumeroMes(), 
 									entity2.getAnos().getAnoAnos());
			for (Adiantamento a : list) {
System.out.println(a);				
				cod[a.getCodFunAdi()] = a.getCodFunAdi();
				sal[a.getCodFunAdi()] = a.getFuncionario().getCargo().getSalarioCargo(); 
				mes[a.getCodFunAdi()][3] = sal[a.getCodFunAdi()];
				if (a.getComissaoAdi() > 0) {
					com[a.getCodFunAdi()] += a.getComissaoAdi();
					mes[a.getCodFunAdi()][4] = com[a.getCodFunAdi()];
				} else {
					com[a.getCodFunAdi()] += 0.00;
 					mes[a.getCodFunAdi()][4] = com[a.getCodFunAdi()];
 				}	
 				if (a.getValorAdi() > 0) {
 					adi[a.getCodFunAdi()] += a.getValorAdi();
 					mes[a.getCodFunAdi()][5] = adi[a.getCodFunAdi()];
 				} else {	
					adi[a.getCodFunAdi()] += 0.00;
					mes[a.getCodFunAdi()][5] = adi[a.getCodFunAdi()];
 				}
 				rec[a.getCodFunAdi()] = ((sal[a.getCodFunAdi()] + 
 										  com[a.getCodFunAdi()]) - 
 										  adi[a.getCodFunAdi()]);
 				mes[a.getCodFunAdi()][6] = rec[a.getCodFunAdi()];
 			}

			classe = "Dados Folha ";
			service.zeraAll();			
			classe = "Meses dados ";
			objMes = mesService.findId(entity2.getMeses().getNumeroMes());
			classe = "Anos dados ";
			objAno = anoService.findAno(entity2.getAnos().getAnoAnos());
			classe = "DadosFolhaMes ";
 			for (il = 0 ; il < mes.length ; il++) {
 				if (cod[il] != null) {
 					funcionario = funService.findById(cod[il]);
 					salario = Mascaras.formataValor(mes[il][3]);
 					comissao = Mascaras.formataValor(mes[il][4]);
 					vale = Mascaras.formataValor(mes[il][5]);
 					receber = Mascaras.formataValor(mes[il][6]);
 					totGer += mes[il][6];
 					total = Mascaras.formataValor(totGer);
 					dados.setNumeroDados(null);
 					dados.setNomeDados(funcionario.getNomeFun());
 					dados.setCargoDados(funcionario.getCargo().getNomeCargo());
 					dados.setSituacaoDados(funcionario.getSituacao().getNomeSit());
 					dados.setSalarioDados(salario);
 					dados.setComissaoDados(comissao);
 					dados.setValeDados(vale);
 					dados.setReceberDados(receber);
 					dados.setTotalDados(total);
 					dados.setMeses(objMes);
 					dados.setAnos(objAno);
System.out.println(dados); 					
 		 			service.insert(dados);
 				}	
 			}
 		}
 		catch (ParseException p) {
 			p.getStackTrace();
 		}
	}

	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeComboBoxMeses();
		initializeComboBoxAnos();
    	}

	private void initializeComboBoxMeses() {
		Callback<ListView<Meses>, ListCell<Meses>> factory = lv -> new ListCell<Meses>() {
			@Override
			protected void updateItem(Meses item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeMes());
 			}
		};
		
		comboBoxMeses.setCellFactory(factory);
		comboBoxMeses.setButtonCell(factory.call(null));
	}		
   	
	private void initializeComboBoxAnos() {
		Callback<ListView<Anos>, ListCell<Anos>> factory = lv -> new ListCell<Anos>() {
			@Override
			protected void updateItem(Anos item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getAnoStrAnos());
 			}
		};
		
		comboBoxAnos.setCellFactory(factory);
		comboBoxAnos.setButtonCell(factory.call(null));
	}		
   	
 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
 	public void updateFormData() {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
// se for uma inclusao, vai posicionar no 1o depto//tipo (First)	
 		if (entity.getMeses() == null) {
			comboBoxMeses.getSelectionModel().selectFirst();
		} else {
 			comboBoxMeses.setValue(entity.getMeses());
		}
 		if (entity.getAnos() == null) {
			comboBoxAnos.getSelectionModel().selectFirst();
		} else {
 			comboBoxAnos.setValue(entity.getAnos());
		}
     }
 	
//	carrega dados do bco  dentro obslist
	public void loadAssociatedObjects() {
		if (mesService == null) {
			throw new IllegalStateException("MesesServiço esta nulo");
		}
// buscando (carregando) os forn q estão no bco de dados		
		List<Meses> listMes = mesService.findAll();
 		obsListMes = FXCollections.observableArrayList(listMes);
		comboBoxMeses.setItems(obsListMes);
		List<Anos> listAno = anoService.findAll();
 		obsListAno = FXCollections.observableArrayList(listAno);
		comboBoxAnos.setItems(obsListAno);
  	}
  	
 // mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
		labelErrorComboBoxMeses.setText((fields.contains("meses") ? erros.get("meses") : ""));
 		labelErrorComboBoxAnos.setText((fields.contains("anos") ? erros.get("anos") : ""));
  	}
}	