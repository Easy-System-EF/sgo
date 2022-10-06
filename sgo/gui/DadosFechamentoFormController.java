package sgo.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listerneres.DataChangeListener;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.exception.ValidationException;
import model.services.CompromissoService;
import model.services.TipoFornecedorService;
import sgcp.model.entityes.Compromisso;
import sgcp.model.entityes.TipoFornecedor;
import sgo.model.entities.Adiantamento;
import sgo.model.entities.Anos;
import sgo.model.entities.Balcao;
import sgo.model.entities.DadosFechamento;
import sgo.model.entities.Funcionario;
import sgo.model.entities.Meses;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Orcamento;
import sgo.model.entities.OrdemServico;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.AnosService;
import sgo.model.services.BalcaoService;
import sgo.model.services.DadosFechamentoService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.MesesService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.OrcamentoService;
import sgo.model.services.OrdemServicoService;

public class DadosFechamentoFormController implements Initializable {

	private DadosFechamento entity;
/*
 *  dependencia service com metodo set
 */
	private DadosFechamentoService service;
	private AdiantamentoService adService;
	private MesesService mesService;
	private AnosService anoService;
	private OrdemServicoService osService;
	private OrcamentoService orcService;
	private OrcVirtualService virService;
	private FuncionarioService funService;
	private BalcaoService balService;
	private TipoFornecedorService tipoService;
	private CompromissoService comService;
	private Meses objMes;
	private Anos objAno;
	private Orcamento orc;
	private Adiantamento adianto;
	private Funcionario fun;
	private Balcao bal;

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
 	int mm = 0;
 	int aa = 0;
 
 	public void setDadosEntityes(DadosFechamento entity,
 								 Orcamento orc,
 								 Adiantamento adianto,
 								 Funcionario fun,
 								 Balcao bal
 								) {		
		this.entity = entity;
		this.orc = orc;
		this.adianto = adianto;
		this.fun = fun;
		this.bal = bal;
	}

 // 	 * metodo set /p service
 	public void setServices(DadosFechamentoService service, 
 							AdiantamentoService adService,
 							MesesService mesService,
 							AnosService anoService,
 							OrdemServicoService osService,
 							OrcamentoService orcService,
 							OrcVirtualService virService,
 							FuncionarioService funService,
 							BalcaoService balService,
 							TipoFornecedorService tipoService,
 							CompromissoService comService) {
 		this.service = service;
 		this.adService = adService;
 		this.mesService = mesService;
 		this.anoService = anoService;
 		this.osService = osService;
 		this.orcService = orcService;
 		this.virService = virService;
 		this.funService = funService;
 		this.balService = balService;
 		this.tipoService = tipoService;
 		this.comService = comService;
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
     		montaDadosMensal();
   	    	notifyDataChangeListerners();
	    	Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
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
	private DadosFechamento getFormData() {
		DadosFechamento obj = new DadosFechamento();
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");

		obj.setMes(comboBoxMeses.getValue());
		mm = comboBoxMeses.getValue().getNumeroMes();
 		if (obj.getMes() == null) {
 		 	exception.addErros("meses", "mes inválido");
		}

		obj.setAno(comboBoxAnos.getValue());
		aa = comboBoxAnos.getValue().getAnoAnos();
 		if (obj.getAno() == null) {
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

	private void montaDadosMensal() {
 		if (adService == null) {
			throw new IllegalStateException("Serviço Adiantamento está vazio");
 		}
 		if (service == null) { 
			throw new IllegalStateException("Serviço DadosFechamentoMensal está vazio");
 		}
 		if (mesService == null) {
			throw new IllegalStateException("Serviço Meses está vazio");
 		}
 		if (anoService == null) {
			throw new IllegalStateException("Serviço Anos está vazio");
 		}
 		if (osService == null) {
			throw new IllegalStateException("Serviço OS está vazio");
 		}
 		if (orcService == null) {
			throw new IllegalStateException("Serviço Orçamento está vazio");
 		}
 		if (virService == null) {
			throw new IllegalStateException("Serviço Virtual está vazio");
 		}
		classe = "Dados Fechamento 1 ";
		DadosFechamento dados = new DadosFechamento();
		dados.setValorResultadoMensal(0.00);
		dados.setValorAcumuladoMensal(0.00);
		service.zeraAll();

		classe = "Meses ";
		objMes = mesService.findId(mm);
		classe = "Anos dados ";
		objAno = anoService.findAno(aa);

// procura codigo dos tipos
		classe = "TipoFornecedor ";
		int tpImposto = 0;
		int tpTaxa = 0;
		List<TipoFornecedor> listTipo = tipoService.findAll();
		for (TipoFornecedor t: listTipo) {
			if (t.getNomeTipo() == "Imposto") {
				tpImposto = t.getCodigoTipo();
			}
			if (t.getNomeTipo() == "Taxa") {
				tpTaxa = t.getCodigoTipo();
			}
		}
		
// monta dados tributos		
		Calendar cal = Calendar.getInstance();
		int mmCom = 0;
		int aaCom = 0;
		classe = "Compromisso ";
		double vlrImposto = 0.00;
		double vlrTaxa = 0.00;
		List<Compromisso> listImp = comService.findByTipo(tpImposto);
		for (Compromisso cI : listImp) {
			cal.setTime(cI.getDataCom());
			mmCom = cal.get(Calendar.MONTH) + 1;
			aaCom = cal.get(Calendar.YEAR);
			if (mmCom == mm && aaCom == aa) {
				vlrImposto += cI.getValorCom();
			}	
		}
		List<Compromisso> listTaxa = comService.findByTipo(tpTaxa);
		for (Compromisso cI : listTaxa) {
			cal.setTime(cI.getDataCom());
			mmCom = cal.get(Calendar.MONTH) + 1;
			aaCom = cal.get(Calendar.YEAR);
			if (mmCom == mm && aaCom == aa) {
				vlrTaxa += cI.getValorCom();
			}	
		}

// som salarios		
		classe = "Funcionario ";
		double vlrFolha = 0.00;
		List<Funcionario> list = funService.findAll();
		for (Funcionario f: list) {
			if (f.getSituacao().getNomeSit().equals("Ativo")) {
				vlrFolha += f.getCargo().getSalarioCargo();
			}				
		}
		
// zera comissão		
		classe = "Adiantamento ";
		List<Adiantamento> adZera = adService.findMes(mm, aa);
		for (Adiantamento a : adZera) {
			if (a.getCodFunAdi() != null) {
				if (a.getComissaoAdi() > 0 || a.getValorAdi() > 0) {
					classe = "Funcionario Dados 1 ";
					fun = funService.findById(a.getCodFunAdi());
					fun.setComissaoFun(0.00);
					fun.setAdiantamentoFun(0.00);
					funService.saveOrUpdate(fun);
				}	
			}	
		}
		
// atualiza comissao		
		List<Adiantamento> adCom = adService.findMes(mm, aa);
		for (Adiantamento a : adCom) {
			if (a.getCodFunAdi() != null) {
				if (a.getComissaoAdi() > 0 || a.getValorAdi() > 0) {
					classe = "Funcionario Dados 2 ";
					fun = funService.findById(a.getCodFunAdi());
					fun.totalComissaoFun(a.getComissaoAdi());
					fun.totalAdiantamentoFun(a.getValorAdi());
					funService.saveOrUpdate(fun);
				}	
			}	
		}
		
// monta dados OS		
		classe = "OS ";
		int qtdOs = 0;
		List<OrdemServico> listOs = osService.findByMesAno(mm, aa);
		qtdOs = listOs.size();
		if (listOs != null) {
			classe = "Orcamento ";
			List<OrcVirtual> listVir = new ArrayList<>();
			Double material = 0.00;
			for (OrdemServico o : listOs) {
				if (o.getNumeroOS() != null) {
					dados.setBalMensal(0);
					dados.setOsMensal(o.getNumeroOS());
					dados.setDataMensal(o.getDataOS());
					dados.setValorOsMensal(o.getValorOS());
					classe = "Orcamento ";
					orc = orcService.findById(o.getOrcamentoOS());
					fun = funService.findById(orc.getFuncionario().getCodigoFun());
					classe = "OrcVirtual ";
					listVir = virService.findByOrcto(orc.getNumeroOrc());
					dados.setClienteMensal(orc.getClienteOrc());
					dados.setFuncionarioMensal(orc.getFuncionarioOrc().substring(0, 20));
					material = 0.00;
					for (OrcVirtual v : listVir) {
						if (v.getNumeroOrcVir() != null) {
							if (v.getMaterial().getGrupo().getNomeGru() != "Mão de Obra") {
							material += (v.getMaterial().getPrecoMat() *
											v.getQuantidadeMatVir());
							}
						}
					}
					dados.setValorMaterialMensal(material);
					dados.setValorComissaoMensal(0.00);
					if (o.getValorOS() > 0) {
						Adiantamento ad = new Adiantamento();
						ad = adService.findByOs(o.getNumeroOS());
						if (ad != null) {
							dados.setValorComissaoMensal(ad.getComissaoAdi());
						}	
					}
//					dados.setValorResultadoMensal(0.0);
//					dados.setValorAcumuladoMensal(0.00);
					dados.resultadoMensal();
					dados.acumuladoMensal();
					dados.setMes(objMes);
					dados.setAno(objAno);	
					classe = "Dados Fechamento ";
					service.insert(dados);
				}				
			}
		}
		
// monta dados balcão		
		classe = "Balcão ";
		int qtdBal = 0;
		List<Balcao> listBal = balService.findByMesAno(mm, aa);
		if (listBal != null) {
			qtdBal = listBal.size();
			List<OrcVirtual> listVir = new ArrayList<>();
			Double material = 0.00;
			for (Balcao b : listBal) {
				if (b.getNumeroBal() != null) {
					dados.setOsMensal(0);
					dados.setBalMensal(b.getNumeroBal());
					dados.setDataMensal(b.getDataBal());
					dados.setValorOsMensal(b.getTotalBal());
					dados.setClienteMensal("Balcão");
					dados.setFuncionarioMensal(b.getFuncionarioBal());
					fun = funService.findById(b.getFuncionario().getCodigoFun());
					classe = "OrcVirtual ";
					material = 0.00;
					listVir = virService.findByBalcao(b.getNumeroBal());
					for (OrcVirtual vB : listVir) {
						if (vB.getNumeroOrcVir() != null &&						
								vB.getMaterial().getGrupo().getNomeGru() != "Mão de obra") {
							material += (vB.getMaterial().getPrecoMat() *
											vB.getQuantidadeMatVir());
						}
					}
					if (dados.getValorResultadoMensal() == null ) {
						dados.setValorResultadoMensal(0.0);
						dados.setValorAcumuladoMensal(0.00);
					}
					dados.setValorMaterialMensal(material);
					dados.setValorComissaoMensal(fun.getComissaoFun());
					dados.resultadoMensal();
					dados.acumuladoMensal();
					dados.setMes(objMes);
					dados.setAno(objAno);	
					classe = "Dados Fechamento ";
					service.insert(dados);
				}				
			}
		}

// monta tributos		
		Date data = new Date();
		dados.setNumeroMensal(null);
		dados.setOsMensal(0);
		dados.setBalMensal(0);
		dados.setDataMensal(data);
		dados.setClienteMensal("");
		dados.setFuncionarioMensal("Tributos p/ OS/Bal");
		dados.setValorComissaoMensal(0.00);
		double tributos = (vlrImposto + vlrTaxa + vlrFolha);
		double tributos1 = (vlrImposto + vlrTaxa + vlrFolha) / (qtdOs + qtdBal);
		dados.setValorResultadoMensal(0.00);
		dados.setValorAcumuladoMensal(dados.getValorAcumuladoMensal() - tributos); 
		dados.setValorOsMensal(tributos);
		dados.setValorMaterialMensal(tributos1);
		service.insert(dados);
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
 		if (entity.getMes() == null) {
			comboBoxMeses.getSelectionModel().selectFirst();
		} else {
 			comboBoxMeses.setValue(entity.getMes());
		}
 		if (entity.getAno() == null) {
			comboBoxAnos.getSelectionModel().selectFirst();
		} else {
 			comboBoxAnos.setValue(entity.getAno());
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