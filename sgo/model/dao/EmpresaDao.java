package sgo.model.dao;

import sgo.model.entities.Empresa;

public interface EmpresaDao {

 	void insert(Empresa tabend);
 	void update(Empresa tabend);
	Empresa findById(Integer cod);
}
