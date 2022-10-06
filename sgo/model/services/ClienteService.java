package sgo.model.services;

import java.util.List;

import sgo.model.dao.ClienteDao;
import sgo.model.dao.DaoFactory;
import sgo.model.entities.Cliente;
 
public class ClienteService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ClienteDao dao = DaoFactory.createClienteDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Cliente> findAll() {
   		return dao.findAll();
	} 
	
	public List<Cliente> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
	public Cliente findById(Integer cod) {
    		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Cliente obj) {
		if (obj.getCodigoCli() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Cliente obj) {
		dao.deleteById(obj.getCodigoCli());
	}
}
