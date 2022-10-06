package model.users.dao;

import db.DB;
import model.users.jdbc.UsersDaoJDBC;

public class DaoFactory {

/*
 * � obrigat�rio passar uma conex�o como argumento (db.getCon...)
 */
 	public static UsersDao createUsersDao() {
		return new UsersDaoJDBC(DB.getConnection());
	}
	
}
