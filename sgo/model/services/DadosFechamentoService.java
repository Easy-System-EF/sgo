package sgo.model.services;

import java.util.List;

import sgo.model.dao.DadosFechamentoDao;
import sgo.model.dao.DaoFactory;
import sgo.model.entities.DadosFechamento;
  
public class DadosFechamentoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private DadosFechamentoDao dao = DaoFactory.createDadosFechamentoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<DadosFechamento> findAll() {
   		return dao.findAll();
	} 
	
	public void zeraAll() {
   		dao.zeraAll();
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void insert(DadosFechamento obj) {
			dao.insert(obj);
	}	
}
