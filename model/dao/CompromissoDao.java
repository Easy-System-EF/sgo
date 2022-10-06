package model.dao;

import java.util.List;

import sgcp.model.entityes.Compromisso;
import sgcp.model.entityes.Fornecedor;
import sgcp.model.entityes.TipoFornecedor;

public interface CompromissoDao {

	void insert(Compromisso obj);
 	void deleteById(Integer nnf, Integer fornId);
  	List<Compromisso> findAll();
  	List<Compromisso> findPesquisa(String str);
 	List<Compromisso> findByFornecedor(Fornecedor fornecedor);
	List<Compromisso> findByTipo(int tp);
}
