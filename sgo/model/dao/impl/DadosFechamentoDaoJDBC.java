package sgo.model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import sgo.model.dao.DadosFechamentoDao;
import sgo.model.entities.Anos;
import sgo.model.entities.DadosFechamento;
import sgo.model.entities.Meses;
  
public class DadosFechamentoDaoJDBC implements DadosFechamentoDao {
	
// tb entra construtor p/ conexão
	private Connection conn;
	
	public DadosFechamentoDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	String classe = "Dados Fechamento Mensal";
	
	@Override
	public void insert(DadosFechamento obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
  		try {
			st = conn.prepareStatement(
					"INSERT INTO dadosFechamento " +
				      "(OsMensal, BalMensal, DataMensal, ClienteMensal, FuncionarioMensal, " +
                      "ValorOsMensal, ValorMaterialMensal, ValorComissaoMensal, ValorResultadoMensal, " +
				      "ValorAcumuladoMensal, MesesIdMensal, AnosIdMensal ) " +
  				      "VALUES " +
				      "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
 					 Statement.RETURN_GENERATED_KEYS); 

			st.setInt(1, obj.getOsMensal());
			st.setInt(2, obj.getBalMensal());
			st.setDate(3, new java.sql.Date(obj.getDataMensal().getTime()));
			st.setString(4, obj.getClienteMensal());
			st.setString(5, obj.getFuncionarioMensal());
 			st.setDouble(6, obj.getValorOsMensal());
 			st.setDouble(7, obj.getValorMaterialMensal());
 			st.setDouble(8, obj.getValorComissaoMensal());
 			st.setDouble(9, obj.getValorResultadoMensal());
 			st.setDouble(10, obj.getValorAcumuladoMensal());
 			st.setInt(11, obj.getMes().getNumeroMes());
 			st.setInt(12, obj.getAno().getNumeroAnos());
 			
 			int rowsaffectad = st.executeUpdate();
			
			if (rowsaffectad > 0)
			{	rs = st.getGeneratedKeys();
				if (rs.next())
				{	int codigo = rs.getInt(1);
					obj.setNumeroMensal(codigo);
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
	public List<DadosFechamento> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( 
					"SELECT *, meses.*, anos.* " +
						"From DadosFechamento " +	
							"INNER JOIN meses " +
								"ON dadosFechamento.MesesIdMensal = meses.NumeroMes " + 
							"INNER JOIN anos " +
								"ON dadosFechamento.AnosIdMensal = anos.NumeroAnos " + 
					"ORDER BY NumeroMensal");
			
			rs = st.executeQuery();
			
			List<DadosFechamento> list = new ArrayList<>();
			Map<Integer, Meses> mapMes = new HashMap();
			Map<Integer, Anos> mapAno = new HashMap();
			
			while (rs.next()) {
				Meses objMes = mapMes.get(rs.getInt("MesesIdMensal"));
				if (objMes == null) {
					objMes = instantiateMeses(rs);
					mapMes.put(rs.getInt("MesesIdMensal"), objMes);
				}	
				Anos objAno = mapAno.get(rs.getInt("AnosIdMensal"));
				if (objAno == null) {
					objAno = instantiateAnos(rs);
					mapAno.put(rs.getInt("AnosIdMensal"), objAno);
				}	
				DadosFechamento obj = instantiateDadosFechamento(rs, objMes, objAno);
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
					"TRUNCATE TABLE sgo.dadosFechamento " );

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
	
	private DadosFechamento instantiateDadosFechamento(ResultSet rs, Meses mes, Anos ano) throws SQLException {
		DadosFechamento dados = new DadosFechamento();
 		dados.setNumeroMensal(rs.getInt("NumeroMensal"));
 		dados.setOsMensal(rs.getInt("OsMensal"));
 		dados.setBalMensal(rs.getInt("BalMensal"));
		dados.setDataMensal(new java.util.Date(rs.getTimestamp("DataMensal").getTime()));
		dados.setClienteMensal(rs.getString("ClienteMensal"));
		dados.setFuncionarioMensal(rs.getString("FuncionarioMensal"));
 		dados.setValorOsMensal(rs.getDouble("ValorOsMensal"));
 		dados.setValorMaterialMensal(rs.getDouble("ValorMaterialMensal"));
 		dados.setValorComissaoMensal(rs.getDouble("ValorComissaoMensal"));
 		dados.setValorResultadoMensal(rs.getDouble("ValorResultadoMensal"));
 		dados.setValorAcumuladoMensal(rs.getDouble("ValorAcumuladoMensal"));
 		dados.setMes(mes);
 		dados.setAno(ano);
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
