package sgo.model.services;

import java.util.List;

import sgo.model.dao.DaoFactory;
import sgo.model.dao.ReceberDao;
import sgo.model.entities.Receber;
 
public class ReceberService {

// dependencia - injeta com padrao factory que vai buscar no bco de dados
// retornando o dao.findAll 
	private ReceberDao dao = DaoFactory.createReceberDao();

//    criar no parcelalist uma dependencia na parc controlador para esse metodo, 
//	carregando e mostrando na view		
	public List<Receber> findByAllOs(Integer os) {
   		return dao.findByAllOs(os);
	} 
	
	public List<Receber> findAllAberto() {
   		return dao.findAllAberto();
	} 
	
	public List<Receber> findAllPago() {
   		return dao.findAllPago();
	} 
	
	public List<Receber> findPeriodoAberto() {
   		return dao.findPeriodoAberto();
	} 

	public List<Receber> findPeriodoPago() {
   		return dao.findPeriodoPago();
	} 

	public List<Receber> findByIdClienteAberto(Integer cod) {
   		return dao.findByIdClienteAberto(cod);
	} 

	public List<Receber> findByIdClientePago(Integer cod) {
   		return dao.findByIdClientePago(cod);
	} 
	
// * inserindo ou atualizando via dao
// * se o codigo não existe insere, se existe altera 
	public void insert(Receber obj) {
		if (obj.getNumeroRec() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
 	}

	public void update(Receber obj) {
		dao.update(obj);
 	}

// removendo
	public void removeOS(Receber obj) {
			dao.removeOS(obj.getOsRec());
	}
}