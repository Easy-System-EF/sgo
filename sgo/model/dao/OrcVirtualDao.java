package sgo.model.dao;

import java.util.List;

import sgo.model.entities.OrcVirtual;

public interface OrcVirtualDao {

	void insert(OrcVirtual obj);
	void update(OrcVirtual obj);
	void deleteById(Integer codigo);
 	List<OrcVirtual> findAll();
 	List<OrcVirtual> findPesquisa(String str);
 	List<OrcVirtual> findByOrcto(Integer orc);
	List<OrcVirtual> findByBalcao(Integer bal);
	void zeroAll();
}
  