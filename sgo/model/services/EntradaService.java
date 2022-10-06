package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.dao.EntradaDao;
import sgo.model.entities.Entrada;
import sgo.model.entities.Grupo;
 
public class EntradaService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private EntradaDao dao = DaoFactory.createEntradaDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Entrada> findAll(List<Grupo> grupo) {
   		return dao.findAll(grupo);
	} 
	
	public Entrada findById(Integer cod, List<Grupo> grupo) {
    		return dao.findById(cod, grupo);	
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Entrada obj) {
		if (obj.getNumeroEnt() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Entrada obj) {
		dao.deleteById(obj.getNumeroEnt());
	}
}
