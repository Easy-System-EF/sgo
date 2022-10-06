package sgo.model.dao;

import java.util.List;

import sgo.model.entities.DadosFolhaMes;

public interface DadosFolhaMesDao {

	void insert(DadosFolhaMes obj);
 	List<DadosFolhaMes> findAll();
	void zeraAll();
}
