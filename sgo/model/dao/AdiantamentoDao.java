package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Adiantamento;

public interface AdiantamentoDao {

	void insert(Adiantamento obj);
	void deleteById(Integer codigo);
 	List<Adiantamento> findMesTipo(Integer mes, Integer Ano, String tipo);
 	List<Adiantamento> findMes(Integer mes, Integer Ano);
 	List<Adiantamento> findPesquisa(String str);
 	Adiantamento findByOs(Integer os);
 	Adiantamento findByBal(Integer numBal);
}
  