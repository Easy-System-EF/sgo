package model.dao;

import java.util.List;

import sgcp.model.entityes.Fornecedor;

public interface FornecedorDao {

	void insert(Fornecedor obj);
	void update(Fornecedor obj);
	void deleteById(Integer codigo);
	Fornecedor findById(Integer codigo); 
 	List<Fornecedor> findAll();
 	List<Fornecedor> findPesquisa(String str);

}
