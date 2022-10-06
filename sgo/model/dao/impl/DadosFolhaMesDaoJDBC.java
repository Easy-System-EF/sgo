package sgo.model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import sgo.model.dao.DadosFolhaMesDao;
import sgo.model.entities.Anos;
import sgo.model.entities.DadosFolhaMes;
import sgo.model.entities.Meses;
  
public class DadosFolhaMesDaoJDBC implements DadosFolhaMesDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public DadosFolhaMesDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(DadosFolhaMes obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO dadosFolhaMes " +
				      "(NomeDados, CargoDados, SituacaoDados, SalarioDados, " +
				      "ComissaoDados, ValeDados, ReceberDados, TotalDados , " +
				      "MesesIdDados, AnosIdDados ) " +
  				      "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 
 
 			st.setString(1, obj.getNomeDados());
 			st.setString(2, obj.getCargoDados());
 			st.setString(3, obj.getSituacaoDados());
 			st.setString(4, obj.getSalarioDados());
 			st.setString(5, obj.getComissaoDados());
 			st.setString(6, obj.getValeDados());
 			st.setString(7, obj.getReceberDados());
 			st.setString(8, obj.getTotalDados());
			st.setInt(9,  obj.getMeses().getNumeroMes());
			st.setInt(10, obj.getAnos().getNumeroAnos());
 			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroDados(codigo);
				}
				else
				{	throw new DbException("Erro!!! sem inclusão" );
				}
			}	
  		}
 		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
 
	@Override
	public List<DadosFolhaMes> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, meses.*, anos.* " +
						"From DadosFolhaMes " +	
							"INNER JOIN meses " +
								"ON dadosFolhaMes.MesesIdDados = meses.NumeroMes " + 
							"INNER JOIN anos " +
								"ON dadosFolhaMes.AnosIdDados = anos.NumeroAnos " + 
					"ORDER BY NomeDados");
			
			rs = st.executeQuery();
			
			List<DadosFolhaMes> list = new ArrayList<>();
			Map<Integer, Meses> mapMes = new HashMap();
			Map<Integer, Anos> mapAno = new HashMap();
			
			while (rs.next()) {
				Meses objMes = mapMes.get(rs.getInt("MesesIdDados"));
				if (objMes == null) {
					objMes = instantiateMeses(rs);
					mapMes.put(rs.getInt("MesesIdDados"), objMes);
				}	
				Anos objAno = mapAno.get(rs.getInt("AnosIdDados"));
				if (objAno == null) {
					objAno = instantiateAnos(rs);
					mapAno.put(rs.getInt("AnosIdDados"), objAno);
				}	
				DadosFolhaMes obj = instantiateDadosFolhaMes(rs, objMes, objAno);
 					list.add(obj);
 			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	@Override
	public void zeraAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"TRUNCATE TABLE sgo.dadosFolhaMes " );

			st.executeUpdate();

		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	} 
	
	private DadosFolhaMes instantiateDadosFolhaMes(ResultSet rs, Meses mes, Anos ano) throws SQLException {
		DadosFolhaMes dados = new DadosFolhaMes();
 		dados.setNumeroDados(rs.getInt("NumeroDados"));
 		dados.setNomeDados(rs.getString("NomeDados"));	
 		dados.setCargoDados(rs.getString("CargoDados"));
 		dados.setSituacaoDados(rs.getString("SituacaoDados"));
 		dados.setSalarioDados(rs.getString("SalarioDados"));
 		dados.setComissaoDados(rs.getString("ComissaoDados"));
 		dados.setValeDados(rs.getString("ValeDados"));
 		dados.setReceberDados(rs.getString("ReceberDados"));
 		dados.setTotalDados(rs.getString("TotalDados"));
 		dados.setMeses(mes);
 		dados.setAnos(ano);
        return dados;
	}

	private Meses instantiateMeses(ResultSet rs) throws SQLException {
		Meses meses = new Meses();
		meses.setNumeroMes(rs.getInt("NumeroMes"));
		meses.setNomeMes(rs.getString("NomeMes"));
		return meses;
	}

	
	private Anos instantiateAnos(ResultSet rs) throws SQLException {
		Anos anos = new Anos();
		anos.setNumeroAnos(rs.getInt("NumeroAnos"));
		anos.setAnoAnos(rs.getInt("AnoAnos"));
		return anos;
	} 	
}
