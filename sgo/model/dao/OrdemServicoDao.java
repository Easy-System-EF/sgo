package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Cargo;
import sgo.model.entities.OrdemServico;

public interface OrdemServicoDao {

	void insert(OrdemServico obj);
	void update(OrdemServico obj);
	void deleteById(Integer codigo);
	OrdemServico findById(Integer codigo); 
 	List<OrdemServico> findAll();
 	List<OrdemServico> findPlaca(String str);
 	List<OrdemServico> findMesAno(Integer mes, Integer ano);
}
  