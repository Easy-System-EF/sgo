package sgo.model.entities;

import java.io.Serializable;
import java.util.Date;

public class DadosFechamento implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroMensal;
	private Integer osMensal;
	private Integer balMensal;
	private Date dataMensal;
	private String clienteMensal;
	private String funcionarioMensal;
	private Double valorOsMensal;
	private Double valorMaterialMensal;
	private Double valorComissaoMensal;
	private Double valorResultadoMensal;
	private Double valorAcumuladoMensal;

	private Meses mes;
	private Anos ano;
		
	public DadosFechamento() {
	}

	public DadosFechamento(Integer numeroMensal, Integer osMensal, Integer balMensal, 
			Date dataMensal, String clienteMensal, String funcionarioMensal, Double valorOsMensal, 
			Double valorMaterialMensal, Double valorComissaoMensal, Double valorResultadoMensal, 
			Double valorAcumuladoMensal, Meses mes, Anos ano) {
		this.numeroMensal = numeroMensal;
		this.osMensal = osMensal;
		this.balMensal = balMensal;
		this.dataMensal = dataMensal;
		this.clienteMensal = clienteMensal;
		this.funcionarioMensal = funcionarioMensal;
		this.valorOsMensal = valorOsMensal;
		this.valorMaterialMensal = valorMaterialMensal;
		this.valorComissaoMensal = valorComissaoMensal;
		this.valorResultadoMensal = valorResultadoMensal;
		this.valorAcumuladoMensal = valorAcumuladoMensal;
		this.mes = mes;
		this.ano = ano;
	}

	public Integer getNumeroMensal() {
		return numeroMensal;
	}

	public void setNumeroMensal(Integer numeroMensal) {
		this.numeroMensal = numeroMensal;
	}

	public Integer getOsMensal() {
		return osMensal;
	}

	public void setOsMensal(Integer osMensal) {
		this.osMensal = osMensal;
	}

	public Integer getBalMensal() {
		return balMensal;
	}

	public void setBalMensal(Integer balMensal) {
		this.balMensal = balMensal;
	}

	public Date getDataMensal() {
		return dataMensal;
	}

	public void setDataMensal(Date dataMensal) {
		this.dataMensal = dataMensal;
	}

	public String getClienteMensal() {
		return clienteMensal;
	}

	public void setClienteMensal(String clienteMensal) {
		this.clienteMensal = clienteMensal;
	}

	public String getFuncionarioMensal() {
		return funcionarioMensal;
	}

	public void setFuncionarioMensal(String funcionarioMensal) {
		this.funcionarioMensal = funcionarioMensal;
	}

	public Double getValorOsMensal() {
		return valorOsMensal;
	}

	public void setValorOsMensal(Double valorOsMensal) {
		this.valorOsMensal = valorOsMensal;
	}

	public Double getValorMaterialMensal() {
		return valorMaterialMensal;
	}

	public void setValorMaterialMensal(Double valorMaterialMensal) {
		this.valorMaterialMensal = valorMaterialMensal;
	}

	public Double getValorComissaoMensal() {
		return valorComissaoMensal;
	}

	public void setValorComissaoMensal(Double valorComissaoMensal) {
		this.valorComissaoMensal = valorComissaoMensal;
	}

	public Double getValorResultadoMensal() {
		return valorResultadoMensal;
	}

	public void setValorResultadoMensal(Double valorResultadoMensal) {
		this.valorResultadoMensal = valorResultadoMensal;
	}

	public Double getValorAcumuladoMensal() {
		return valorAcumuladoMensal;
	}

	public void setValorAcumuladoMensal(Double valorAcumuladoMensal) {
		this.valorAcumuladoMensal = valorAcumuladoMensal;
	}

	public Meses getMes() {
		return mes;
	}

	public void setMes(Meses mes) {
		this.mes = mes;
	}

	public Anos getAno() {
		return ano;
	}

	public void setAno(Anos ano) {
		this.ano = ano;
	}

	public void resultadoMensal() {
		valorResultadoMensal = valorOsMensal - (valorMaterialMensal + valorComissaoMensal);
	}

	public void acumuladoMensal() {
		valorAcumuladoMensal += valorResultadoMensal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroMensal == null) ? 0 : numeroMensal.hashCode());
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
		DadosFechamento other = (DadosFechamento) obj;
		if (numeroMensal == null) {
			if (other.numeroMensal != null)
				return false;
		} else if (!numeroMensal.equals(other.numeroMensal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DadosFechamento [numeroMensal = " + numeroMensal + ", osMensal = " + osMensal + ", balMensal = " + balMensal
				+ ", dataMensal = " + dataMensal + ", clienteMensal = " + clienteMensal + ", funcionarioMensal = "
				+ funcionarioMensal + ", valorOsMensal = " + valorOsMensal + ", valorMaterialMensal = "
				+ valorMaterialMensal + ", valorComissaoMensal = " + valorComissaoMensal + ", valorResultadoMensal = "
				+ valorResultadoMensal + ", valorAcumuladoMensal = " + valorAcumuladoMensal + ", mes = " + mes + ", ano = "
				+ ano + "]";
	}
}
