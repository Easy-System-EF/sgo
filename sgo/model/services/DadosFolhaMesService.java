package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.entities.DadosFolhaMes;
import sgo.model.dao.DadosFolhaMesDao;
  
public class DadosFolhaMesService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private DadosFolhaMesDao dao = DaoFactory.createDadosFolhaMesDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<DadosFolhaMes> findAll() {
   		return dao.findAll();
	} 
	
	public void zeraAll() {
   		dao.zeraAll();
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void insert(DadosFolhaMes obj) {
		if (obj.getNumeroDados() == null) {
			dao.insert(obj);
		}
	}	
}
