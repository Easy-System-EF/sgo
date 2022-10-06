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
import java.util.Date;
import java.util.List;

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
import sgo.model.entities.Grupo;
import sgo.model.entities.Material;
import sgo.model.entities.OrcVirtual;
import sgo.model.entities.Orcamento;
import sgo.model.entities.OrdemServico;
import sgo.model.entities.Receber;
import sgo.model.entities.ReposicaoVeiculo;
import sgo.model.entities.Veiculo;
import sgo.model.services.GrupoService;
import sgo.model.services.MaterialService;
import sgo.model.services.OrcVirtualService;
import sgo.model.services.OrcamentoService;
import sgo.model.services.ReceberService;
import sgo.model.services.ReposicaoVeiculoService;
import sgo.model.services.VeiculoService;

public class ConvVir2 implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
			String classe = "";
 	 		String pathOs = "c:\\ARQS\\CGOTXT\\ATXTOS.TXT";
 	 		Orcamento orc = new Orcamento();
 	 		OrcamentoService orcService = new OrcamentoService();
 	 		OrcVirtual vir = new OrcVirtual();
 	 		OrcVirtualService virService = new OrcVirtualService();
 	 		Material mat = new Material();
 	 		MaterialService matService = new MaterialService();
 	 		mat = matService.findById(6443);
 	 		Grupo gru = new Grupo();
 	 		GrupoService gruService = new GrupoService();
 	 		
 	 				
 	 		try {
	 	 		BufferedReader brOs = new BufferedReader(new FileReader(pathOs));
	 			String lineOs = brOs.readLine();
 	 	 		while (lineOs != null) { 
	 	 			String[] campos = lineOs.split(";");
	 	 			if (Integer.parseInt(campos[3]) > 0) {
	 	 				int orcto = (Integer.parseInt(campos[1]));
	 	 				orc = orcService.findById(orcto);
System.out.println(orc.getOsOrc());	 	 				
	 	 				vir.setNumeroVir(null);
	 	 				vir.setNomeMatVir("Mão de obra WS");
	 	 				vir.setQuantidadeMatVir(1.00);
	 	 				vir.setPrecoMatVir(0.00);
	 	 				vir.setTotalMatVir((Double.parseDouble(campos[3]) / 100));
	 	 				vir.setNumeroOrcVir(orc.getNumeroOrc());
	 	 				vir.setNumeroBalVir(0);
	 	 				vir.setMaterial(mat);
	 	 				virService.saveOrUpdate(vir);
	 	 			}	
	 	 			lineOs = brOs.readLine();
 	 	 		}
 	 	 		brOs.close();
	 	 	}
 	 		catch (FileNotFoundException e) {
 	 			throw new DbException (e.getMessage());
 	 		}
 	 		catch (IOException e) {
 	 			throw new DbException (e.getMessage());
 	 		}
	}	
	 
}
