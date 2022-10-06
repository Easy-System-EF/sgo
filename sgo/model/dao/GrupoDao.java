package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Grupo;

public interface GrupoDao {

	void insert(Grupo obj);
	void update(Grupo obj);
	void deleteById(Integer codigo);
	Grupo findById(Integer codigo); 
 	List<Grupo> findAll();

}
