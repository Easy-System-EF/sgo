package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Receber;

public interface ReceberDao {

	void insert(Receber obj);
	void update(Receber obj);
 	void removeOS(Integer osRec);
   	List<Receber> findByAllOs(Integer os);
  	List<Receber> findAllAberto();
  	List<Receber> findAllPago();
  	List<Receber> findPeriodoAberto();
  	List<Receber> findPeriodoPago();
   	List<Receber> findByIdClienteAberto(Integer cod);
   	List<Receber> findByIdClientePago(Integer cod);
 }
