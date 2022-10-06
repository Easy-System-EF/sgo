package sgo.model.services;

import java.util.List;

import sgo.model.dao.BalcaoDao;
import sgo.model.dao.DaoFactory;
import sgo.model.entities.Balcao;
import sgo.model.entities.OrdemServico;
 
public class BalcaoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private BalcaoDao dao = DaoFactory.createBalcaoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Balcao> findAll() {
   		return dao.findAll();
	} 
	
	public Balcao findById(Integer cod) {
		return dao.findById(cod);
} 

	public List<Balcao> findByMesAno(int mes, int ano) {
		return dao.findMesAno(mes, ano);
	} 

// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Balcao obj) {
		if (obj.getNumeroBal() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Balcao obj) {
		dao.deleteById(obj.getNumeroBal());
	}
}
