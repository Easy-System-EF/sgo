package sgo.model.dao;

import db.DB;
import sgo.model.dao.impl.AdiantamentoDaoJDBC;
import sgo.model.dao.impl.AnosDaoJDBC;
import sgo.model.dao.impl.BalcaoDaoJDBC;
import sgo.model.dao.impl.CargoDaoJDBC;
import sgo.model.dao.impl.ClienteDaoJDBC;
import sgo.model.dao.impl.DadosFechamentoDaoJDBC;
import sgo.model.dao.impl.DadosFolhaMesDaoJDBC;
import sgo.model.dao.impl.EmpresaDaoJDBC;
import sgo.model.dao.impl.EntradaDaoJDBC;
import sgo.model.dao.impl.FuncionarioDaoJDBC;
import sgo.model.dao.impl.GrupoDaoJDBC;
import sgo.model.dao.impl.LoginDaoJDBC;
import sgo.model.dao.impl.MaterialDaoJDBC;
import sgo.model.dao.impl.MesesDaoJDBC;
import sgo.model.dao.impl.NotaFiscalJDBC;
import sgo.model.dao.impl.OrcVirtualDaoJDBC;
import sgo.model.dao.impl.OrcamentoDaoJDBC;
import sgo.model.dao.impl.OrdemServicoDaoJDBC;
import sgo.model.dao.impl.ReceberDaoJDBC;
import sgo.model.dao.impl.ReposicaoVeiculoDaoJDBC;
import sgo.model.dao.impl.SituacaoDaoJDBC;
import sgo.model.dao.impl.VeiculoDaoJDBC;

public class DaoFactory {

/*
 * é obrigatório passar uma conexão como argumento (db.getCon...)
 */
 	public static ClienteDao createClienteDao() {
		return new ClienteDaoJDBC(DB.getConnection());
	}
	
 	public static GrupoDao createGrupoDao() {
		return new GrupoDaoJDBC(DB.getConnection());
	}
	
  	public static MaterialDao createMaterialDao() {
		return new MaterialDaoJDBC(DB.getConnection());
	}
	
  	public static CargoDao createCargoDao() {
		return new CargoDaoJDBC(DB.getConnection());
	}
	
  	public static SituacaoDao createSituacaoDao() {
		return new SituacaoDaoJDBC(DB.getConnection());
	}
	
  	public static FuncionarioDao createFuncionarioDao() {
		return new FuncionarioDaoJDBC(DB.getConnection());
	}
	
  	public static OrcVirtualDao createOrcVirtualDao() {
		return new OrcVirtualDaoJDBC(DB.getConnection());
	}
	
  	public static OrcamentoDao createOrcamentoDao() {
		return new OrcamentoDaoJDBC(DB.getConnection());
	}
	
  	public static EntradaDao createEntradaDao() {
		return new EntradaDaoJDBC(DB.getConnection());
	}
  	
  	public static OrdemServicoDao createOrdemServicoDao() {
		return new OrdemServicoDaoJDBC(DB.getConnection());
	}	

  	public static ReceberDao createReceberDao() {
		return new ReceberDaoJDBC(DB.getConnection());
	}
  	
  	public static ReposicaoVeiculoDao createReposicaoVeiculoDao() {
		return new ReposicaoVeiculoDaoJDBC(DB.getConnection());
	}
  	public static VeiculoDao createVeiculoDao() {
		return new VeiculoDaoJDBC(DB.getConnection());
	}
  	public static AdiantamentoDao createAdiantamentoDao() {
		return new AdiantamentoDaoJDBC(DB.getConnection());
	}
  	public static MesesDao createMesesDao() {
		return new MesesDaoJDBC(DB.getConnection());
	}
  	public static DadosFolhaMesDao createDadosFolhaMesDao() {
		return new DadosFolhaMesDaoJDBC(DB.getConnection());
	}
  	public static AnosDao createAnosDao() {
		return new AnosDaoJDBC(DB.getConnection());
	}
  	public static DadosFechamentoDao createDadosFechamentoDao() {
		return new DadosFechamentoDaoJDBC(DB.getConnection());
	}
  	public static LoginDao createLoginDao() {
		return new LoginDaoJDBC(DB.getConnection());
	}
  	public static EmpresaDao createEmpresaDao() {
		return new EmpresaDaoJDBC(DB.getConnection());
	}
  	public static BalcaoDao createBalcaoDao() {
		return new BalcaoDaoJDBC(DB.getConnection());
	}
  	public static NotaFiscalDao createNotaFiscalDao() {
		return new NotaFiscalJDBC(DB.getConnection());
	}
}
