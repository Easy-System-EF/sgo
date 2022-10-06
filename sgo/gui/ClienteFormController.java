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
import gui.util.Constraints;
import gui.util.CpfCnpj;
import gui.util.Mascaras;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import model.exception.ValidationException;
import sgo.model.entities.Cliente;
import sgo.model.services.ClienteService;

public class ClienteFormController implements Initializable {

	private Cliente entity;
	
/*
 *  dependencia service com metodo set
 */
	private ClienteService service;

// lista da classe subject (form) - guarda lista de obj p/ receber e emitir o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField textCodigoCli;
	
	@FXML
	private TextField textNomeCli;
	
	@FXML
	private TextField textRuaCli;
	
	@FXML
	private TextField textComplementoCli;
	
	@FXML
	private TextField textNumeroCli;
	
	@FXML
	private TextField textCepCli;
	
	@FXML
	private TextField textBairroCli;
	
	@FXML
	private TextField textCidadeCli;
	
	@FXML
	private TextField textUfCli;
	
	@FXML
	private TextField textDdd01Cli;
	
	@FXML
	private TextField textTelefone01Cli;
	
	@FXML
	private TextField textDdd02Cli;
	
	@FXML
	private TextField textTelefone02Cli;
	
	@FXML
	private Label lbPessoa;
	
 	@FXML
 	private RadioButton rbFisica;
 	
 	@FXML
 	private RadioButton rbJuridica;
 	
	@FXML
	private TextField textCpfCli;
	
	@FXML
	private TextField textCnpjCli;
	
 	@FXML
	private TextField textEmailCli;

  	@FXML
	private Button btSaveCli;
	
	@FXML
	private Button btCancelCli;
	
	@FXML
	private Label labelErrorNomeCli;

	@FXML
	private Label labelErrorRuaCli; 
	
	@FXML
	private Label labelErrorBairroCli; 
	
	@FXML
	private Label labelErrorCidadeCli;
	
	@FXML
	private Label labelErrorUFCli; 

	@FXML
	private Label labelErrorTelefone01Cli; 

	@FXML
	private Label labelErrorEmailCli; 

	@FXML
	private Label labelErrorPessoaCli; 

	@FXML
	private Label labelErrorCpfCli; 

	@FXML
	private Label labelErrorCnpjCli; 

	@FXML
	private Label labelUser;

	String classe = "Cliente ";
	public String user = "usuário";	
	
// flag p/ verificar UFCli	
	int flag = 0;
	Integer codAnt = 999999;
 
 	public void setCliente(Cliente entity) {
		this.entity = entity;
	}

 // 	 * metodo set /p service
 	public void setClienteService(ClienteService service) {
		this.service = service;
	}
	
//  * o controlador tem uma lista de eventos q permite distribuição via metodo abaixo
// * recebe o evento e inscreve na lista
 	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

/* 
 * vamos instanciar um forn e salvar no bco de dados
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
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null)
		{	throw new IllegalStateException("Entidade nula");
		}
		if (service == null)
		{	throw new IllegalStateException("Serviço nulo");
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
		catch (ParseException p) {
			p.printStackTrace();
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
	private Cliente getFormData() throws ParseException {
		Cliente obj = new Cliente();
		List<Cliente> listCli = new ArrayList<>();
 // instanciando uma exceção, mas não lançado - validation exc....		
		ValidationException exception = new ValidationException("Validation exception");
// set CODIGO c/ utils p/ transf string em int \\ ou null		
		obj.setCodigoCli(Utils.tryParseToInt(textCodigoCli.getText()));
// tst name (trim elimina branco no principio ou final
// lança Erros - nome do cpo e msg de erro
		if (textNomeCli.getText() == null || textNomeCli.getText().trim().contentEquals("")) {
			exception.addErros("nome", "Nome é obrigatório");
		} else {			
			obj.setNomeCli(textNomeCli.getText());
			if (textNomeCli.getText().length() < 3) {
				exception.addErros("nome", "Nome muito curto");
			}	
		}	
			
		if (textRuaCli.getText() == null || textRuaCli.getText().trim().contentEquals("")) {
			exception.addErros("rua", "Rua é obrigatório");
		}
		else {
			obj.setRuaCli(textRuaCli.getText());
			if (textRuaCli.getText().length() < 3) {
				exception.addErros("rua", "Rua muito curta");
			}	
		}				
			
   		obj.setNumeroCli(Utils.tryParseToInt(textNumeroCli.getText()));

   		obj.setComplementoCli(textComplementoCli.getText());

   		String cep = "";
		if (textCepCli.getText() != null)
		{	cep = textCepCli.getText().replace("-", "");
 	 		obj.setCepCli(cep);
			if (textCepCli.getText().length() < 8) {
				exception.addErros("cep", "Cep inválido");
			}	
 		}
		
		
 		if (textBairroCli.getText() == null || textBairroCli.getText().trim().contentEquals("")) {
			exception.addErros("bairro", "Bairro é obrigatório");
		}
 		else {
 			obj.setBairroCli(textBairroCli.getText());
			if (textBairroCli.getText().length() < 4) {
				exception.addErros("bairro", "Bairro muito curto");
			}	
 		}	
 			
		if (textCidadeCli.getText() == null || textCidadeCli.getText().trim().contentEquals("")) {
			exception.addErros("cidade", "Cidade é obrigatório");
		}
		else {
			obj.setCidadeCli(textCidadeCli.getText());
			if (textCidadeCli.getText().length() < 4) {
				exception.addErros("cidade", "Cidade muito curta");
			}	
		}	
			
		if (textUfCli.getText() == null || textUfCli.getText().trim().contentEquals("")) {
			exception.addErros("uf", "UF é obrigatório");
		}
 		obj.setUfCli(textUfCli.getText());
// verifica se UFCli existe
  		if (obj.getUfCli() != null) {
  			verificaUfCli(obj);
  		}
		if (obj.getUfCli()  == "XX") {
			exception.addErros("uf", "UF não existe ");
		}
		
		if (textDdd01Cli.getText() == null || textTelefone01Cli.getText().trim().contentEquals("")) {
  			exception.addErros("telefone01", "1º DDD é obrigatório");
   		}
   		obj.setDdd01Cli(Utils.tryParseToInt(textDdd01Cli.getText()));
		if (textDdd01Cli.getText() != null) {
			if (textDdd01Cli.getText().length() < 2) {
   	  			exception.addErros("telefone01", "DDD inválido");
			}	
   		}
   		
   		if (textTelefone01Cli.getText() == null || textTelefone01Cli.getText().trim().contentEquals("")) {
  			exception.addErros("telefone01", "1º Telefone é obrigatório");
		}	
		obj.setTelefone01Cli(Utils.tryParseToInt(textTelefone01Cli.getText()));
   		if (obj.getTelefone01Cli() != null) { 
			if (obj.getTelefone01Cli() < 20000000) {
				exception.addErros("telefone01", "Número telefone não existe");
			}
   		}
		
  		obj.setDdd02Cli(Utils.tryParseToInt(textDdd02Cli.getText()));
  		
  		obj.setTelefone02Cli(Utils.tryParseToInt(textTelefone02Cli.getText()));
 		
  		obj.setEmailCli(textEmailCli.getText());
 		
 		if (rbFisica.isSelected())
 		{ 	obj.setPessoaCli('F');
 		}
 		if (rbJuridica.isSelected())
 		{	obj.setPessoaCli('J');	
 		}
 		if (obj.getPessoaCli() != 'F' && obj.getPessoaCli() != 'J') 
		{	exception.addErros("pessoa", "Identificação Pessoa obrigatória");
		}
		if (rbFisica.isSelected() && rbJuridica.isSelected()) 
		{	exception.addErros("pessoa", "Ou Física ou Jurídica?");
			rbFisica.setSelected(false);
			rbJuridica.setSelected(false);
			obj.setPessoaCli(' ');
		}
		
//	 	//validação cpf / cnpj  	 	

 		if (rbFisica.isSelected()) {
 			if (textCpfCli.getText() == null || textCpfCli.getText().trim().contentEquals("")) { 
 				exception.addErros("cpf", "Cpf é obrigatório");
 			}	else {
 					if (textCpfCli.getText() != null) {
 						if (textCpfCli.getText().length() < 11) {
 							exception.addErros("cpf", "Cpf incompleto");
 						}
 					}
 				}		
 				obj.setCpfCli(textCpfCli.getText());
 				if (obj.getPessoaCli() == 'F') {
 					obj.getCpfCli().replace(".", "");
 					obj.getCpfCli().replace("-", "");
 					String [] cpfV = new String[11];
 					for (int i = 0 ; i < cpfV.length ; i ++ ) {
 						char b = obj.getCpfCli().charAt(i);
 						String c = "" + b;
 						cpfV[i] = (c);
 					}	
 					int flagv = 0;
 					CpfCnpj.Cpf(flagv, cpfV);
 					if (CpfCnpj.Cpf(flagv, cpfV) == false) { 
 						exception.addErros("cpf", "Cnpj inválido");
 					}	
 					else {
 						obj.setCnpjCli(null);
 					}
 				}	
		}
		
 		if (rbJuridica.isSelected()) {
 			if (textCnpjCli.getText() == null || textCnpjCli.getText().trim().contentEquals("")) { 
 				exception.addErros("cnpj", "Cnpj é obrigatório");
 			}	
 			else {
 				if (textCnpjCli.getText() != null) {
 					if (textCnpjCli.getText().length() < 14) {
 						exception.addErros("cnpj", "Cnpj incompleto");
 					}
 				}
 				else {
 					obj.setCnpjCli(textCnpjCli.getText());
 					if (obj.getPessoaCli() == 'J') {
 						String cnpj1 = obj.getCnpjCli().replace(".", "");
 						String cnpj2 = cnpj1.replace("/", "");
 						String cnpj3 = cnpj2.replace("-", "");
 						obj.setCnpjCli(cnpj3);
 						String [] cnpjV = new String[14];
 						for (int i = 0 ; i < cnpjV.length ; i ++ ) {
 							char b = obj.getCnpjCli().charAt(i);
 							String c = "" + b;
 							cnpjV[i] = (c);
 						}	
 						int flagv = 0;
 						CpfCnpj.Cnpj(flagv, cnpjV);
 						if (CpfCnpj.Cnpj(flagv, cnpjV) == false) { 
 							exception.addErros("cnpj", "Cnpj inválido");
 						}	
 						else {
 							obj.setCpfCli(null);
 						}
 					}
 				}	
			}
		}	
		
		if (obj.getCnpjCli() != null) {
			if (codAnt != obj.getCodigoCli()) {
				if (obj.getPessoaCli() == 'J') {
					listCli = service.findAll();
					for (Cliente c : listCli) {
						if (c.getCnpjCli() != null) {
							if (c.getCnpjCli().equals(obj.getCnpjCli()) &&
									!c.getCodigoCli().equals(obj.getCodigoCli())) {
								exception.addErros("cpf", "Cnpj já existe ");
								exception.addErros("cnpj", c.getCodigoCli() + " " + c.getNomeCli());
							}	
						}	
					}
				} 
			}
		}
		
		if (obj.getCpfCli() != null) {
			if (codAnt != obj.getCodigoCli()) {
				if (obj.getPessoaCli() == 'F') {
					listCli = service.findAll();
					for (Cliente c : listCli) {
						if (c.getCpfCli() != null) {
							if (c.getCpfCli().equals(obj.getCpfCli()) &&
									!c.getCodigoCli().equals(obj.getCodigoCli())) {
								exception.addErros("cpf", "Cpf já existe ");
								exception.addErros("cnpj", c.getCodigoCli() + " " + c.getNomeCli());
							}	
						}
					}	
				}
			}
		}
 // tst se houve algum (erro com size > 0)		
		if (exception.getErros().size() > 0)
		{	throw exception;
		}
		return obj;
	} 
	
	private static Cliente verificaUfCli(Cliente obj) {
   		int flag = 0;
  		int i = 0;
  		String vuf = obj.getUfCli();
  		String tabelaUFCli = "AC, AL, AM, AP, AC, BA, CE, DF, GO, MA, MG, MT, MS, PB, PE, PI, PR, "
       		         	+ "RJ, RN, RO, RR, RS, SE, SC, SP, TO";
  		String[] tabuf = tabelaUFCli.split(",");
	  	
  		for ( i = 0 ; i < tabuf.length ; i ++)
  			{	Boolean fv = tabuf[i].contains(vuf) ;
  				if (fv == true )
  				{	flag = 1;	}
  			}	
  		if (flag == 0)
  		{	obj.setUfCli("XX"); 	}
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
 		Constraints.setTextFieldInteger(textCodigoCli);
 		Constraints.setTextFieldInteger(textNumeroCli);
// 		Constraints.setTextFieldInteger(textCepCli);
 		Constraints.setTextFieldInteger(textDdd01Cli);
 		Constraints.setTextFieldInteger(textTelefone01Cli);
 		Constraints.setTextFieldInteger(textDdd02Cli);
 		Constraints.setTextFieldInteger(textTelefone02Cli);
// 		Constraints.setTextFieldInteger(textCpfCli);
// 		Constraints.setTextFieldInteger(textCnpjCli);
 
 		Constraints.setTextFieldMaxLength(textNomeCli, 40);
 		Constraints.setTextFieldMaxLength(textCepCli, 8);
 		Constraints.setTextFieldMaxLength(textUfCli, 2);
 		Constraints.setTextFieldMaxLength(textDdd01Cli, 2);
 		Constraints.setTextFieldMaxLength(textTelefone01Cli, 9);
 		Constraints.setTextFieldMaxLength(textDdd02Cli, 2);
 		Constraints.setTextFieldMaxLength(textTelefone02Cli, 9);
  		Constraints.setTextFieldMaxLength(textEmailCli, 40);
   		Constraints.setTextFieldMaxLength(textCpfCli, 11);
  		Constraints.setTextFieldMaxLength(textCnpjCli, 14);
 	}

 /*
  * transforma string da tela p/ o tipo no bco de dados 
  */
  	public void updateFormData() throws ParseException {
 		if (entity == null)
 		{	throw new IllegalStateException("Entidade esta nula");
 		}
 		String cep = null;
 		String cepM = null;
 		String cpf1 = null;
 		String cpfM = null;
 		String cnpj1 = null;
 		String cnpjM = null;
 		labelUser.setText(user);
//  string value of p/ casting int p/ string 		
 		textCodigoCli.setText(String.valueOf(entity.getCodigoCli()));
 		if (entity.getCodigoCli() != null) {
 			codAnt = entity.getCodigoCli();
 		}
 		textNomeCli.setText(entity.getNomeCli());
 		textRuaCli.setText(entity.getRuaCli());
 		if (entity.getNumeroCli() == null) {
 			entity.setNumeroCli(0);
 		}
 		textNumeroCli.setText(String.valueOf(entity.getNumeroCli()));
 		
 		textComplementoCli.setText(entity.getComplementoCli());
 		
 		textCepCli.setText(entity.getCepCli());
 		if (entity.getCepCli() != null) {
 			cep = entity.getCepCli();
   			cepM = Mascaras.formataString(cep, "Cep");
   			textCepCli.setText(cepM);
 		}
System.out.println();	
   		textBairroCli.setText(entity.getBairroCli());
 		textCidadeCli.setText(entity.getCidadeCli());
 		textUfCli.setText(entity.getUfCli());
 		textDdd01Cli.setText(String.valueOf(entity.getDdd01Cli()));
 		textTelefone01Cli.setText(String.valueOf(entity.getTelefone01Cli()));
 		if (entity.getDdd02Cli() == null) {
 			entity.setDdd02Cli(0);
 		}
 		textDdd02Cli.setText(String.valueOf(entity.getDdd02Cli()));
 		if (entity.getTelefone02Cli() == null) {
 			entity.setTelefone02Cli(0);
 		}
 		textTelefone02Cli.setText(String.valueOf(entity.getTelefone02Cli()));
  	 	textEmailCli.setText(entity.getEmailCli());
  	 	rbFisica.setSelected(false);
  	 	rbJuridica.setSelected(false);
  	 	if (entity.getPessoaCli() == 'F') {
  	 		rbFisica.setSelected(true);
  	 		textCnpjCli.setText(null);
   	 	}	 
  	 	if (entity.getPessoaCli() == 'J') { 
  	 		rbJuridica.setSelected(true);
  	 		textCpfCli.setText(null);
  	 	} 
  	 	
  	 	if (entity.getPessoaCli() == 'F') {
  	 		textCpfCli.setText(entity.getCpfCli());
  	 		if (entity.getCpfCli() != null ) {
  	 			cpf1 = entity.getCpfCli();
  	 			cpfM = Mascaras.formataString(cpf1, "Cpf");
  	 			textCpfCli.setText(cpfM);
  	 		}	
  	 	}	
  	 	
  	 	if (entity.getPessoaCli() == 'J') {
  	 		textCnpjCli.setText(entity.getCnpjCli());
  	 		if (entity.getCnpjCli() != null );{
  	 			cnpj1 = entity.getCnpjCli();
  	 			cnpjM = Mascaras.formataString(cnpj1, "Cnpj");
  	 			textCnpjCli.setText(cnpjM);
  	 		}	
  	 	}
    }
 
// mandando a msg de erro para o labelErro correspondente 	
 	private void setErrorMessages(Map<String, String> erros) {
 		Set<String> fields = erros.keySet();
 		if (fields.contains("nome")) 
 		{	labelErrorNomeCli.setText(erros.get("nome"));	
 		}
 		labelErrorNomeCli.setText((fields.contains("nome") ? erros.get("nome") : ""));		
 		labelErrorRuaCli.setText((fields.contains("rua") ? erros.get("rua") : ""));		
		labelErrorBairroCli.setText((fields.contains("bairro") ? erros.get("bairro") : ""));
		labelErrorCidadeCli.setText((fields.contains("cidade") ? erros.get("cidade") : ""));
		labelErrorUFCli.setText((fields.contains("uf") ? erros.get("uf") : ""));
 		labelErrorTelefone01Cli.setText((fields.contains("telefone01") ? erros.get("telefone01") : ""));		
 		labelErrorEmailCli.setText((fields.contains("email") ? erros.get("email") : ""));
 		labelErrorPessoaCli.setText((fields.contains("pessoa") ? erros.get("pessoa") : ""));		
 		labelErrorCpfCli.setText((fields.contains("cpf") ? erros.get("cpf") : ""));		
 		labelErrorCnpjCli.setText((fields.contains("cnpj") ? erros.get("cnpj") : ""));		
   	}	
 } 
