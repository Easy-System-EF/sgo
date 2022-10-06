package sgo.model.dao;

import java.util.List;

import sgo.model.entities.Anos;
import sgo.model.entities.Meses;

public interface AnosDao {

 	List<Anos> findAll();
 	Anos findAno(Integer ano);
}
