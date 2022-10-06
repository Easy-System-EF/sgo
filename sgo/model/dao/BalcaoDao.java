package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Balcao;

public interface BalcaoDao {

	void insert(Balcao obj);
	void update(Balcao obj);
	void deleteById(Integer codigo);
	Balcao findById(Integer codigo); 
 	List<Balcao> findAll();
	List<Balcao> findMesAno(Integer mm, Integer aa);
}
  