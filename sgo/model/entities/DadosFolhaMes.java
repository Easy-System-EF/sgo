package sgo.model.entities;

public class DadosFolhaMes {

	private Integer numeroDados;
	private String nomeDados;
	private String cargoDados;
	private String situacaoDados;
	private String salarioDados;
	private String comissaoDados;
	private String valeDados;
	private String receberDados;
	private String totalDados;
	
	private Meses mes;
	private Anos ano;

	public DadosFolhaMes() {
		
	}

	public DadosFolhaMes(Integer numeroDados, String nomeDados, String cargoDados, String situacaoDados, String salarioDados,
			String comissaoDados, String valeDados, String receberDados, String totalDados, 
			Meses mes, Anos ano) {
		this.numeroDados = numeroDados;
		this.nomeDados = nomeDados;
		this.cargoDados = cargoDados;
		this.situacaoDados = situacaoDados;
		this.salarioDados = salarioDados;
		this.comissaoDados = comissaoDados;
		this.valeDados = valeDados;
		this.receberDados = receberDados;
		this.totalDados = totalDados;
		this.mes = mes;
		this.ano = ano;
	}

	public Integer getNumeroDados() {
		return numeroDados;
	}

	public void setNumeroDados(Integer numeroDados) {
		this.numeroDados = numeroDados;
	}

	public String getNomeDados() {
		return nomeDados;
	}

	public void setNomeDados(String nomeDados) {
		this.nomeDados = nomeDados;
	}

	public String getCargoDados() {
		return cargoDados;
	}

	public void setCargoDados(String cargoDados) {
		this.cargoDados = cargoDados;
	}

	public String getSituacaoDados() {
		return situacaoDados;
	}

	public void setSituacaoDados(String situacaoDados) {
		this.situacaoDados = situacaoDados;
	}

	public String getSalarioDados() {
		return salarioDados;
	}

	public void setSalarioDados(String salarioDados) {
		this.salarioDados = salarioDados;
	}

	public String getComissaoDados() {
		return comissaoDados;
	}

	public void setComissaoDados(String comissaoDados) {
		this.comissaoDados = comissaoDados;
	}

	public String getValeDados() {
		return valeDados;
	}

	public void setValeDados(String valeDados) {
		this.valeDados = valeDados;
	}

	public String getReceberDados() {
		return receberDados;
	}

	public void setReceberDados(String receberDados) {
		this.receberDados = receberDados;
	}

	public String getTotalDados() {
		return totalDados;
	}

	public void setTotalDados(String totalDados) {
		this.totalDados = totalDados;
	}

	public Meses getMeses() {
		return mes;
	}

	public void setMeses(Meses mes) {
		this.mes = mes;
	}

	public Anos getAnos() {
		return ano;
	}

	public void setAnos(Anos ano) {
		this.ano = ano;
	}

	@Override
	public String toString() {
		return "DadosFolhaMes [numeroDados = " + numeroDados + ", nomeDados = " + nomeDados + ", cargoDados = " + cargoDados + ", situacaoDados = "
				+ situacaoDados + ", salarioDados = " + salarioDados + ", comissaoDados = " + comissaoDados + ", valeDados = "
				+ valeDados + ", receberDados = " + receberDados + ", totalDados = " + totalDados + ", mes = " + mes + ", ano = " + ano + "]";
	}
}
