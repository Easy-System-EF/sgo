package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Cargo;

public interface CargoDao {

	void insert(Cargo obj);
	void update(Cargo obj);
	void deleteById(Integer codigo);
	Cargo findById(Integer codigo); 
 	List<Cargo> findAll();

}
