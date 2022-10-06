package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ParPeriodoDao;
import sgcp.model.entityes.TipoFornecedor;
import sgcp.model.entityes.consulta.ParPeriodo;
 
public class ParPeriodoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ParPeriodoDao dao = DaoFactory.createConsultaDao();
 
	public List<ParPeriodo> findAll(){
 		return dao.findAll();
	}
	
	public ParPeriodo findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
 // * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void update(ParPeriodo obj) {
		dao.update(obj);
 	}
 
// removendo
	public void remove() {
   			dao.deleteByAll();
 	}
}
