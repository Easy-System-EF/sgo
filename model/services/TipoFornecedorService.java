package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.TipoFornecedorDao;
import sgcp.model.entityes.TipoFornecedor;
 
public class TipoFornecedorService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private TipoFornecedorDao dao = DaoFactory.createTipoFornecedorDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<TipoFornecedor> findAll() {
   		return dao.findAll();
	} 
	
	public TipoFornecedor findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(TipoFornecedor obj) {
		if (obj.getCodigoTipo() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(TipoFornecedor obj) {
		dao.deleteById(obj.getCodigoTipo());
	}
}
