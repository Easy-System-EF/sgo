package sgo.model.dao;

import java.util.List;

import sgo.model.entities.DadosFolhaMes;
import sgo.model.entities.Meses;

public interface MesesDao {

 	List<Meses> findAll();
 	Meses findId(Integer mes);
}
