package sgo.model.dao;

import java.util.List;

import sgo.model.entities.DadosFechamento;

public interface DadosFechamentoDao {

	void insert(DadosFechamento obj);
 	List<DadosFechamento> findAll();
	void zeraAll();
}
