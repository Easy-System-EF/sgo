package model.dao;

import java.util.List;

import sgcp.model.entityes.TipoFornecedor;

public interface TipoFornecedorDao {

	void insert(TipoFornecedor obj);
	void update(TipoFornecedor obj);
	void deleteById(Integer codigo);
	TipoFornecedor findById(Integer codigo); 
 	List<TipoFornecedor> findAll();

}
