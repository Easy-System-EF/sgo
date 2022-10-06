package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.dao.MesesDao;
import sgo.model.entities.DadosFolhaMes;
import sgo.model.entities.Meses;
  
public class MesesService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private MesesDao dao = DaoFactory.createMesesDao();

//    criar no fornecedorlist uma dependencia no forn controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Meses> findAll() {
   		return dao.findAll();
	} 
	
	public Meses findId(Integer mes) {
   		return dao.findId(mes);
	} 	
}
