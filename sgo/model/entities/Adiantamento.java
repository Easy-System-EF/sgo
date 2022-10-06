package sgo.model.entities;

import java.io.Serializable;
import java.util.Date;

public class Adiantamento implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroAdi;
	private Date dataAdi;
	private Integer codFunAdi;
	private String nomeFunAdi;
	private String cargoAdi;
	private String situacaoAdi;
	private Double valorAdi;
	private Integer mesAdi;
	private Integer anoAdi;
	private Double comissaoAdi;
	private Integer osAdi;
	private Integer balcaoAdi;
	private String tipoAdi;

	private Funcionario funcionario;
	private Cargo cargo;
	private Situacao situacao;
	
	public Adiantamento() {		
	}

	public Adiantamento(Integer numeroAdi, Date dataAdi, Integer codFunAdi, String nomeFunAdi, String cargoAdi,
			String situacaoAdi, Double valorAdi, Integer mesAdi, Integer anoAdi, Double comissaoAdi, Integer osAdi,
			Integer balcaoAdi,String tipoAdi, Funcionario funcionario, Cargo cargo, Situacao situacao) {
		this.numeroAdi = numeroAdi;
		this.dataAdi = dataAdi;
		this.codFunAdi = codFunAdi;
		this.nomeFunAdi = nomeFunAdi;
		this.cargoAdi = cargoAdi;
		this.situacaoAdi = situacaoAdi;
		this.valorAdi = valorAdi;
		this.mesAdi = mesAdi;
		this.anoAdi = anoAdi;
		this.comissaoAdi = comissaoAdi;
		this.osAdi = osAdi;
		this.balcaoAdi = balcaoAdi;
		this.tipoAdi = tipoAdi;
		this.funcionario = funcionario;
		this.cargo = cargo;
		this.situacao = situacao;
	}

	public Integer getNumeroAdi() {
		return numeroAdi;
	}

	public void setNumeroAdi(Integer numeroAdi) {
		this.numeroAdi = numeroAdi;
	}

	public Date getDataAdi() {
		return dataAdi;
	}

	public void setDataAdi(Date dataAdi) {
		this.dataAdi = dataAdi;
	}

	public Integer getCodFunAdi() {
		return codFunAdi;
	}

	public void setCodFunAdi(Integer codFunAdi) {
		this.codFunAdi = codFunAdi;
	}

	public String getNomeFunAdi() {
		return nomeFunAdi;
	}

	public void setNomeFunAdi(String nomeFunAdi) {
		this.nomeFunAdi = nomeFunAdi;
	}

	public String getCargoAdi() {
		return cargoAdi;
	}

	public void setCargoAdi(String cargoAdi) {
		this.cargoAdi = cargoAdi;
	}

	public String getSituacaoAdi() {
		return situacaoAdi;
	}

	public void setSituacaoAdi(String situacaoAdi) {
		this.situacaoAdi = situacaoAdi;
	}

	public Double getValorAdi() {
		return valorAdi;
	}

	public void setValorAdi(Double valorAdi) {
		this.valorAdi = valorAdi;
	}

	public Integer getMesAdi() {
		return mesAdi;
	}

	public void setMesAdi(Integer mesAdi) {
		this.mesAdi = mesAdi;
	}

	public Integer getAnoAdi() {
		return anoAdi;
	}

	public void setAnoAdi(Integer anoAdi) {
		this.anoAdi = anoAdi;
	}

	public Double getComissaoAdi() {
		return comissaoAdi;
	}

	public void setComissaoAdi(Double comissaoAdi) {
		this.comissaoAdi = comissaoAdi;
	}

	public Integer getOsAdi() {
		return osAdi;
	}

	public void setOsAdi(Integer osAdi) {
		this.osAdi = osAdi;
	}

	public Integer getBalcaoAdi() {
		return balcaoAdi;
	}

	public void setBalcaoAdi(Integer balcaoAdi) {
		this.balcaoAdi = balcaoAdi;
	}

	public String getTipoAdi() {
		return tipoAdi;
	}

	public void setTipoAdi(String tipoAdi) {
		this.tipoAdi = tipoAdi;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public void calculaComissao(Double vlr) {
		comissaoAdi = (vlr * cargo.getComissaoCargo()) / 100;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroAdi == null) ? 0 : numeroAdi.hashCode());
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
		Adiantamento other = (Adiantamento) obj;
		if (numeroAdi == null) {
			if (other.numeroAdi != null)
				return false;
		} else if (!numeroAdi.equals(other.numeroAdi))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Adiantamento [numeroAdi = " + numeroAdi + ", dataAdi = " + dataAdi + ", codFunAdi = " + codFunAdi
				+ ", nomeFunAdi = " + nomeFunAdi + ", cargoAdi = " + cargoAdi + ", situacaoAdi = " + situacaoAdi
				+ ", valorAdi = " + valorAdi + ", mesAdi = " + mesAdi + ", anoAdi = " + anoAdi + ", comissaoAdi = "
				+ comissaoAdi + ", osAdi = " + osAdi + ", balcaoAdi = " + balcaoAdi + ", tipoAdi = " + tipoAdi
				+ ", funcionario = " + funcionario + ", cargo = " + cargo + ", situacao = " + situacao + "]";
	}
}