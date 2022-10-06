package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.dao.OrcamentoDao;
import sgo.model.entities.Orcamento;
 
public class OrcamentoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private OrcamentoDao dao = DaoFactory.createOrcamentoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Orcamento> findAll() {
   		return dao.findAll();
	} 
	
	public List<Orcamento> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
	public Orcamento findById(Integer cod) {
		return dao.findById(cod);
	} 

	public Orcamento findByOS(Integer os) {
		return dao.findById(os);
	} 

	public Orcamento findByPlaca(String placa) {
		return dao.findByPlaca(placa);
	} 

// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Orcamento obj) {
		if (obj.getNumeroOrc() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Orcamento obj) {
		dao.deleteById(obj.getNumeroOrc());
	}
}
