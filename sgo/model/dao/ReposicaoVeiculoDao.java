package sgo.model.dao;

import java.util.List;

import sgo.model.entities.ReposicaoVeiculo;

public interface ReposicaoVeiculoDao {

	void insert(ReposicaoVeiculo obj);
	void deleteByOs(Integer os);
	List<ReposicaoVeiculo> findByPlaca(String placa, Integer km); 
 	List<ReposicaoVeiculo> findAllData();

}
 