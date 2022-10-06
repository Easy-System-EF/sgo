package cobol_java;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sgo.model.entities.Adiantamento;
import sgo.model.entities.Funcionario;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Orcamento;
import sgo.model.entities.OrdemServico;
import sgo.model.services.AdiantamentoService;
import sgo.model.services.FuncionarioService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.OrcamentoService;
import sgo.model.services.OrdemServicoService;

public class ConvComissao implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dataHj = new Date();
			String classe = "";
 	 		String pathOs = "c:\\ARQS\\CGOTXT\\ATXTOS.TXT";
 	 		Calendar cal = Calendar.getInstance();
 	 		int dd = 0;
 	 		int mm = 0;
 	 		int mi = 2000;
 	 		int aa = 0;
 	 		int num = 0; 	 				
 	 		gravaComissaoOS();
	}	

	private static void gravaComissaoOS() {
		String classe = "Adiantamento ";
		Funcionario fun = new Funcionario();
		FuncionarioService funService = new FuncionarioService();
		OrcVirtual vir = new OrcVirtual();
		OrcVirtualService virService = new OrcVirtualService();
		List<OrcVirtual> listVir = new ArrayList<>();
		Adiantamento adi = new Adiantamento();
		AdiantamentoService adiService = new AdiantamentoService();
		Orcamento orc = new Orcamento();
		OrcamentoService orcService = new OrcamentoService();
		OrdemServico os = new OrdemServico();
		OrdemServicoService osService = new OrdemServicoService();
		List<OrdemServico> listOs = osService.findAll();
		for (OrdemServico os1 : listOs) {
System.out.println(os1.getOrcamentoOS());			
			orc = orcService.findById(os1.getOrcamentoOS());
			listVir = virService.findByOrcto(orc.getNumeroOrc());
			for (OrcVirtual v : listVir) {
System.out.println(v.getMaterial().getGrupo().getNomeGru());	
				if (v.getMaterial().getGrupo().getNomeGru().contains("Mão de obra")) {
System.out.println("entrou ");					
					double mao = v.getTotalMatVir();
					fun = funService.findById(orc.getFuncionario().getCodigoFun());
					adi.setDataAdi(os1.getDataOS());
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
					adi.setOsAdi(os1.getNumeroOS());
					adi.setBalcaoAdi(0);
					adi.setTipoAdi("C");
					adi.setFuncionario(fun);
					adi.setCargo(fun.getCargo());
					adi.setSituacao(fun.getSituacao());
					adi.calculaComissao(mao);
System.out.println(mao + " " + adi);			
//			adiService.saveOrUpdate(adi);
				}
			}	
		}
	}	
}