package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.dao.MaterialDao;
import sgo.model.entities.Material;
  
public class MaterialService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private MaterialDao dao = DaoFactory.createMaterialDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Material> findAll() {
   		return dao.findAll();
	} 
	
	public List<Material> findPesquisa(String str) {
   		return dao.findPesquisa(str);
	} 
	
	public Material findById(Integer cod) {
   		return dao.findById(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void saveOrUpdate(Material obj) {
		if (obj.getCodigoMat() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

// removendo
	public void remove(Material obj) {
		dao.deleteById(obj.getCodigoMat());
	}
}
