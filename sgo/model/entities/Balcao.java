   package sgo.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Balcao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroBal;
	private Date dataBal;
	private String FuncionarioBal;
	private Double descontoBal;
	private Double totalBal;
	private Integer pagamentoBal;
	private Date dataPrimeiroPagamentoBal;
	private Integer nnfBal;
	private String observacaoBal;
	private Integer mesBal;
	private Integer anoBal;

	private Funcionario funcionario;
 	
	public Balcao() {
	}


	public Balcao(Integer numeroBal, Date dataBal, String funcionarioBal, Double descontoBal, Double totalBal,
			Integer pagamentoBal, Date dataPrimeiroPagamentoBal, Integer nnfBal, String observacaoBal, Integer mesBal,
			Integer anoBal, Funcionario funcionario) {
		this.numeroBal = numeroBal;
		this.dataBal = dataBal;
		this.FuncionarioBal = funcionarioBal;
		this.descontoBal = descontoBal;
		this.totalBal = totalBal;
		this.pagamentoBal = pagamentoBal;
		this.dataPrimeiroPagamentoBal = dataPrimeiroPagamentoBal;
		this.nnfBal = nnfBal;
		this.observacaoBal = observacaoBal;
		this.mesBal = mesBal;
		this.anoBal = anoBal;
		this.funcionario = funcionario;
	}

	private static double vlr = 0.0;

	public Integer getNumeroBal() {
		return numeroBal;
	}

	public void setNumeroBal(Integer numeroBal) {
		this.numeroBal = numeroBal;
	}

	public Date getDataBal() {
		return dataBal;
	}

	public void setDataBal(Date dataBal) {
		this.dataBal = dataBal;
	}

	public String getFuncionarioBal() {
		return FuncionarioBal;
	}

	public void setFuncionarioBal(String funcionarioBal) {
		FuncionarioBal = funcionarioBal;
	}

	public Double getDescontoBal() {
		return descontoBal;
	}

	public void setDescontoBal(Double descontoBal) {
		this.descontoBal = descontoBal;
	}

	public Double getTotalBal() {
		return totalBal;
	}

	public void setTotalBal(Double totalBal) {
		this.totalBal = totalBal;
	}

	public Integer getPagamentoBal() {
		return pagamentoBal;
	}

	public void setPagamentoBal(Integer pagamentoBal) {
		this.pagamentoBal = pagamentoBal;
	}

	public Date getDataPrimeiroPagamentoBal() {
		return dataPrimeiroPagamentoBal;
	}

	public void setDataPrimeiroPagamentoBal(Date dataPrimeiroPagamentoBal) {
		this.dataPrimeiroPagamentoBal = dataPrimeiroPagamentoBal;
	}

	public Integer getNnfBal() {
		return nnfBal;
	}

	public void setNnfBal(Integer nnfBal) {
		this.nnfBal = nnfBal;
	}

	public String getObservacaoBal() {
		return observacaoBal;
	}

	public void setObservacaoBal(String observacaoBal) {
		this.observacaoBal = observacaoBal;
	}

	public Integer getMesBal() {
		return mesBal;
	}

	public void setMesBal(Integer mesBal) {
		this.mesBal = mesBal;
	}

	public Integer getAnoBal() {
		return anoBal;
	}

	public void setAnoBal(Integer anoBal) {
		this.anoBal = anoBal;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Double calculaTotalBal(List<OrcVirtual> listVir) {
		vlr = 0.0;
		totalBal = 0.0;
		for (OrcVirtual v : listVir) {
			if (v.getNumeroBalVir() == numeroBal) {
				if (v.getTotalMatVir() != null) {
					vlr += v.getTotalMatVir();
				}	
			}
		} 
		return totalBal = vlr - descontoBal;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroBal == null) ? 0 : numeroBal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Balcao other = (Balcao) obj;
		if (numeroBal == null) {
			if (other.numeroBal != null)
				return false;
		} else if (!numeroBal.equals(other.numeroBal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Balcao [numeroBal = " + numeroBal + ", dataBal = " + dataBal + ", FuncionarioBal = " + FuncionarioBal
				+ ", descontoBal = " + descontoBal + ", totalBal = " + totalBal + ", pagamentoBal = " + pagamentoBal
				+ ", dataPrimeiroPagamentoBal = " + dataPrimeiroPagamentoBal + ", nnfBal = " + nnfBal + ", observacaoBal = "
				+ observacaoBal + ", mesBal = " + mesBal + ", anoBal = " + anoBal 
				+ ", funcionario = " + funcionario + "]";
	}
}
