package sgo.model.services;

import java.util.List;

import sgo.model.dao.AdiantamentoDao;
import sgo.model.entities.Adiantamento;
import sgo.model.dao.DaoFactory;
 
public class AdiantamentoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private AdiantamentoDao dao = DaoFactory.createAdiantamentoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Adiantamento> findMes(Integer mes, Integer ano) {
   		return dao.findMes(mes, ano);
	} 
	
	public List<Adiantamento> findMesTipo(Integer mes, Integer ano, String tipo) {
   		return dao.findMesTipo(mes, ano, tipo);
	} 
	
	public List<Adiantamento> findPesquisa(String str) {
		return dao.findPesquisa(str);
} 
	public Adiantamento findByOs(Integer os) {
		return dao.findByOs(os);
} 
	public Adiantamento findByBal(Integer numBal) {
		return dao.findByOs(numBal);
} 

// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Adiantamento obj) {
		if (obj.getNumeroAdi() == null) {
			dao.insert(obj);
		}
	}

// removendo
	public void remove(Integer cod) {
		dao.deleteById(cod);
	}
}
