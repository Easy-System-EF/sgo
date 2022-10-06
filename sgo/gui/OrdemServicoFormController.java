package sgo.gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
import gui.util.CalculaParcela;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.exception.ValidationException;
import model.services.ParPeriodoService;
import sgcp.model.entityes.consulta.ParPeriodo;
import sgo.model.entities.Adiantamento;
import sgo.model.entities.Funcionario;
import sgo.model.entities.Material;
import sgo.model.entities.NotaFiscal;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Orcamento;
import sgo.model.entities.OrdemServico;
import sgo.model.entities.Receber;
import sgo.model.entities.ReposicaoVeiculo;
import sgo.model.entities.Veiculo;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.MaterialService;
import sgo.model.services.NotaFiscalService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.OrcamentoService;
import sgo.model.services.OrdemServicoService;
import sgo.model.services.ReceberService;
import sgo.model.services.ReposicaoVeiculoService;
import sgo.model.services.VeiculoService;

public class OrdemServicoFormController implements Initializable, DataChangeListener {

	private OrdemServico entity;
	private Orcamento orcamento;
	private Material material;
	private ParPeriodo periodo;
	private Veiculo veiculo;
	private Funcionario funcionario;
	private Adiantamento adiantamento;
	private NotaFiscal nota = new NotaFiscal();

	/*
	 * private OrdemServico entity; dependencia service com metodo set
	 */
	private OrdemServicoService service;
	private OrcamentoService orcService;
	private OrcVirtualService virService;
	private MaterialService matService;
	private ReceberService recService;
	private ReposicaoVeiculoService repService;
	private ParPeriodoService perService;
	private VeiculoService veiService;
	private AdiantamentoService adiService;
	private FuncionarioService funService;
	private NotaFiscalService nfService;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textNumeroOS;

	@FXML
	private DatePicker dpDataOS;

	@FXML
	private TextField textPesquisa;

	@FXML
	private ComboBox<Orcamento> comboBoxOrcamento;

	@FXML
	private RadioButton rbParcelaUnica;

	@FXML
	private RadioButton rbParcela02;

	@FXML
	private RadioButton rbParcela03;

	@FXML
	private RadioButton rbPrazoAvista;

	@FXML
	private RadioButton rbPrazo10;

	@FXML
	private RadioButton rbPrazo15;

	@FXML
	private RadioButton rbPrazo30;

	@FXML
	private RadioButton rbPagamentoDinheiro;

	@FXML
	private RadioButton rbPagamentoPix;

	@FXML
	private RadioButton rbPagamentoDebito;

	@FXML
	private RadioButton rbPagamentoCC;

	@FXML
	private DatePicker dpData1oOS;

	@FXML
	private TextField textNnfOS;

	@FXML
	private TextField textObservacaoOS;

	@FXML
	private Button btPesquisa;

	@FXML
	private Button btSaveOS;

	@FXML
	private Button btCancelOS;

	@FXML
	private Label labelErrorDataOS;

	@FXML
	private Label labelErrorOrcamentoOS;

	@FXML
	private Label labelErrorParcelaOS;

	@FXML
	private Label labelErrorPrazoOS;

	@FXML
	private Label labelErrorPagamentoOS;

	@FXML
	private Label labelErrorData1oOS;

	@FXML
	private Label labelErrorNnfOS;

	@FXML
	private Label labelUser;

	// auxiliar
	public String user = "usuário";
	String pesquisa = "";
	int flagg = 0;
// saber se func tem comissao	
	double flagCom = 0.0;
	String classe = "";
	Double maoObra = 0.00;
	int ultimaNF = 0;

	private ObservableList<Orcamento> obsListOrc;

	public void setOrdemServico(OrdemServico entity) {
		this.entity = entity;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public void setPeriodo(ParPeriodo periodo) {
		this.periodo = periodo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	// * metodo set /p service
	public void setServices(OrdemServicoService service, OrcamentoService orcService, OrcVirtualService virService,
			MaterialService matService, ReceberService recService, ReposicaoVeiculoService repService,
			ParPeriodoService perService, VeiculoService veiService, FuncionarioService funService,
			AdiantamentoService adiService, NotaFiscalService nfService) {
		this.service = service;
		this.orcService = orcService;
		this.virService = virService;
		this.matService = matService;
		this.recService = recService;
		this.repService = repService;
		this.perService = perService;
		this.veiService = veiService;
		this.funService = funService;
		this.adiService = adiService;
		this.nfService = nfService;
	}

	@FXML
	public void onBtPesquisaAction(ActionEvent event) throws ParseException {
		classe = "Orçamento";
		pesquisa = "";
		String placa = null;
		try {
			ValidationException exception = new ValidationException("Validation exception");
			pesquisa = textPesquisa.getText().toUpperCase().trim();
			if (pesquisa != "") {
				List<Orcamento> listOrc = orcService.findPesquisa(pesquisa);
				if (listOrc.size() == 0) { 
					pesquisa = "";
					Alerts.showAlert("Orçamento ", "Não encontrado ", "ou fechado ",  AlertType.INFORMATION);
					listOrc = orcService.findAll();
			 	}
				for (Orcamento o : listOrc) {
					placa = o.getPlacaOrc();
				}	
				if (placa == null) {
					exception.addErros("dataOS", "Não existe Orçamento ");
					exception.addErros("orcto", "aberto para consulta ");
				} else {
					obsListOrc = FXCollections.observableArrayList(listOrc);
					comboBoxOrcamento.setItems(obsListOrc);
					notifyDataChangeListerners();
					updateFormData();
				}	
			}
			if (exception.getErros().size() > 0) {
				throw exception;
			}	
		} 
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (ParseException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		} 
		catch (DbException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro pesquisando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	/*
	 * vamos instanciar um forn e salvar no bco de dados meu obj entity (lá em cima)
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
	public void onBtSaveOSAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade OS nula");
		}
		if (orcService == null) {
			throw new IllegalStateException("Serviço orçamento nulo");
		}
		try {
			entity = getFormData();
			confereSaldo(entity);
			classe = "Ordem de Serviço";
			service.saveOrUpdate(entity);
			nfService.saveOrUpdate(nota);
			classe = "Orçamento ";
			orcamento = orcService.findById(entity.getOrcamentoOS());
			if (flagg == 0) {
				flagg = 2;
				updateMaterialOS(entity);
				porPeriodo();
				gravaReceberOS(entity);
				createReposicao(entity, orcamento);
				updateOsOrcamento(entity, orcamento);
				updateKmVeiculo(entity);
				if (maoObra > 0) {
					gravaComissaoOS(entity);
				}
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

	private void confereSaldo(OrdemServico entity2) {
		try {
			classe = "Material";
			ValidationException exception = new ValidationException("Validation exception");
			List<OrcVirtual> vir = virService.findByOrcto(entity2.getOrcamentoOS());
			for (OrcVirtual v : vir) {
				material = matService.findById(v.getMaterial().getCodigoMat());
				if (material.getGrupo().getNomeGru() == "Mão de obra" && flagCom > 0) {
					maoObra += material.getVendaMat();
				}
				if (material.getGrupo().getNomeGru() != "Mão de obra") {
					if (v.getQuantidadeMatVir() > material.getSaldoMat()) {
						exception.addErros("orcto", "Erro !!! Saldo < saida ");
						exception.addErros("parcela", v.getMaterial().getNomeMat());
						exception.addErros("prazo", "Efetue entrada");
						flagg = 1;
					}
				}
			}
			if (exception.getErros().size() > 0) {
				throw exception;
			}
		} catch (DbException e) {
			Alerts.showAlert("Erro saldo OS ", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void updateMaterialOS(OrdemServico entity2) {
		try {
			classe = "Material";
			ValidationException exception = new ValidationException("Validation exception");
			List<OrcVirtual> vir = virService.findByOrcto(entity2.getOrcamentoOS());
			for (OrcVirtual v : vir) {
				material = matService.findById(v.getMaterial().getCodigoMat());
				if (material.getGrupo().getNomeGru() == "Mão de obra") {
					material.setEntradaMat(v.getQuantidadeMatVir());
				}
				if (flagg == 2) {
					material.setSaidaMat(v.getQuantidadeMatVir());
					material.setCmmMat(material.calculaCmm());
					matService.saveOrUpdate(material);
				}
			}
			if (exception.getErros().size() > 0) {
				throw exception;
			}
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private ParPeriodo porPeriodo() {
		try {
			classe = "Período";
			SimpleDateFormat sdfi = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dti = sdfi.parse("01/01/2001 00:00:00");
			Date dtf = sdff.parse("31/01/2041 00:00:00");

			Integer cod = 1;
			periodo = new ParPeriodo();
			periodo = perService.findById(cod);
			periodo.setIdPeriodo(1);
			periodo.setDtiPeriodo(dti);
			periodo.setDtfPeriodo(dtf);
			perService.update(periodo);
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		} catch (ParseException e) {
			Alerts.showAlert("ParseException ", "Erro Data Periodo ", e.getMessage(), AlertType.ERROR);
		}
		return periodo;
	}

	private void gravaReceberOS(OrdemServico entity2) {
		try {
			classe = "Receber";
			Receber receber = new Receber();
			orcamento = orcService.findById(entity2.getOrcamentoOS());
			receber.setFuncionarioRec(orcamento.getFuncionario().getCodigoFun());
			receber.setClienteRec(orcamento.getCliente().getCodigoCli());
			receber.setNomeClienteRec(orcamento.getCliente().getNomeCli());
			receber.setOsRec(entity2.getNumeroOS());
			receber.setDataOsRec(entity2.getDataOS());
			receber.setPlacaRec(entity2.getPlacaOS());
			receber.setPeriodo(periodo);
			if (entity2.getPagamentoOS() == 1) {
				receber.setFormaPagamentoRec("Dinheiro");
			} else {
				if (entity2.getPagamentoOS() == 2) {
					receber.setFormaPagamentoRec("Pix");
				} else {
					if (entity2.getPagamentoOS() == 3) {
						receber.setFormaPagamentoRec("Débito");
					} else {
						if (entity2.getPagamentoOS() == 4) {
							receber.setFormaPagamentoRec("CC");
						}
					}
				}
			}
			if (entity2.getPrazoOS() == 1) {
				receber.setDataPagamentoRec(entity2.getDataPrimeiroPagamentoOS());
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date dtp = sdf.parse("00/00/0000 00:00:00");
				receber.setDataPagamentoRec(dtp);
			}
			for (int i = 1; i < entity2.getParcelaOS() + 1; i++) {
				receber.setParcelaRec(i);
				if (i == 1) {
					receber.setDataVencimentoRec(entity2.getDataPrimeiroPagamentoOS());
				}
				if (i > 1) {
					receber.setDataVencimentoRec(CalculaParcela
							.CalculaVencimentoDia(entity2.getDataPrimeiroPagamentoOS(), (i - 1), entity2.getPrazoOS()));
				}
				receber.setValorRec(CalculaParcela.calculaParcelas(entity2.getValorOS(), entity2.getParcelaOS(), i));
				receber.setNumeroRec(null);
				recService.insert(receber);
			}
		} catch (ParseException p) {
			Alerts.showAlert("Erro salvando objeto", classe, p.getMessage(), AlertType.ERROR);
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void createReposicao(OrdemServico entity2, Orcamento orcamento) {
		try {
			classe = "Reposição";
			ReposicaoVeiculo reposicao = new ReposicaoVeiculo();
			reposicao.setOsRep(entity2.getNumeroOS());
			if (reposicao.getOsRep() != null) {
				repService.remove(reposicao);
			}
			List<OrcVirtual> listVir = virService.findAll();
			for (int i = 0; i < listVir.size(); i++) {
				if (listVir.get(i).getNumeroOrcVir().equals(entity2.getOrcamentoOS())) {
					if (listVir.get(i).getMaterial().getVidaKmMat() > 0
							|| listVir.get(i).getMaterial().getVidaMesMat() > 0) {
						reposicao.setNumeroRep(null);
						reposicao.setOsRep(entity2.getNumeroOS());
						reposicao.setDataRep(entity2.getDataOS());
						reposicao.setPlacaRep(entity2.getPlacaOS());
						reposicao.setClienteRep(entity2.getClienteOS());
						reposicao.setDddClienteRep(orcamento.getCliente().getDdd01Cli());
						reposicao.setTelefoneClienteRep(orcamento.getCliente().getTelefone01Cli());
						reposicao.setCodigoMaterialRep(listVir.get(i).getMaterial().getCodigoMat());
						reposicao.setMaterialRep(listVir.get(i).getMaterial().getNomeMat());
						reposicao.setKmTrocaRep(orcamento.getKmFinalOrc());
						if (listVir.get(i).getMaterial().getVidaKmMat() > 0) {
							reposicao.setProximaKmRep(
									orcamento.getKmFinalOrc() + listVir.get(i).getMaterial().getVidaKmMat());
						} else {
							reposicao.setProximaKmRep(0);
						}
						if (listVir.get(i).getMaterial().getVidaMesMat() > 0) {
							reposicao.setProximaDataRep(CalculaParcela.CalculaVencimentoMes(entity2.getDataOS(),
									listVir.get(i).getMaterial().getVidaMesMat()));
						} else {
							reposicao.setProximaDataRep(entity2.getDataOS());
						}
						repService.insert(reposicao);
					}
				}
			}
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void updateOsOrcamento(OrdemServico entity2, Orcamento orcamento) {
		try {
			classe = "Orçamento";
			orcamento.setOsOrc(entity2.getNumeroOS());
			orcService.saveOrUpdate(orcamento);
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void updateKmVeiculo(OrdemServico entity2) {
		try {
			classe = "Veiculo";
			veiculo = veiService.findByPlaca(entity2.getPlacaOS());
			if (veiculo.getPlacaVei() == entity2.getPlacaOS()) {
				veiculo.setKmInicialVei(veiculo.getKmFinalVei());
				veiculo.setKmFinalVei(entity2.getKmOS());
			} else {
				veiculo.setPlacaVei(entity2.getPlacaOS());
				veiculo.setKmInicialVei(entity2.getKmOS());
				veiculo.setKmFinalVei(entity2.getKmOS());
				veiculo.setModeloVei(null);
				veiculo.setAnoVei(0);
			}
			veiService.saveOrUpdate(veiculo);
		} catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private void gravaComissaoOS(OrdemServico entity2) {
		classe = "Adiantamento ";
		orcamento = orcService.findById(entity2.getOrcamentoOS());
		funcionario = funService.findById(orcamento.getFuncionario().getCodigoFun());
		adiantamento = new Adiantamento();
		adiantamento.setDataAdi(entity2.getDataOS());
		adiantamento.setCodFunAdi(funcionario.getCodigoFun());
		adiantamento.setNomeFunAdi(funcionario.getNomeFun());
		adiantamento.setCargoAdi(funcionario.getCargoFun());
		adiantamento.setSituacaoAdi(funcionario.getSituacaoFun());
		adiantamento.setValorAdi(0.00);
		adiantamento.setComissaoAdi(0.0);
		Calendar cal = Calendar.getInstance();
		cal.setTime(adiantamento.getDataAdi());
		adiantamento.setMesAdi(cal.get(Calendar.MONTH) + 1);
		adiantamento.setAnoAdi(cal.get(Calendar.YEAR));
		adiantamento.setOsAdi(entity2.getNumeroOS());
		adiantamento.setBalcaoAdi(0);
		adiantamento.setTipoAdi("C");
		adiantamento.setFuncionario(funcionario);
		adiantamento.setCargo(funcionario.getCargo());
		adiantamento.setSituacao(funcionario.getSituacao());
		adiantamento.calculaComissao(maoObra);
		adiService.saveOrUpdate(adiantamento);
	}

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

	/*
	 * criamos um obj vazio (obj), chamo codigo (em string) e transformamos em int
	 * (la no util) se codigo for nulo insere, se não for atz tb verificamos se cpos
	 * obrigatórios estão preenchidos, para informar erro(s) para cpos string não
	 * precisa tryParse
	 */
	private OrdemServico getFormData() throws ParseException {
		OrdemServico obj = new OrdemServico();
		// instanciando uma exceção, mas não lançado - validation exc....
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setNumeroOS(Utils.tryParseToInt(textNumeroOS.getText()));
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro
		if (dpDataOS.getValue() != null) {
			Instant instant = Instant.from(dpDataOS.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataOS(Date.from(instant));
		}
		if (dpDataOS.getValue() == null) {
			exception.addErros("dataOS", "Data é obrigatória");
		}

		obj.setOrcamento(comboBoxOrcamento.getValue());
		obj.setOrcamentoOS(obj.getOrcamento().getNumeroOrc());
		obj.setPlacaOS(obj.getOrcamento().getPlacaOrc());
		obj.setClienteOS(obj.orcamento.getClienteOrc());
		obj.setValorOS(obj.getOrcamento().getTotalOrc());
		if (obj.getNumeroOS() != null) {
			exception.addErros("dataOS", "Orçamento Fechado");
			exception.addErros("orcto", "Sem acesso");
			exception.addErros("parcela", "Ordem de Serviço Nº: " + obj.getNumeroOS());
		}

		if (obj.getOrcamento().getFuncionario().getComissaoFun() != null) {
			flagCom = 1;
		}
		obj.setOrcamentoOS(comboBoxOrcamento.getValue().getNumeroOrc());
		obj.setPlacaOS(comboBoxOrcamento.getValue().getPlacaOrc());
		obj.setClienteOS(comboBoxOrcamento.getValue().getClienteOrc());
		obj.setValorOS(comboBoxOrcamento.getValue().getTotalOrc());
		obj.setKmOS(comboBoxOrcamento.getValue().getKmFinalOrc());

		int flag1 = 0;
		if (rbParcelaUnica.isSelected()) {
			obj.setParcelaOS(1);
			flag1 += 1;
		}
		if (rbParcela02.isSelected()) {
			obj.setParcelaOS(2);
			flag1 += 1;
		}
		if (rbParcela03.isSelected()) {
			obj.setParcelaOS(3);
			flag1 += 1;
		}
		if (obj.getParcelaOS() == null) {
			exception.addErros("parcela", "Parcela obrigatória");
		}
		if (flag1 > 1) {
			exception.addErros("parcela", "Só pode uma opção");
		}

		int flag2 = 0;
		if (rbPrazoAvista.isSelected()) {
			obj.setPrazoOS(1);
			flag2 += 1;
		}
		if (rbPrazo10.isSelected()) {
			obj.setPrazoOS(10);
			flag2 += 1;
		}
		if (rbPrazo15.isSelected()) {
			obj.setPrazoOS(15);
			flag2 += 1;
		}
		if (rbPrazo30.isSelected()) {
			obj.setPrazoOS(30);
			flag2 += 1;
		}
		if (obj.getPrazoOS() == null) {
			exception.addErros("prazo", "Prazo obrigatório");
		}
		if (flag2 > 1) {
			exception.addErros("prazo", "Só pode uma opção");
		}
		if (obj.getParcelaOS() != null) {
			if (obj.getParcelaOS() > 1 && obj.getPrazoOS() == 1) {
				exception.addErros("prazo", "Prazo incompatível");
			}
		}

		int flag3 = 0;
		if (rbPagamentoDinheiro.isSelected()) {
			obj.setPagamentoOS(1);
			flag3 += 1;
		}
		if (rbPagamentoPix.isSelected()) {
			obj.setPagamentoOS(2);
			flag3 += 1;
		}
		if (rbPagamentoDebito.isSelected()) {
			obj.setPagamentoOS(3);
			flag3 += 1;
		}
		if (rbPagamentoCC.isSelected()) {
			obj.setPagamentoOS(4);
			flag3 += 1;
		}
		if (obj.getPagamentoOS() == null) {
			exception.addErros("pagamento", "Pagamento obrigatório");
		}
		if (flag3 > 1) {
			exception.addErros("pagamento", "Só pode uma opção");
		}
		if (obj.getPagamentoOS() != null) {
			if (obj.getPagamentoOS() == 4 && obj.getPrazoOS() > 1) {
				exception.addErros("pagamento", "CC prazo é igual a vista");
			}
		}

		if (dpData1oOS.getValue() != null) {
			Instant instant = Instant.from(dpData1oOS.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataPrimeiroPagamentoOS(Date.from(instant));
		}
		if (dpData1oOS.getValue() == null) {
			exception.addErros("data1o", "Data é obrigatória");
		}
		if (obj.getDataPrimeiroPagamentoOS().before(obj.getDataOS())) {
			exception.addErros("data1o", "Data menor que Data da OS");
		}

		if (textNnfOS.getText() == null || textNnfOS.getText().trim().contentEquals("")) {
			obj.setNnfOS(0);
		} else {
			obj.setNnfOS(Utils.tryParseToInt(textNnfOS.getText()));
		}

		if (ultimaNF == 0 && obj.getNnfOS() > 0 && obj.getNumeroOS() != null) {
			List<NotaFiscal> nf = nfService.findAll();
			if (nf != null) {
				for (NotaFiscal n : nf) {
					if (n.getNumeroNF() > ultimaNF) {
						ultimaNF = n.getNumeroNF();
					}
				}
				if (obj.getNnfOS() <= ultimaNF) {
					exception.addErros("nf", "NF menor ou igual a última");
				}
			}
			nota.setCodigoNF(null);
			nota.setNumeroNF(obj.getNnfOS());
			nota.setOsNF(obj.getNumeroOS());
			nota.setOsNF(0);
		}

		obj.setObservacaoOS(textObservacaoOS.getText());

		Calendar cal = Calendar.getInstance();
		cal.setTime(obj.getDataOS());
		obj.setMesOs(cal.get(Calendar.MONTH) + 1);
		obj.setAnoOs(cal.get(Calendar.YEAR));

		// tst se houve algum (erro com size > 0)
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

	// msm processo save p/ fechar
	@FXML
	public void onBtCancelOSAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/*
	 * o contrainsts (confere) impede alfa em cpo numerico e delimita tamanho
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textNumeroOS);
		Utils.formatDatePicker(dpDataOS, "dd/MM/yyyy");
		Utils.formatDatePicker(dpData1oOS, "dd/MM/yyyy");
		Constraints.setTextFieldInteger(textNnfOS);
		Constraints.setTextFieldMaxLength(textObservacaoOS, 60);
		Constraints.setTextFieldMaxLength(textNnfOS, 06);
		Constraints.setTextFieldMaxLength(textPesquisa, 7);
		initializeComboBoxOrcamento();
	}

	private void initializeComboBoxOrcamento() {
		Callback<ListView<Orcamento>, ListCell<Orcamento>> factory = lv -> new ListCell<Orcamento>() {
			@Override
			protected void updateItem(Orcamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getPlacaOrc() + " - " + item.getClienteOrc());
			}
		};

		comboBoxOrcamento.setCellFactory(factory);
		comboBoxOrcamento.setButtonCell(factory.call(null));
	}

//	carrega dados do bco cargo dentro obslist via
	public void loadAssociatedObjects() {
		if (orcService == null) {
			throw new IllegalStateException("Orçamento Serviço esta nulo");
		}
// buscando (carregando) bco de dados		
		List<Orcamento> listOrc = orcService.findAll();
// transf p/ obslist		
		obsListOrc = FXCollections.observableArrayList(listOrc);
		comboBoxOrcamento.setItems(obsListOrc);
	}

	public void updateFormData() throws ParseException {
		if (entity == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		labelUser.setText(user);
//  string value of p/ casting int p/ string 		
		textNumeroOS.setText(String.valueOf(entity.getNumeroOS()));
		if (entity.getDataOS() == null) {
			entity.setDataOS(new Date());
		}
		if (entity.getDataOS() != null) {
			dpDataOS.setValue(LocalDate.ofInstant(entity.getDataOS().toInstant(), ZoneId.systemDefault()));
		}

		if (entity.getOrcamento() == null) {
			comboBoxOrcamento.getSelectionModel().selectFirst();
		} else {
			comboBoxOrcamento.setValue(entity.getOrcamento());
		}

		rbParcelaUnica.setSelected(false);
		rbParcela02.setSelected(false);
		rbParcela03.setSelected(false);
		if (entity.getParcelaOS() != null) {
			if (entity.getParcelaOS() == 1) {
				rbParcelaUnica.setSelected(true);
			} else {
				if (entity.getParcelaOS() == 2) {
					rbParcela02.setSelected(true);
				} else {
					if (entity.getParcelaOS() == 3) {
						rbParcela03.setSelected(true);
					}
				}
			}
		}
		rbPrazoAvista.setSelected(false);
		rbPrazo10.setSelected(false);
		rbPrazo15.setSelected(false);
		rbPrazo30.setSelected(false);
		if (entity.getPrazoOS() != null) {
			if (entity.getPrazoOS() == 1) {
				rbPrazoAvista.setSelected(true);
			} else {
				if (entity.getPrazoOS() == 10) {
					rbPrazo10.setSelected(true);
				} else {
					if (entity.getPrazoOS() == 15) {
						rbPrazo15.setSelected(true);
					} else {
						if (entity.getPrazoOS() == 30) {
							rbPrazo30.setSelected(true);
						}
					}
				}
			}
		}
		rbPagamentoDinheiro.setSelected(false);
		rbPagamentoPix.setSelected(false);
		rbPagamentoDebito.setSelected(false);
		rbPagamentoCC.setSelected(false);
		if (entity.getPagamentoOS() != null) {
			if (entity.getPagamentoOS() == 1) {
				rbPagamentoDinheiro.setSelected(true);
			} else {
				if (entity.getPagamentoOS() == 2) {
					rbPagamentoPix.setSelected(true);
				} else {
					if (entity.getPagamentoOS() == 3) {
						rbPagamentoDebito.setSelected(true);
					} else {
						if (entity.getPagamentoOS() == 4) {
							rbPagamentoCC.setSelected(true);
						}
					}
				}
			}
		}
		if (entity.getDataPrimeiroPagamentoOS() == null) {
			entity.setDataPrimeiroPagamentoOS(entity.getDataOS());
		}
		dpData1oOS
				.setValue(LocalDate.ofInstant(entity.getDataPrimeiroPagamentoOS().toInstant(), ZoneId.systemDefault()));

		textNnfOS.setText(String.valueOf(entity.getNnfOS()));
		textObservacaoOS.setText(entity.getObservacaoOS());
		textPesquisa.setText(pesquisa);
	}

// mandando a msg de erro para o labelErro correspondente 	
	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		labelErrorDataOS.setText((fields.contains("dataOS") ? erros.get("dataOS") : ""));
		labelErrorOrcamentoOS.setText((fields.contains("orcto") ? erros.get("orcto") : ""));
		labelErrorParcelaOS.setText((fields.contains("parcela") ? erros.get("parcela") : ""));
		labelErrorPrazoOS.setText((fields.contains("prazo") ? erros.get("prazo") : ""));
		labelErrorPagamentoOS.setText((fields.contains("pagamento") ? erros.get("pagamento") : ""));
		labelErrorData1oOS.setText((fields.contains("data1o") ? erros.get("data1o") : ""));
	}

	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub
	}
}
