package sgo.model.services;

import java.util.List;

import sgo.model.dao.FuncionarioDao;
import sgo.model.entities.Funcionario;
import sgo.model.dao.DaoFactory;
 
public class FuncionarioService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private FuncionarioDao dao = DaoFactory.createFuncionarioDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Funcionario> findAll() {
   		return dao.findAll();
	} 
	
	public List<Funcionario> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
	public List<Funcionario> findFechamento() {
   		return dao.findFechamento();
	} 
	
	public List<Funcionario> findByAtivo(String situacao) {
   		return dao.findByAtivo(situacao);
	} 
	
	public Funcionario findById(Integer cod) {
    		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Funcionario obj) {
		if (obj.getCodigoFun() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Funcionario obj) {
		dao.deleteById(obj.getCodigoFun());
	}
}
