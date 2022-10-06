package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Cliente;

public interface ClienteDao {

	void insert(Cliente obj);
	void update(Cliente obj);
	void deleteById(Integer codigo);
	Cliente findById(Integer codigo); 
 	List<Cliente> findAll();
 	List<Cliente> findPesquisa(String str);

}
  