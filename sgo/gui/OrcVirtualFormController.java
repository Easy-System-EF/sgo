package sgo.gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.exception.ValidationException;
import sgo.model.entities.Balcao;
import sgo.model.entities.Material;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Orcamento;
import sgo.model.services.BalcaoService;
import sgo.model.services.GrupoService;
import sgo.model.services.MaterialService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.OrcamentoService;

public class OrcVirtualFormController implements Initializable, DataChangeListener {

	private OrcVirtual entity;
	Material mat = new Material();
 
	/*
	 * dependencia service com metodo set
	 */
	private OrcVirtualService service;
	private OrcamentoService orcService;
	private BalcaoService balService;
	private MaterialService matService;
 
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
		try {
			updateFormData();
			getFormData();
		}
		catch (ParseException p) {
			p.printStackTrace();
		}
	}

	@FXML
	private Label labelEntidade;

	@FXML
	private TextField textNumeroOrcBalVir;

	@FXML
	private TextField textPesquisa;

	@FXML
	private ComboBox<Material> comboBoxMatVir;

	@FXML
	private TextField textQtdMatVir;

	@FXML
	private Label labelPrecoMatVir;

	@FXML
	private Label labelTotalMatVir;

	@FXML
	private Button btPesquisa;
	
	@FXML
	private Button btSaveVir;
	
	@FXML
	private Button btCancelVir;

	@FXML
	private Label labelErrorMaterialVir;

	@FXML
	private Label labelErrorQtdMatVir;

	@FXML
	private Label labelErrorPrecoMatVir;
	
 	@FXML
 	private Label labelUser;

 // auxiliar
 	public String user = "usuário";		
 	String classe = "OrcVirtual ";
 	String pesquisa = "";
	double totAnt = 0.00;
	int flag = 0;
	int flagE = 0;
	public static Integer numOrc = 0;
	public static Integer numBal = 0;
	private double maoObra = 0;
	private int flagGrava = 0;
	
	private ObservableList<Material> obsListMat;

	public void setOrcVirtual(OrcVirtual entity) {
		this.entity = entity;
	}

 	// * metodo set /p service
	public void setOrcVirtualService(OrcVirtualService service) {
		this.service = service;
	}

	public void setOrcamentoService(OrcamentoService orcService) {
		this.orcService = orcService;
	}

	public void setBalcaoService(BalcaoService balService) {
		this.balService = balService;
	}

	public void setMaterialService(MaterialService matService) {
		this.matService = matService;
	}
	
	@FXML
	public void onBtPesqMatAction(ActionEvent event) {
		classe = "Material";
		pesquisa = "";
		try {
	  		pesquisa = textPesquisa.getText().toUpperCase().trim();
	  		if (pesquisa != "") {
	  			List<Material> listMat = matService.findPesquisa(pesquisa);
				if (listMat.size() == 0) { 
					pesquisa = "";
					Optional<ButtonType> result = Alerts.showConfirmation("Pesquisa sem resultado ", "Deseja incluir?");
					if (result.get() == ButtonType.OK) {
				 		 Stage parentStage = Utils.currentStage(event);
				 		 Material obj = new Material();
				 		 createDialogMat(obj, "/sgo/gui/MaterialForm.fxml", parentStage);
				   	}
					listMat = matService.findPesquisa(pesquisa);
			 	}
	  			obsListMat = FXCollections.observableArrayList(listMat);
	  			comboBoxMatVir.setItems(obsListMat);
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
	 * vamos instanciar um orc e salvar no bco de dados meu obj entity (lá em cima)
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
	public void onBtSaveVirAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			ValidationException exception = new ValidationException("Validation exception");
			entity = getFormData();
			flagGrava = 2;
			classe = "OrcVirtual";
			service.saveOrUpdate(entity);
			if (numOrc > 0) {
				Orcamento orc = orcService.findById(entity.getNumeroOrcVir());
				orc.setTotalOrc(entity.getTotalMatVir());
				orcService.saveOrUpdate(orc);
			}
			if (numBal > 0) {
				if (flagGrava == 2) {
					List<OrcVirtual> virtual = service.findByBalcao(numBal);
//					updateMaterial(virtual);
					Balcao bal = balService.findById(entity.getNumeroBalVir());
					bal.setTotalBal(entity.getTotalMatVir());
				}	
			}
			flag = 0;
			if (entity.getTotalMatVir() != 0 && flag == 0) { 
				exception.addErros("confirma", "confirma?");
				String vlr = Mascaras.formataValor(entity.getPrecoMatVir());
				labelPrecoMatVir.setText(String.valueOf(vlr));
				vlr = Mascaras.formataValor(entity.getTotalMatVir());
				labelTotalMatVir.setText(vlr);
			}
 			notifyDataChangeListerners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException p) {
			p.printStackTrace();
		}
	}

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se não for atz tb verificamos se cpos
	 * obrigatórios estão preenchidos, para informar erro(s) para cpos string não
	 * precisa tryParse
	 */
	public OrcVirtual getFormData() throws ParseException {
		ValidationException exception = new ValidationException("Validation exception");
 		OrcVirtual obj = new OrcVirtual();
		// instanciando uma exceção, mas não lançado - validation exc....
// set CODIGO c/ utils p/ transf string em int \\ ou null
		obj.setNumeroVir(entity.getNumeroVir());
		if (numOrc > 0) {
			obj.setNumeroOrcVir(numOrc);
			obj.setNumeroBalVir(0);
		} else {
			if (numBal > 0) {
				obj.setNumeroBalVir(numBal);
				obj.setNumeroOrcVir(0);
			}	
		}
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro
		
		int flagMao = 0;
		obj.setMaterial(comboBoxMatVir.getValue()); 
		int cod = comboBoxMatVir.getValue().getCodigoMat();
		if (comboBoxMatVir.getValue().getGrupo().getNomeGru() == "Mão de Obra") {
			flagMao = 1;
		}		
		Material mat = matService.findById(cod); 
		obj.setNomeMatVir(mat.getNomeMat());
		obj.setPrecoMatVir(mat.getVendaMat());
		String vlr = Mascaras.formataValor(mat.getVendaMat());
		labelPrecoMatVir.setText(vlr);

		if (textQtdMatVir.getText() == null || textQtdMatVir.getText().trim().contentEquals("")) {
			exception.addErros("qtd", "Qtd é obrigatória");
			flagE = 1;
		}	else { 
			if (textQtdMatVir.getText() != null) {
				obj.setQuantidadeMatVir(Utils.formatDecimalIn(textQtdMatVir.getText().replace(".", "")));
				if (obj.getQuantidadeMatVir() != null) {
					obj.setTotalMatVir(obj.getQuantidadeMatVir() * obj.getPrecoMatVir());
				}
			}	
		}

		List<OrcVirtual> vir = service.findByBalcao(numBal);
		for (OrcVirtual v : vir) {
			if (v.getNumeroOrcVir() == obj.getNumeroOrcVir()) {
				if (v.getMaterial().getCodigoMat().equals(obj.getMaterial().getCodigoMat())) {
					if (obj.getNumeroVir() != v.getNumeroVir()) {
						exception.addErros("material", "Erro!!! já existe material");
						flagE = 1;
					}	
				}
			}	
		}
		
		if (numBal > 0) {
			if (flagMao != 1) {
				confereSaldo(vir);
			}	
		}

		if (flagE == 0) {
			if (obj.getTotalMatVir() != 0 && flag == 0) {
				if (obj.getTotalMatVir() != totAnt) {
					exception.addErros("confirma", "confirma?");
					String vlr2 = Mascaras.formataValor(obj.getPrecoMatVir());
					labelPrecoMatVir.setText(vlr2);
					vlr2 = Mascaras.formataValor(obj.getTotalMatVir());
					labelTotalMatVir.setText(vlr2);
				}	
			}	
		}
		
		if (obj.getQuantidadeMatVir() == 0) {
			exception.addErros("qtd", "Quantidade não pode ser 0");
			flagE = 1;
		}	 
  				
// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

	private void confereSaldo(List<OrcVirtual> virtual2) {
		try {
			classe = "Material ";
			ValidationException exception = new ValidationException("Validation exception");
			for (OrcVirtual v : virtual2) {
				mat = matService.findById(v.getMaterial().getCodigoMat());
				if (mat.getGrupo().getNomeGru() == "Mão de obra") {
					maoObra += mat.getVendaMat();
				}
				if (mat.getGrupo().getNomeGru() != "Mão de obra") {
					if(v.getQuantidadeMatVir() > mat.getSaldoMat()) {
						exception.addErros("material", "Erro !!! Saldo insuficiente ");
						flagGrava = 1;
					}	
				}
			}			
			if (exception.getErros().size() > 0) {
				throw exception;
			}
		}	
		catch (DbException e) {
			Alerts.showAlert("Erro confere saldo balcao ", classe, e.getMessage(), AlertType.ERROR);
		}
	}

		private void updateMaterial(List<OrcVirtual> virtual2) {
			try {
				classe = "Material";
				ValidationException exception = new ValidationException("Validation exception");
				for (OrcVirtual v : virtual2) {
					mat = matService.findById(v.getMaterial().getCodigoMat());
					if (flagGrava == 2) {
						mat.setSaidaMat(v.getQuantidadeMatVir());
						mat.setCmmMat(mat.calculaCmm());
						matService.saveOrUpdate(mat);
					}
				}	
				if (exception.getErros().size() > 0) {
					throw exception;
				}
			}	
			catch (DbException e) {
				Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
			}
		}
		
	// msm processo save p/ fechar
	@FXML
	public void onBtCancelVirAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldMaxLength(textPesquisa, 7);
		initializeComboBoxMatVir();
	}

	private void initializeComboBoxMatVir() {
		Callback<ListView<Material>, ListCell<Material>> factory = lv -> new ListCell<Material>() {
			@Override
			protected void updateItem(Material item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeMat());
			}
		};

		comboBoxMatVir.setCellFactory(factory);
		comboBoxMatVir.setButtonCell(factory.call(null));
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
		if (numOrc > 0) {
			textNumeroOrcBalVir.setText(String.valueOf(numOrc));
			labelEntidade.setText("Orçamento");
		}	

		if (numBal > 0) { 
			textNumeroOrcBalVir.setText(String.valueOf(numBal));
			labelEntidade.setText("Balcão");
		}	
		
// se for uma inclusao, vai posicionar no 1o registro (First)
		if (entity.getMaterial() == null) {
			comboBoxMatVir.getSelectionModel().selectFirst();
		} else {
			comboBoxMatVir.setValue(entity.getMaterial());
		}
		
		if (entity.getQuantidadeMatVir() != null) {
			String qtd = Mascaras.formataValor(entity.getQuantidadeMatVir());
			textQtdMatVir.setText(qtd);
		}	else {
				entity.setQuantidadeMatVir(0.0);
				textQtdMatVir.setText(String.valueOf(entity.getQuantidadeMatVir()));
			}

		if (entity.getNumeroOrcVir() != null) {
			String vlr = Mascaras.formataValor(entity.getMaterial().getVendaMat());
			labelPrecoMatVir.setText(vlr);
		}	
		if (entity.getNumeroBalVir() != null) {
			String vlr = Mascaras.formataValor(entity.getMaterial().getVendaMat());
			labelPrecoMatVir.setText(vlr);
		}	
		
		if (entity.getTotalMatVir() != null) {
			totAnt = entity.getTotalMatVir();
		}	
		
		String vlr2 = Mascaras.formataValor(totAnt);
		labelTotalMatVir.setText(vlr2);
		textPesquisa.setText(pesquisa);
	}

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (numOrc > 0 && orcService == null) {
			throw new IllegalStateException("OrcamentoServiço esta nulo");
		} else {
			if (numBal > 0 && balService == null) {
				throw new IllegalStateException("Balcão Serviço esta nulo");
			}
		}
		if (service == null) {
			throw new IllegalStateException("VirtualServiço esta nulo");
		}
		labelUser.setText(user);
 // buscando (carregando) os dados do bco de dados	
		List<Material> listMat = matService.findAll();
		List<OrcVirtual> vir = service.findAll();
// transf p/ obslist
		obsListMat = FXCollections.observableArrayList(listMat);
		comboBoxMatVir.setItems(obsListMat);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorMaterialVir.setText((fields.contains("material") ? erros.get("material") : ""));
		labelErrorQtdMatVir.setText((fields.contains("qtd") ? erros.get("qtd") : ""));
		labelErrorPrecoMatVir.setText((fields.contains("preco") ? erros.get("preco") : ""));
		if (fields.contains("confirma") && flag == 0) {
			Alerts.showAlert("Fechamento", null, "Conferindo total", AlertType.INFORMATION);
			labelPrecoMatVir.viewOrderProperty();
			flag = 1;
		}
		flagE = 0;
	}

	private void createDialogMat(Material obj, String absoluteName, Stage parentStage) {
		try {
			classe = "Material ";
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			MaterialFormController controller = loader.getController();
			controller.user = user;
			controller.setMaterial(obj);
			controller.setMaterialService(new MaterialService());
			controller.setGrupoService(new GrupoService());
			controller.loadAssociatedObjects();
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Digite Material                                             ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro carregando tela" + classe, e.getMessage(), AlertType.ERROR);
		}
 	} 	
}
