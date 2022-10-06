package sgo.model.dao;

import java.util.List;

import sgo.model.entities.NotaFiscal;

public interface NotaFiscalDao {

	void insert(NotaFiscal obj);
	void deleteById(Integer nf);
 	List<NotaFiscal> findAll();
}
