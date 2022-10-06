package sgcp.model.entityes.consulta;

import java.io.Serializable;
import java.util.Date;

import sgcp.model.entityes.Fornecedor;
import sgcp.model.entityes.TipoFornecedor;

public class ParPeriodo implements Serializable {

 	private static final long serialVersionUID = 1L;

 	private Integer idPeriodo;
 	private Date dtiPeriodo;
 	private Date dtfPeriodo;
 	
 	private Fornecedor fornecedor;
 	private TipoFornecedor tipoFornecedor;
 	
 	public ParPeriodo() {
 	}

	public ParPeriodo(Integer IdPeriodo, Date dtiPeriodo, Date dtfPeriodo, Fornecedor fornecedor, TipoFornecedor tipoFornecedor) {
 		this.idPeriodo = idPeriodo;
 		this.dtiPeriodo = dtiPeriodo;
		this.dtfPeriodo = dtfPeriodo;
		this.fornecedor = fornecedor;
		this.tipoFornecedor = tipoFornecedor;
	}
 	
	public Integer getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public Date getDtiPeriodo() {
		return dtiPeriodo;
	}

	public void setDtiPeriodo(Date dtiPeriodo) {
		this.dtiPeriodo = dtiPeriodo;
	}

	public Date getDtfPeriodo() {
		return dtfPeriodo;
	}

	public void setDtfPeriodo(Date dtfPeriodo) {
		this.dtfPeriodo = dtfPeriodo;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public TipoFornecedor getTipoFornecedor() {
		return tipoFornecedor;
	}

	public void setTipoFornecedor(TipoFornecedor tipoFornecedor) {
		this.tipoFornecedor = tipoFornecedor;
	}

	@Override
	public String toString() {
		return "ParPeriodo [idPeriodo = " + idPeriodo + ", dtiPeriodo = " + dtiPeriodo + ", dtfPeriodo = " + dtfPeriodo
				+ ", fornecedor = " + fornecedor + ", tipoFornecedor = " + tipoFornecedor + "]";
	}
	
 }