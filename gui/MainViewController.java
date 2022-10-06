package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.listerneres.DataChangeListener;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.services.CompromissoService;
import model.services.FornecedorService;
import model.services.ParPeriodoService;
import model.services.ParcelaService;
import model.services.TipoFornecedorService;
import sgo.gui.AdiantamentoListController;
import sgo.gui.BalcaoListController;
import sgo.gui.CargoListController;
import sgo.gui.ClienteListController;
import sgo.gui.ComissaoListController;
import sgo.gui.DadosFechamentoListController;
import sgo.gui.DadosFolhaListController;
import sgo.gui.EntradaListController;
import sgo.gui.FuncionarioListController;
import sgo.gui.GrupoListController;
import sgo.gui.LoginFormController;
import sgo.gui.MaterialListController;
import sgo.gui.OrcamentoListController;
import sgo.gui.OrdemServicoListController;
import sgo.gui.ReceberListAbertoController;
import sgo.gui.ReceberListPagoController;
import sgo.gui.ReceberRelatorioAbertoController;
import sgo.gui.ReceberRelatorioPagoController;
import sgo.gui.ReposicaoVeiculoListController;
import sgo.gui.UltimasVisitasListController;
import sgo.model.entities.Login;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.BalcaoService;
import sgo.model.services.CargoService;
import sgo.model.services.ClienteService;
import sgo.model.services.DadosFechamentoService;
import sgo.model.services.DadosFolhaMesService;
import sgo.model.services.EntradaService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.GrupoService;
import sgo.model.services.LoginService;
import sgo.model.services.MaterialService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.OrcamentoService;
import sgo.model.services.OrdemServicoService;
import sgo.model.services.ReceberService;
import sgo.model.services.ReposicaoVeiculoService;

public class MainViewController implements Initializable, DataChangeListener {
 
	@FXML
	private MenuItem menuItemFornecedor;

	@FXML
	private MenuItem menuItemCompromisso;

	@FXML
	private MenuItem menuItemTipoFornecedor;

	@FXML
	private MenuItem menuItemCliente;

	@FXML
	private MenuItem menuItemGrupo;

	@FXML
	private MenuItem menuItemMaterial;

	@FXML
	private MenuItem menuItemCargo;
	
	@FXML
	private MenuItem menuItemFuncionario;
	
	@FXML
	private MenuItem menuItemAdiantamento;
	
	@FXML
	private MenuItem menuItemEntrada;
	
	@FXML
	private MenuItem menuItemOrcamento;
	
	@FXML
	private MenuItem menuItemOrdemServico;
	
	@FXML
	private MenuItem menuItemBalcao;
	
	@FXML
	private MenuItem menuItemRelatorioParcelaAberto;

	@FXML
	private MenuItem menuItemRelatorioParcelaPago;

	@FXML
	private MenuItem menuItemRelatorioReceber;

	@FXML
	private MenuItem menuItemRelatorioRecebido;

	@FXML
	private MenuItem menuItemConsultaAberto;

	@FXML
	private MenuItem menuItemConsultaPago;

	@FXML
	private MenuItem menuItemConsultaReceber;
	
	@FXML
	private MenuItem menuItemConsultaRecebido;
	
	@FXML
	private MenuItem menuItemConsultaReposicao;
	
	@FXML
	private MenuItem menuItemConsultaVisitas;
	
	@FXML
	private MenuItem menuItemConsultaComissao;
	
	@FXML
	private MenuItem menuItemConsultaDadosFolha;
	
	@FXML
	private MenuItem menuItemConsultaDadosFechamento;
	
	@FXML
	private MenuItem menuItemSobre;

	@FXML
	private Button btLogin;

	@FXML
	private Label labelUser;

//auxiliares	
	String classe = "";
	public static String senha = "null";
	public static int nivel = 0;
	public static String user = "usuário";	
//código da empresa
//  1 = Easy; 2 = WS........	
	public static int numEmp = 2;
	
	@FXML
	private void onBtLoginAction() {
		classe = "Login ";
		Login log = new Login();
		senha = "null";
		createDialogForm(log, "/sgo/gui/LoginForm.fxml");
		if (senha == "null") {
			temLogar();
		}
		initializeNodes();
	}
	
	/*
	 * função e inicialização do FornecedorController antes na loadview 2 -
	 * expressão lambda inicialização como parametro da função loadView aqui
	 */
	@FXML
	public void onMenuItemFornecedorAction() {
		classe = "Fornecedor ";
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/gui/FornecedorList.fxml", (FornecedorListController controller) -> {
			controller.user = user;
			controller.setFornecedorService(new FornecedorService());
			controller.updateTableView();
			// view2 p/ funcionar mock
		});}
	}
	
	// criou um expressão lambda como par ametros para atz tableview =>
	// initializingAction
	@FXML
	public void onMenuItemCompromissoAction() {
		classe = "Compromisso ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/gui/CompromissoList.fxml", (CompromissoListController controller) -> {
					controller.user = user;
					controller.setCompromissoService(new CompromissoService());
					controller.setFornecedorService(new FornecedorService());
					controller.setParcelaService(new ParcelaService());
					controller.updateTableView();
		});
			}
		}	
	}

	@FXML
	public void onMenuItemTipoFornecedorAction() {
		classe = "Tipo Fornecedor ";
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/gui/TipoFornecedorList.fxml", (TipoFornecedorListController controller) -> {
			controller.user = user;
			controller.setTipoFornecedorService(new TipoFornecedorService());
			controller.updateTableView();
			// view2 p/ funcionar mock
		});}
	}

	@FXML
	public void onMenuItemClienteAction() {
		classe = "Cliente ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/sgo/gui/ClientList.fxml",  (ClienteListController controller) -> {
					controller.user = user;
					controller.setClienteService(new ClienteService());
					controller.updateTableView();
		});
			}
		}	
	}

	@FXML
	public void onMenuItemGrupoAction() {
		classe = "Grupo ";
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/sgo/gui/GrupoList.fxml", (GrupoListController controller) -> {
			controller.user = user;
			controller.setGrupoService(new GrupoService());
			controller.updateTableView();
		});}
	}
 
	@FXML
	public void onMenuItemMaterialAction() {
		classe = "Material ";
		if (senha != "Ok") {
			temLogar();
		} else {	
 		loadView("/sgo/gui/MaterialList.fxml", (MaterialListController controller) -> {
			controller.user = user;
  			controller.setMaterialService(new MaterialService());
  			controller.setGrupoService(new GrupoService());
   			controller.updateTableView();
		});}
	}
 
	@FXML
	public void onMenuItemCargoAction() {
		classe = "Cargo ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/sgo/gui/CargoList.fxml", (CargoListController controller) -> {
					controller.user = user;
					controller.setCargoService(new CargoService());
					controller.updateTableView();
		});
			}
		}	
	}
 
	@FXML
	public void onMenuItemFuncionarioAction() {
		classe = "Funcionario ";
		if (senha != "Ok") {
			temLogar();
		} else {	
 		loadView("/sgo/gui/FuncionarioList.fxml", (FuncionarioListController controller) -> {
			controller.user = user;
  			controller.setFuncionarioService(new FuncionarioService());
   			controller.updateTableView();
		});}
	}
  
	@FXML
	public void onMenuItemAdiantamentoAction() {
		classe = "Adiantamento ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/sgo/gui/AdiantamentoList.fxml", (AdiantamentoListController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setAdiantamentoService(new AdiantamentoService());
					controller.setTipo("A");
					controller.updateTableView();
		});
			}
		}	
	}
  
	@FXML
	public void onMenuItemEntradaAction() {
		classe = "Entrada ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/sgo/gui/EntradaList.fxml", (EntradaListController controller) -> {
					controller.user = user;
					controller.setMaterialService(new MaterialService());
					controller.setFornecedorService(new FornecedorService());
					controller.setEntradaService(new EntradaService());
					controller.setGrupoService(new GrupoService());
					controller.updateTableView();
		});
			}
		}	
	}
  
	@FXML
	public void onMenuItemOrcamentoAction() {
		classe = "Orçamento ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/sgo/gui/OrcamentoList.fxml", (OrcamentoListController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setOrcamentoService(new OrcamentoService());
					controller.updateTableView();
		});
			}
		}	
	}
  
	@FXML
	public void onMenuItemOrdemServicoAction() {
		classe = "OS";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/sgo/gui/OrdemServicoList.fxml", (OrdemServicoListController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setOrdemServicoService(new OrdemServicoService());
					controller.updateTableView();
		});
			}
		}	
	}
  
	@FXML
	public void onMenuItemBalcaoAction() {
		classe = "Balcão";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			loadView("/sgo/gui/BalcaoList.fxml", (BalcaoListController controller) -> {
				controller.user = user;
				controller.numEmp = numEmp;
				controller.setServices(new BalcaoService(), 
									   new MaterialService(),
									   new OrcVirtualService(),
									   new ReceberService(),
									   new AdiantamentoService());
				controller.updateTableView();
			});
		}	
	}
  
	@FXML  
	public void onMenuItemRelatorioParcelaAbertoAction() {
		classe = "Compromisso Aberto ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/ParcelaPrintAberto.fxml", (ParcelaPrintAbertoController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setFornecedorService(new FornecedorService());
					controller.setParPeriodoService(new ParPeriodoService());
					controller.setParcelaService(new ParcelaService());
					controller.updateTableViewAberto();
		}); 
			}
		}	
	}

	@FXML
	public void onMenuItemRelatorioParcelaPagoAction() {
		classe = "Compromisso Pago ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/ParcelaPrintPago.fxml", (ParcelaPrintPagoController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setFornecedorService(new FornecedorService());
					controller.setParPeriodoService(new ParPeriodoService());
					controller.setParcelaService(new ParcelaService());
					controller.updateTableViewPago();
		});
			}
		}
	}

	@FXML  
	public void onMenuItemRelatorioReceberAction() {
		classe = "Lst Receber ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/sgo/gui/ReceberRelatorioAberto.fxml", (ReceberRelatorioAbertoController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setReceberService(new ReceberService());
					controller.updateTableViewAberto();
		});
			}
		}	
	}

	@FXML  
	public void onMenuItemRelatorioRecebidoAction() {
		classe = "Lst Recebido ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/sgo/gui/ReceberRelatorioPago.fxml", (ReceberRelatorioPagoController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.opcao = 't';
					controller.setReceberService(new ReceberService());
					controller.updateTableViewPago();
		});
			}
		}	
	}

	@FXML
	public void onMenuItemConsultaAbertoAction() {
		classe = "Contas a Pagar ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/ParcelaListAberto.fxml", (ParcelaListAbertoController controller) -> {
					controller.user = user;
					controller.setFornecedorService(new FornecedorService());
					controller.setParPeriodoService(new ParPeriodoService());
					controller.setParcelaService(new ParcelaService());
					controller.updateTableViewAberto();
		});
			}
		}	
	}

	@FXML
	public void onMenuItemConsultaPagoAction() {
		classe = "Contas Pagas ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/ParcelaListPago.fxml", (ParcelaListPagoController controller) -> {
					controller.user = user;
					controller.setFornecedorService(new FornecedorService());
					controller.setParPeriodoService(new ParPeriodoService());
					controller.setParcelaService(new ParcelaService());
					controller.updateTableViewPago();
		});
			}
		}	
	}

	@FXML
	public void onMenuItemConsultaReceberAction() {
		classe = "Con Receber";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/sgo/gui/ReceberListAberto.fxml", (ReceberListAbertoController controller) -> {
					controller.user = user;
					controller.setReceberService(new ReceberService());
					controller.updateTableView();
		});
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaRecebidoAction() {
		classe = "Con Recebido ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/sgo/gui/ReceberListPago.fxml", (ReceberListPagoController controller) -> {
					controller.user = user;
					controller.setReceberService(new ReceberService());
					controller.updateTableView();
		});
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaReposicaoAction() {
		classe = "Con Reposição ";
		if (senha != "Ok") {
			temLogar();
		} else {	
 		loadView("/sgo/gui/ReposicaoVeiculoList.fxml", (ReposicaoVeiculoListController contRV) -> {
			contRV.user = user;
  			contRV.setReposicaoVeiculoService(new ReposicaoVeiculoService());
   			contRV.updateTableView();
		});}
	}
  
	@FXML
	public void onMenuItemConsultaVisitasAction() {
		classe = "Con Visitas ";
		if (senha != "Ok") {
			temLogar();
		} else {	
 		loadView("/sgo/gui/UltimasVisitasList.fxml", (UltimasVisitasListController contUV) -> {
			contUV.user = user;
  			contUV.setOrdemServicoService(new OrdemServicoService());
   			contUV.updateTableView();
		});}
	}
  
	@FXML
	public void onMenuItemConsultaComissaoAction() {
		classe = "Con Adiantamento ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/sgo/gui/ComissaoList.fxml", (ComissaoListController controller) -> {
					controller.user = user;
					controller.setServices(new AdiantamentoService(),
										   new BalcaoService());
					controller.updateTableView();
		});
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaDadosFolhaAction() {
		classe = "Con DadosFolha ";
		classe = "Con Adiantamento ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/sgo/gui/DadosFolhaList.fxml", (DadosFolhaListController controller) -> {
					controller.user = user;
					controller.setServices(new DadosFolhaMesService());
		});
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaDadosFechamentoAction() {
		classe = "Con Dados Fechamnto ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/sgo/gui/DadosFechamentoList.fxml", (DadosFechamentoListController controller) -> {
					controller.user = user;
					controller.setServices(new DadosFechamentoService());
		});
			}
		}	
	}
  
	// obrigatoria função x , sem nada; mesmo sem "nada" repassado para atender
	// parametros <consumer> do loadView
	@FXML
	public void onMenuItemSobreAction() {
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/gui/Sobre.fxml", x -> {
		});}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		labelUser.setText(user);
	}

	private void temLogar() {
		Alerts.showAlert("Erro login!!!", null, "Tem que logar ", AlertType.ERROR);
	}

	/*
	 * interface consumer <T>, passa a ser função 
	 * generica synchronized garante processo inteiro sem interrupção
	 */
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
//	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

// busca o conteudo da variavel no Main via metodo getScene			
			Scene mainScene = Main.getMainScene();
			/*
			 * os filhos da janela Vbox (sobre) inseridos no scrollpane casting vbox
			 * scrollpane trazendo o primeiro reg content getRoot pega o primeiro elemento
			 * da view principal - scrollpane casting scrollpane - getContent
			 */
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

//	busca o primeiro children do vbox da janela principal			
			Node mainMenu = mainVBox.getChildren().get(0);
// limpa os filhos do main vbox
			mainVBox.getChildren().clear();
// adiciona o mainMenu			
			mainVBox.getChildren().add(mainMenu);
// adiciona os filhos do newVbox
			mainVBox.getChildren().addAll(newVBox.getChildren());

// ativar essa função ; retorna o controlador <T> do tipo chamado aqui em cima 
//			na função(Fornecedor, compromis....			
			T controller = loader.getController();
// para executar a ação -> função accept do consumer			
			initializingAction.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", classe + "Erro carregando a página", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void createDialogForm(Login log, String absoluteName) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			LoginFormController controller = loader.getController();
// injetando passando parametro obj 			
			controller.setLogin(log);
			controller.numEmp = numEmp;
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setLoginService(new LoginService());
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListenerMain(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Logar                                             ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
//		catch (ParseException p) {
//			p.printStackTrace();
//		}
 	}

	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub
		
	}
}