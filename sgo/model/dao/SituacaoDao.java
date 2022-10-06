package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Situacao;

public interface SituacaoDao {

	void insert(Situacao obj);
	void update(Situacao obj);
	void deleteById(Integer codigo);
	Situacao findById(Integer codigo); 
 	List<Situacao> findAll();

}
