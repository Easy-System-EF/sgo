package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.dao.OrdemServicoDao;
import sgo.model.entities.OrdemServico;
 
public class OrdemServicoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private OrdemServicoDao dao = DaoFactory.createOrdemServicoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<OrdemServico> findAll() {
   		return dao.findAll();
	} 
	
	public OrdemServico findById(Integer cod) {
    		return dao.findById(cod);
	} 
	
	public List<OrdemServico> findByPlaca(String str) {
		return dao.findPlaca(str);
	} 

	public List<OrdemServico> findByMesAno(int mes, int ano) {
		return dao.findMesAno(mes, ano);
	} 

// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(OrdemServico obj) {
		if (obj.getNumeroOS() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(OrdemServico obj) {
		dao.deleteById(obj.getNumeroOS());
	}
}
