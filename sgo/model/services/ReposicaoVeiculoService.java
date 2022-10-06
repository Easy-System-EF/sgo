package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.entities.ReposicaoVeiculo;
import sgo.model.dao.ReposicaoVeiculoDao;
  
public class ReposicaoVeiculoService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ReposicaoVeiculoDao dao = DaoFactory.createReposicaoVeiculoDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<ReposicaoVeiculo> findAllData() {
   		return dao.findAllData();
	} 
	
	public List<ReposicaoVeiculo> findByPlaca(String placa, Integer km) {
   		return dao.findByPlaca(placa, km);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void insert(ReposicaoVeiculo obj) {
		if (obj.getNumeroRep() == null) {
			dao.insert(obj);
		}
	}

// removendo
	public void remove(ReposicaoVeiculo obj) {
		dao.deleteByOs(obj.getOsRep());
	}
}
