package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Veiculo;

public interface VeiculoDao {

	void insert(Veiculo obj);
	void update(Veiculo obj);
	void deleteById(String placa);
 	List<Veiculo> findAll();
	Veiculo findByPlaca(String placa);
}
