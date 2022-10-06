package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Entrada;
import sgo.model.entities.Grupo;

public interface EntradaDao {

	void insert(Entrada obj);
	void update(Entrada obj);
	void deleteById(Integer codigo);
	Entrada findById(Integer codigo, List<Grupo> grupo); 
 	List<Entrada> findAll(List<Grupo> grupo);

}
  