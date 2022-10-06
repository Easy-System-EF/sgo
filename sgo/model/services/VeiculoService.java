package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.dao.VeiculoDao;
import sgo.model.entities.Veiculo;
  
public class VeiculoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private VeiculoDao dao = DaoFactory.createVeiculoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Veiculo> findAll() {
   		return dao.findAll();
	} 
	
	public Veiculo findByPlaca(String placa) {
   		return dao.findByPlaca(placa);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Veiculo obj) {
		if (obj.getNumeroVei() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Veiculo obj) {
		dao.deleteById(obj.getPlacaVei());
	}
}
