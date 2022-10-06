package cobol_java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import db.DB;
import db.DbException;
import gui.util.Alerts;
import gui.util.CalculaParcela;
import javafx.scene.control.Alert.AlertType;
import model.services.FornecedorService;
import model.services.ParPeriodoService;
import model.services.TipoFornecedorService;
import sgcp.model.entityes.Fornecedor;
import sgcp.model.entityes.TipoFornecedor;
import sgcp.model.entityes.consulta.ParPeriodo;
import sgo.model.entities.Adiantamento;
import sgo.model.entities.Cliente;
import sgo.model.entities.Funcionario;
import sgo.model.entities.Material;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Orcamento;
import sgo.model.entities.OrdemServico;
import sgo.model.entities.Receber;
import sgo.model.entities.ReposicaoVeiculo;
import sgo.model.entities.Veiculo;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.ClienteService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.MaterialService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.OrcamentoService;
import sgo.model.services.ReceberService;
import sgo.model.services.ReposicaoVeiculoService;
import sgo.model.services.VeiculoService;

public class ConvOs implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dataHj = new Date();
			String classe = "";
 	 		String pathOs = "c:\\ARQS\\CGOTXT\\ATXTOS.TXT";
 	 		OrdemServico os = new OrdemServico();
 	 		Orcamento orc = new Orcamento();
 	 		OrcamentoService orcService = new OrcamentoService();
 	 		Set<OrdemServico> set = new HashSet<>();
 	 		Funcionario fun = new Funcionario();
 	 		FuncionarioService funService = new FuncionarioService(); 
 	 		Cliente cli =  new Cliente();
 	 		ClienteService cliService = new ClienteService();
 	 		Veiculo vei = new Veiculo();
 	 		VeiculoService veiService = new VeiculoService();
 	 		OrcVirtual vir = new OrcVirtual();
 	 		OrcVirtualService virService = new OrcVirtualService();
 	 		Calendar cal = Calendar.getInstance();
 	 		int dd = 0;
 	 		int mm = 0;
 	 		int mi = 2000;
 	 		int aa = 0;
 	 		int num = 0;
 	 				
 	 		try {
	 	 		BufferedReader brOs = new BufferedReader(new FileReader(pathOs));
	 			String lineOs = brOs.readLine();
 	 	 		while (lineOs != null) { 
	 	 			String[] campos = lineOs.split(";");
	 	 			os.setNumeroOS(Integer.parseInt(campos[0]));
	 	 			os.setDataOS(dataHj);
	 	 			int mmOs = 0;
	 	 			int aaOs = 0;
	 	 			Date dtvOs = new Date();
	 	 			dd = Integer.parseInt(campos[15].substring(0, 2));
	 	 			mm = Integer.parseInt(campos[15].substring(2, 4));
	 	 			aa = 2000 + Integer.parseInt(campos[15].substring(4, 6));
	 	 			mmOs = mm;
	 	 			aaOs = aa;
	 	 			cal.setTime(os.getDataOS());
	 	 			cal.set(aa, (mm - 1), dd);
	 	 			os.setDataOS(cal.getTime());
	 	 			
	 	 			os.setOrcamentoOS(Integer.parseInt(campos[1]));
	 	 			os.setPlacaOS(campos[2]);
	 	 			os.setPagamentoOS(Integer.parseInt(campos[6]));
	 	 			Date dtv1 = new Date();
	 	 			dd = Integer.parseInt(campos[7].substring(0, 2));
	 	 			mm = Integer.parseInt(campos[7].substring(2, 4));
	 	 			aa = 2000 + Integer.parseInt(campos[7].substring(4, 6));
	 	 			if (dd > 0 && mm > 0 && aa > 0 ) {
	 	 				cal.setTime(dtv1);
	 	 				cal.set(aa, (mm - 1), dd);
	 	 				dtv1 = cal.getTime();
	 	 			}
	 	 			if (os.getPagamentoOS() == 1 || os.getPagamentoOS() == 4) {
	 	 				os.setDataPrimeiroPagamentoOS(dtv1);
	 	 				os.setPrazoOS(1);
	 	 				os.setParcelaOS(1);
	 	 			}
	 	 			int dias = 0;
 	 				long dif = os.getDataOS().getTime() - dtv1.getTime();	
	 	 				dias = (int) TimeUnit.DAYS.convert(dif, TimeUnit.MILLISECONDS);
	 	 			if (dias > 0) {
		 	 			os.setPrazoOS(dias);	 	 				
	 	 			} else {
	 	 				os.setPrazoOS(1);
	 	 			}
	 	 			os.setNnfOS(Integer.parseInt(campos[13]));
	 	 			os.setObservacaoOS("");
	 	 			os.setMesOs(mmOs);
	 	 			os.setAnoOs(aaOs);
	 	 			orc = orcService.findById(os.getOrcamentoOS());
	 	 			os.setValorOS((orc.getTotalOrc() + (Double.parseDouble(campos[3]) / 100)));
	 	 			os.setKmOS(orc.getKmFinalOrc());
	 	 			os.setClienteOS(orc.getClienteOrc());
	 	 			os.setOrcamento(orc);
	 	 			set.add(new OrdemServico(os.getNumeroOS(), os.getDataOS(), 
 	 					os.getOrcamentoOS(), os.getPlacaOS(), os.getClienteOS(),
 	 					os.getValorOS(), os.getParcelaOS(), os.getPrazoOS(),
 	 					os.getPagamentoOS(), os.getDataPrimeiroPagamentoOS(), os.getNnfOS(),
 	 					os.getObservacaoOS(), os.getKmOS(), os.getMesOs(), os.getAnoOs(),
 	 					os.orcamento));
System.out.println(" os1 " + os.getNumeroOS());	 	 			
	 	 			lineOs = brOs.readLine();
 	 	 		}
  	 		brOs.close();
 	 	 		for (OrdemServico o : set) {
//	 	 			if (o != null) {
//	 	 				os = (OrdemServico) o;
System.out.println("os = o " + os.getNumeroOS());	 	 				
////	 	 				insertOS(os);
////						zeraMat();
////	 					updateMaterialOS(os);
////	 					gravaReceberOS(os);
////	 					createReposicao(os); 			
////	 					updateKmVeiculo(os);
						limpaReposicao();	
//	 	 				gravaComissaoOS(os);
	 	 				
//	 	 			}
 	 	 		}	
	 	 	}
 	 		catch (FileNotFoundException e) {
 	 			throw new DbException (e.getMessage());
 	 		}
 	 		catch (IOException e) {
 	 			throw new DbException (e.getMessage());
 	 		}
	}	

	private static void limpaReposicao() {
		Date dtHj = new Date();
		VeiculoService veiService = new VeiculoService();
		Veiculo vei = new Veiculo();
		List<Veiculo> listVei = new ArrayList<>();
		ReposicaoVeiculo rep = new ReposicaoVeiculo();
		ReposicaoVeiculoService repService = new ReposicaoVeiculoService();
		List<ReposicaoVeiculo> listRep = repService.findAllData();
		for (ReposicaoVeiculo r : listRep) {
			int del = 0;
System.out.println("rep " + r.getNumeroRep());			
			vei = veiService.findByPlaca(r.getPlacaRep());
			if (r.getProximaKmRep() < vei.getKmFinalVei()) {
				del += 1;
			}
			if (r.getProximaDataRep().before(dtHj)) {
				del += 1;
			}
			if (del > 0) {
				rep = (ReposicaoVeiculo) r;
				repService.remove(rep);
			}
		}
	}

	private static void zeraMat() {
		Material mat1 = new Material();
		MaterialService mat1Service = new MaterialService();
		List<Material> listMat = new ArrayList<>();
		listMat = mat1Service.findAll();
		for (Material m : listMat) {
System.out.println("mat " + m.getCodigoMat());			
			m.setEntradaMat(0.00);
			m.setSaidaMat(0.00);
			m.setCmmMat(0.00);
			mat1 = (Material) m;
			mat1Service.saveOrUpdate(mat1);
		}		
	}

	private static void gravaComissaoOS(OrdemServico entity2) {
		String classe = "Adiantamento ";
		AdiantamentoService adiService = new AdiantamentoService();
		Orcamento orc = new Orcamento();
		OrcamentoService orcService = new OrcamentoService();
		orc = orcService.findById(entity2.getOrcamentoOS());
		String grupo = orc.getVirtual().getMaterial().getGrupo().getNomeGru();
System.out.println(grupo);		
		if (grupo == "Mão de obra") {
			double mao = orc.getVirtual().getTotalMatVir();
			Funcionario fun = new Funcionario();
			FuncionarioService funService = new FuncionarioService();
			fun = funService.findById(orc.getFuncionario().getCodigoFun());
			OrcVirtual vir = new OrcVirtual();
			OrcVirtualService virService = new OrcVirtualService(); 
			Adiantamento adi = new Adiantamento();
			adi.setDataAdi(entity2.getDataOS());
			adi.setCodFunAdi(fun.getCodigoFun());
			adi.setNomeFunAdi(fun.getNomeFun());
			adi.setCargoAdi(fun.getCargoFun());
			adi.setSituacaoAdi(fun.getSituacaoFun());
			adi.setValorAdi(0.00);
			adi.setComissaoAdi(0.0);
			Calendar cal = Calendar.getInstance();
			cal.setTime(adi.getDataAdi());
			adi.setMesAdi(cal.get(Calendar.MONTH) + 1);
			adi.setAnoAdi(cal.get(Calendar.YEAR));
			adi.setOsAdi(entity2.getNumeroOS());
			adi.setBalcaoAdi(0);
			adi.setTipoAdi("C");
			adi.setFuncionario(fun);
			adi.setCargo(fun.getCargo());
			adi.setSituacao(fun.getSituacao());
			adi.calculaComissao(mao);
System.out.println(adi.getNomeFunAdi() + " " + fun.getCargo().getComissaoCargo()  + " " + mao);			
//			adiService.saveOrUpdate(adi);
		}	
	}
	 
	public static void insertOS(OrdemServico os) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
System.out.println("os " + os.getNumeroOS());		
		try {
			conn = DB.getConnection();	
			st = conn.prepareStatement(
					"INSERT INTO OrdemServico " +
						      "(NumeroOS, DataOS, OrcamentoOS, PlacaOS, ClienteOS, ValorOS, " +
						       "ParcelaOS, PrazoOS, PagamentoOS, DataPrimeiroPagamentoOS, " +
						       "NnfOS, ObservacaoOS, KmOS, MesOS, AnoOS, OrcamentoId) " +
		   				       "VALUES " +
						       "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
		 					 Statement.RETURN_GENERATED_KEYS); 

					st.setInt(1, os.getNumeroOS());
					st.setDate(2, new java.sql.Date(os.getDataOS().getTime()));
					st.setInt(3, os.getOrcamentoOS());
					st.setString(4, os.getPlacaOS());
					st.setString(5,  os.getClienteOS());
					st.setDouble(6, os.getValorOS());
					st.setInt(7,  os.getParcelaOS());
					st.setInt(8, os.getPrazoOS());
					st.setInt(9, os.getPagamentoOS());
					st.setDate(10, new java.sql.Date(os.getDataPrimeiroPagamentoOS().getTime()));
					st.setInt(11, os.getNnfOS());
					st.setString(12, os.getObservacaoOS());
					st.setInt(13, os.getKmOS());
					st.setInt(14, os.getMesOs());
					st.setInt(15, os.getAnoOs());
					st.setInt(16, os.getOrcamento().getNumeroOrc());

	 	 	   		st.executeUpdate();
	 	 		}	
	 	 		catch (SQLException e) {
	 	 			throw new DbException (e.getMessage());
	 	 		}
	 	 		finally {
	 	 			DB.closeResultSet(rs);
	 	 			DB.closeStatement(st);
	 	 		}
	 	 	}
	
	private static void updateMaterialOS(OrdemServico entity2) {
		String classe = "Material";
System.out.println("mat " + entity2.getNumeroOS());		
		Material material = new Material();
		MaterialService matService = new MaterialService();
		List<Material> listMat = matService.findAll();
		try {
			OrcVirtualService vService = new OrcVirtualService();
			List<OrcVirtual> listVir = vService.findAll();
			List<OrcVirtual> vir = vService.findByOrcto(entity2.getOrcamentoOS());
			for (OrcVirtual v : vir) {
				material = matService.findById(v.getMaterial().getCodigoMat());
					material.setEntradaMat(v.getQuantidadeMatVir());
					material.setSaidaMat(v.getQuantidadeMatVir());
					material.setCmmMat(material.calculaCmm());
					matService.saveOrUpdate(material);
				}
			}	
			catch (DbException e) {
				Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
			}
	}

	private static void gravaReceberOS(OrdemServico entity2) {
		String classe = "Receber";
		Receber receber = new Receber();
		ReceberService recService = new ReceberService();
		Fornecedor forn = new Fornecedor();
		FornecedorService forService = new FornecedorService();
		TipoFornecedor tpFor = new TipoFornecedor();
		TipoFornecedorService tpService = new TipoFornecedorService();
		ParPeriodo periodo = new ParPeriodo();
		ParPeriodoService perService = new ParPeriodoService();
		perService.findById(1);
		SimpleDateFormat sdfi = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			Date dti = sdfi.parse("01/01/2001 00:00:00");
			Date dtf = sdff.parse("31/01/2041 00:00:00");
			forn = forService.findById(1);
			tpFor = tpService.findById(1);
			periodo.setIdPeriodo(1);;
			periodo.setDtiPeriodo(dti);
			periodo.setDtfPeriodo(dtf);
			periodo.setFornecedor(forn);
			periodo.setTipoFornecedor(tpFor);
			perService.update(periodo);
			receber.setFuncionarioRec(entity2.getOrcamento().getFuncionario().getCodigoFun());
			receber.setClienteRec(entity2.getOrcamento().getCliente().getCodigoCli());
			receber.setNomeClienteRec(entity2.getOrcamento().getCliente().getNomeCli());
			receber.setOsRec(entity2.getNumeroOS());
			receber.setDataOsRec(entity2.getDataOS());
			receber.setPlacaRec(entity2.getPlacaOS());
			receber.setPeriodo(periodo);
			if (entity2.getPagamentoOS() == 1) {
				receber.setFormaPagamentoRec("Dinheiro");
			} else {
				if (entity2.getPagamentoOS() == 4) {
					receber.setFormaPagamentoRec("CC");					
				}
			}
			if (entity2.getPrazoOS() == 1) {
				receber.setDataPagamentoRec(entity2.getDataPrimeiroPagamentoOS());
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date dtp = sdf.parse("00/00/0000 00:00:00");
				receber.setDataPagamentoRec(dtp);
				}
			for (int i = 1 ; i < entity2.getParcelaOS() + 1 ; i ++) {
				receber.setParcelaRec(i);
				if (i == 1) {
					receber.setDataVencimentoRec(entity2.getDataPrimeiroPagamentoOS());
				} 
				if (i > 1) {
					receber.setDataVencimentoRec(CalculaParcela.CalculaVencimentoDia
							(entity2.getDataPrimeiroPagamentoOS(), (i - 1), entity2.getPrazoOS()));
				}	
				receber.setValorRec(CalculaParcela.calculaParcelas
							(entity2.getValorOS(), entity2.getParcelaOS(), i));
				receber.setNumeroRec(null);
			}
			recService.insert(receber);
System.out.println("rec " + receber.getNumeroRec());			
		}	
		catch (ParseException p) {
			Alerts.showAlert("Erro salvando objeto", classe, p.getMessage(), AlertType.ERROR);			
		}
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}

	private static void createReposicao(OrdemServico entity2) {
		String classe = "Reposição";
 		ReposicaoVeiculoService repService = new ReposicaoVeiculoService();
		ReposicaoVeiculo reposicao = new ReposicaoVeiculo();
		OrcVirtualService vService = new OrcVirtualService();
		List<OrcVirtual> listVir = vService.findAll();
System.out.println("os rep " + entity2.getNumeroOS());					
		listVir = vService.findByOrcto(entity2.getOrcamentoOS());
		try {
//			reposicao.setOsRep(entity2.getNumeroOS());
//			if (reposicao.getOsRep() != null) {
//				repService.remove(reposicao);
//			}	
			for (OrcVirtual ov : listVir) {
				if (ov.getNumeroOrcVir().equals(entity2.getOrcamentoOS())) {
					if (ov.getMaterial().getVidaKmMat() > 0 ||
							ov.getMaterial().getVidaMesMat() > 0) {
						reposicao.setNumeroRep(null);
						reposicao.setOsRep(entity2.getNumeroOS());
						reposicao.setDataRep(entity2.getDataOS());
						reposicao.setPlacaRep(entity2.getPlacaOS());
						reposicao.setClienteRep(entity2.getClienteOS());
						reposicao.setDddClienteRep(entity2.getOrcamento().getCliente().getDdd01Cli());
						reposicao.setTelefoneClienteRep(entity2.getOrcamento().getCliente().getTelefone01Cli());
						reposicao.setCodigoMaterialRep(ov.getMaterial().getCodigoMat());
						reposicao.setMaterialRep(ov.getMaterial().getNomeMat());
						reposicao.setKmTrocaRep(entity2.getKmOS());
						if (ov.getMaterial().getVidaKmMat() > 0) {
							reposicao.setProximaKmRep(entity2.getKmOS() +
							ov.getMaterial().getVidaKmMat());
						} else {			
							reposicao.setProximaKmRep(0);
							}
						if (ov.getMaterial().getVidaMesMat() > 0) {
							reposicao.setProximaDataRep(CalculaParcela.
									CalculaVencimentoMes(entity2.getDataOS(), 
											ov.getMaterial().getVidaMesMat()));
						} else {
							reposicao.setProximaDataRep(entity2.getDataOS());
							}
System.out.println("reposicao " + reposicao.getOsRep());						
						repService.insert(reposicao);
					}	
				}
			}
		}	
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	 }
	
	private static void updateKmVeiculo(OrdemServico entity2) {
		String classe = "Veiculo";
		Veiculo veiculo = new Veiculo();
		VeiculoService veicService = new VeiculoService();
		veiculo = veicService.findByPlaca(entity2.getPlacaOS());
System.out.println("upVei " + veiculo.getPlacaVei());		
		try {
			if (veiculo != null) {
				if (veiculo.getPlacaVei() == entity2.getPlacaOS()) {
					if (entity2.getKmOS() > veiculo.getKmFinalVei()) {
						veiculo.setKmInicialVei(veiculo.getKmFinalVei());
						veiculo.setKmFinalVei(entity2.getKmOS());
					}	
				} 
				else {
					veiculo.setPlacaVei(entity2.getPlacaOS());
					veiculo.setKmInicialVei(entity2.getKmOS());
					veiculo.setKmFinalVei(entity2.getKmOS());
					veiculo.setModeloVei(null);
					veiculo.setAnoVei(0);
				}	
			}	
			veicService.saveOrUpdate(veiculo);		
		} 
		catch (DbException e) {
			Alerts.showAlert("Erro salvando objeto", classe, e.getMessage(), AlertType.ERROR);
		}
	}
}
