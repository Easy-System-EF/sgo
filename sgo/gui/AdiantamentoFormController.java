package sgo.gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.exception.ValidationException;
import sgo.model.entities.Adiantamento;
import sgo.model.entities.Funcionario;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.CargoService;
import sgo.model.services.EmpresaService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.SituacaoService;

public class AdiantamentoFormController implements Initializable {

	private Adiantamento entity;
	
/*
 *  dependencia service com metodo set
 */
	private AdiantamentoService service;
	private FuncionarioService funService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textNumeroAdi;
	
	@FXML
	private TextField textIniciais;
	
	@FXML
	private ComboBox<Funcionario> comboBoxFun;
	
	@FXML
	private DatePicker dpDataAdi;
	
	@FXML
	private TextField textValorAdi;
	
   	@FXML
	private Button btSaveAdi;
	
	@FXML
	private Button btCancelAdi;
	
	@FXML
	private Button btPesquisa;
	@FXML
	private Label labelErrorNomeAdi;

	@FXML
	private Label labelErrorDataAdi; 
	
	@FXML
	private Label labelErrorValorAdi; 
	
 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usu�rio";
 	String pesquisa = ""; 
	String classe = "Adiantamento Form";
	public static Integer numEmp = null;

	private ObservableList<Funcionario>  obsListFun ;

 	public void setAdiantamento(Adiantamento entity) {
		this.entity = entity;
	} 

 // 	 * metodo set /p service
 	public void setServices(AdiantamentoService service,
 							FuncionarioService funService) {
		this.service = service;
		this.funService = funService;
	}
	
//  * o controlador tem uma lista de eventos q permite distribui��o via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
 	
	@FXML
	public void onBtPesquisaAction(ActionEvent event) {
		classe = "Adiantamento";
		try {
	  		pesquisa = textIniciais.getText().toUpperCase().trim();
	  		if (pesquisa != "") {
	  			List<Funcionario> listFun = funService.findPesquisa(pesquisa);
				if (listFun.size() == 0) { 
					pesquisa = "";
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
				 		 Stage parentStage = Utils.currentStage(event);
		 		 		 Funcionario obj = new Funcionario();
		 		 		 createDialogForm(obj, "/sgo/gui/FuncionarioForm.fxml", parentStage);
					}
					listFun = funService.findPesquisa(pesquisa);
			 	}
	  			obsListFun = FXCollections.observableArrayList(listFun);
	  			comboBoxFun.setItems(obsListFun);
	  			notifyDataChangeListerners();
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

/* 
 * vamos instanciar e salvar no bco de dados
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
	public void onBtSaveAdiAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade Adiantamento nula ");
		}
		if (service == null)
		{	throw new IllegalStateException("Servi�o nulo");
		}
 		try {
    		 entity = getFormData();
	    	 service.saveOrUpdate(entity);
	    	 imprimeRecibo(entity);
	    	 notifyDataChangeListerners();
	    	 Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe , e.getMessage(), AlertType.ERROR);
		}
		catch (ParseException p) {
			p.printStackTrace();
		}
	}
	
private void imprimeRecibo(Adiantamento entity2) {
	 AdiantamentoImprimeController adiantoImpr = new AdiantamentoImprimeController();
	 adiantoImpr.numEmp = numEmp;
	 adiantoImpr.setAdiantamento(entity2);
	 adiantoImpr.setServices(new AdiantamentoService(), new EmpresaService());
	 adiantoImpr.imprimeAdiantamento();	
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
	private Adiantamento getFormData() throws ParseException {
		Adiantamento obj = new Adiantamento();
 // instanciando uma exce��o, mas n�o lan�ado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setNumeroAdi(Utils.tryParseToInt(textNumeroAdi.getText()));
// tst name (trim elimina branco no principio ou final
// lan�a Erros - nome do cpo e msg de erro

		double vale = 0.00;
		int cont = 0;

		if (dpDataAdi.getValue() != null) {
			Instant instant = Instant.from(dpDataAdi.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataAdi(Date.from(instant));
		}
		if (dpDataAdi.getValue() == null) {
			exception.addErros("data", "Data � obrigat�ria");
		}

		obj.setFuncionario(comboBoxFun.getValue());
		obj.setCodFunAdi(comboBoxFun.getValue().getCodigoFun());
		obj.setNomeFunAdi(comboBoxFun.getValue().getNomeFun());
		obj.setCargoAdi(comboBoxFun.getValue().getCargo().getNomeCargo());
		obj.setSituacaoAdi(comboBoxFun.getValue().getSituacao().getNomeSit());
		obj.setCargo(comboBoxFun.getValue().getCargo());
		obj.setSituacao(comboBoxFun.getValue().getSituacao());

		Date dataHj = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataHj);
		int mm = cal.get(Calendar.MONTH) + 1;
		int aa = cal.get(Calendar.YEAR);
		cal.setTime(obj.getDataAdi());
		obj.setMesAdi(cal.get(Calendar.MONTH) + 1);
		obj.setAnoAdi(cal.get(Calendar.YEAR));
		if (mm != obj.getMesAdi() || aa != obj.getAnoAdi()) {
			exception.addErros("data", "mes ou ano inv�lido!!!");
		}

		List<Adiantamento> listAd = new ArrayList<>();
		listAd = service.findMesTipo(mm, aa, "A");
		for (Adiantamento a : listAd) {
			if (a.getCodFunAdi() == obj.getCodFunAdi()) {
				vale += a.getValorAdi();
				if (a.getDataAdi().equals(obj.getDataAdi())) { 
					cont += 1;
				}	
			}
		}
		
		obj.setBalcaoAdi(0);

		if (cont > 0) {
			exception.addErros("data", "J� existe adiantamento nesta data");				
		}

		textValorAdi.getText().replace(".", "");
		obj.setValorAdi(Utils.formatDecimalIn(textValorAdi.getText().replace(".", "")));
		double valido = 40 * comboBoxFun.getValue().getCargo().getSalarioCargo() / 100;
		if (obj.getValorAdi() == null || obj.getValorAdi() == 0.0) {
			exception.addErros("valor", "Valor � obrigat�rio");
		} else {
			if (obj.getValorAdi() + vale > valido) {
				exception.addErros("valor", "Estouro do limite de adiantamento");
			}	
		}
		
		obj.setTipoAdi("A");
		obj.setComissaoAdi(0.00);
		obj.setOsAdi(0);

  // tst se houve algum (erro com size > 0)		
		if (exception.getErros().size() > 0){
			throw exception;
		}
		return obj;
	} 
	
// msm processo save p/ fechar	
	@FXML
	public void onBtCancelAdiAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private void createDialogForm(Funcionario obj, String absoluteName, Stage parentStage) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			FuncionarioFormController controller = loader.getController();
			controller.user = user;
 // injetando passando parametro obj 			
			controller.setFuncionario(obj);
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setServices(new FuncionarioService(), new CargoService(), new SituacaoService());
			controller.loadAssociatedObjects();
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
//			controller.subscribeDataChangeListener(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Adiantamento                                             ");
 			dialogStage.setScene(new Scene(pane));
// pode redimencionar a janela: s/n?
			dialogStage.setResizable(false);
// quem e o stage pai da janela?
			dialogStage.initOwner(parentStage);
// travada enquanto n�o sair da tela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
 	} 
	
/*
 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho 
 */
  	@Override
	public void initialize(URL url, ResourceBundle rb) {  		
 		Constraints.setTextFieldInteger(textNumeroAdi);
// 		Constraints.setTextFieldDouble(textValorAdi);
 		Constraints.setTextFieldMaxLength(textIniciais, 7);
 		Utils.formatDatePicker(dpDataAdi, "dd/MM/yyyy");
		initializeComboBoxFuncionario();
  	}
  	
	private void initializeComboBoxFuncionario() {
		Callback<ListView<Funcionario>, ListCell<Funcionario>> factory = lv -> new ListCell<Funcionario>() {
			@Override
			protected void updateItem(Funcionario item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeFun());
 			}
		};
		
		comboBoxFun.setCellFactory(factory);
		comboBoxFun.setButtonCell(factory.call(null));
	}		
 
 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
  	public void updateFormData() throws ParseException {
 		if (entity == null) {
 			throw new IllegalStateException("Entidade esta nula");
 		}
 		labelUser.setText(user);
//  string value of p/ casting int p/ string 		
 		textNumeroAdi.setText(String.valueOf(entity.getNumeroAdi()));

 		if (entity.getNomeFunAdi() == null) {
			comboBoxFun.getSelectionModel().selectFirst();
		} else {
 			comboBoxFun.setValue(entity.getFuncionario());
		}
 		
		if (entity.getDataAdi() == null) {
			entity.setDataAdi(new Date());
		}
		
		if (entity.getDataAdi() != null) {
			dpDataAdi.setValue(LocalDate.ofInstant(entity.getDataAdi().toInstant(), ZoneId.systemDefault()));
		}

		if (entity.getValorAdi() == null) {
			entity.setValorAdi(0.00);
		}
		String vlr = Mascaras.formataValor(entity.getValorAdi());
		textValorAdi.setText(vlr);
   	}

	public void loadAssociatedObjects() {
		if (funService == null) {
			throw new IllegalStateException("Funcionario Servi�o esta nulo");
		}
// buscando (carregando) os cargos q est�o no bco de dados via Dialogform
		List<Funcionario> list = funService.findByAtivo("Ativo");
// transf p/ obslist		
		obsListFun = FXCollections.observableArrayList(list);
		comboBoxFun.setItems(obsListFun);
 	}
  	
// mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
 		labelErrorNomeAdi.setText((fields.contains("nome") ? erros.get("nome") : ""));		
 		labelErrorDataAdi.setText((fields.contains("data") ? erros.get("data") : ""));		
		labelErrorValorAdi.setText((fields.contains("valor") ? erros.get("valor") : ""));
   	}	
 } 
