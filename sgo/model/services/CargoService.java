package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.entities.Cargo;
import sgo.model.dao.CargoDao;
  
public class CargoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private CargoDao dao = DaoFactory.createCargoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Cargo> findAll() {
   		return dao.findAll();
	} 
	
	public Cargo findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Cargo obj) {
		if (obj.getCodigoCargo() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Cargo obj) {
		dao.deleteById(obj.getCodigoCargo());
	}
}
