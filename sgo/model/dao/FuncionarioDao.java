package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Funcionario;

public interface FuncionarioDao {

	void insert(Funcionario obj);
	void update(Funcionario obj);
	void deleteById(Integer codigo);
	Funcionario findById(Integer codigo); 
 	List<Funcionario> findAll();
 	List<Funcionario> findPesquisa(String str);
 	List<Funcionario> findFechamento();
 	List<Funcionario> findByAtivo(String situacao);
}
  