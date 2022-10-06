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

public class ConvVir3 implements Serializable{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
 	 	OrcVirtual vir = new OrcVirtual();
 	 	OrcVirtualService virService = new OrcVirtualService();
 	 	List<OrcVirtual> listVir = virService.findAll();
 	 	for (OrcVirtual v : listVir) {
System.out.println(v.getNumeroVir()); 	 		
 	 		if (v.getMaterial().getNomeMat() == "Mão de obra WS") {
 	 			v.setPrecoMatVir(v.getTotalMatVir());
 	 			vir = (OrcVirtual) v;
 	 			virService.saveOrUpdate(vir);
 	 		}
 	 	}
	}	 
}
