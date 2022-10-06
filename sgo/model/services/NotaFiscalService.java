package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.dao.NotaFiscalDao;
import sgo.model.entities.NotaFiscal;
  
public class NotaFiscalService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private NotaFiscalDao dao = DaoFactory.createNotaFiscalDao();

//	carregando e mostrando na view		
	public List<NotaFiscal> findAll() {
   		return dao.findAll();
	} 
	
	public void remove(Integer nf) {
		dao.deleteById(nf);
	}

	public void saveOrUpdate(NotaFiscal obj) {
		if (obj.getCodigoNF() == null) {
			dao.insert(obj);
		}
	}

}
