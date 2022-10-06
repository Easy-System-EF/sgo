package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Orcamento;

public interface OrcamentoDao {

	void insert(Orcamento obj);
	void update(Orcamento obj);
	void deleteById(Integer codigo);
	Orcamento findById(Integer codigo); 
	Orcamento findByOS(Integer os); 
	Orcamento findByPlaca(String placa); 
 	List<Orcamento> findAll();
 	List<Orcamento> findPesquisa(String str);

}
  