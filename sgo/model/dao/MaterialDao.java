package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Material;

public interface MaterialDao {

	void insert(Material obj);
	void update(Material obj);
	void deleteById(Integer codigo);
	Material findById(Integer codigo); 
 	List<Material> findAll();
 	List<Material> findPesquisa(String str);
 
}
