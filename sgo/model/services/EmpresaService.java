package sgo.model.services;

import sgo.model.dao.DaoFactory;
import sgo.model.dao.EmpresaDao;
import sgo.model.entities.Empresa;
  
public class EmpresaService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private EmpresaDao dao = DaoFactory.createEmpresaDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public void insertOrUpdate(Empresa obj) {
		if (obj.getNumeroEmp() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	} 

	public Empresa findById(Integer cod) {
   		return dao.findById(cod);
	}
}
