package model.dao;

import db.DB;
import model.dao.impl.CompromissoDaoJDBC;
import model.dao.impl.FornecedorDaoJDBC;
import model.dao.impl.ParPeriodoDaoJDBC;
import model.dao.impl.ParcelaDaoJDBC;
import model.dao.impl.TipoFornecedorDaoJDBC;

public class DaoFactory {

/*
 * é obrigatório passar uma conexão como argumento (db.getCon...)
 */
	public static CompromissoDao createCompromissoDao() {
		return new CompromissoDaoJDBC(DB.getConnection());
	}
	
	public static FornecedorDao createFornecedorDao() {
		return new FornecedorDaoJDBC(DB.getConnection());
	}
	
	public static TipoFornecedorDao createTipoFornecedorDao() {
		return new TipoFornecedorDaoJDBC(DB.getConnection());
	}
	public static ParcelaDao createParcelaDao() {
		return new ParcelaDaoJDBC(DB.getConnection());
	}
	public static ParPeriodoDao createConsultaDao() {
		return new ParPeriodoDaoJDBC(DB.getConnection());
	}
}
