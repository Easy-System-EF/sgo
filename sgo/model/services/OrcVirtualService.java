package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.dao.OrcVirtualDao;
import sgo.model.entities.OrcVirtual;
 
public class OrcVirtualService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private OrcVirtualDao dao = DaoFactory.createOrcVirtualDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<OrcVirtual> findAll() {
   		return dao.findAll();
	} 
	
	public List<OrcVirtual> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
	public List<OrcVirtual> findByOrcto(Integer orc) {
   		return dao.findByOrcto(orc);
	} 
	
	public void zeraAll() {
   		dao.zeroAll();
	} 
	
	public List<OrcVirtual> findByBalcao(Integer bal) {
   		return dao.findByBalcao(bal);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(OrcVirtual obj) {
		if (obj.getNumeroVir() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(OrcVirtual obj) {
		dao.deleteById(obj.getNumeroVir());
	}
}
