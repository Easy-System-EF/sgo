package model.dao;

import java.util.List;

import sgcp.model.entityes.consulta.ParPeriodo;

public interface ParPeriodoDao {

	void update(ParPeriodo obj);
  	void deleteByAll();
  	ParPeriodo findById(Integer cod);
   	List<ParPeriodo> findAll();
 }
