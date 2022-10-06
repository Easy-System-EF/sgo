package sgo.model.entities;

import java.io.Serializable;
import java.util.Date;

public class ReposicaoVeiculo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroRep;
	private Integer osRep;
	private Date dataRep;
	private String placaRep;
	private String clienteRep;
	private Integer dddClienteRep;
	private Integer telefoneClienteRep;
	private Integer codigoMaterialRep;
	private String materialRep;
	private Integer kmTrocaRep;
	private Integer proximaKmRep;
	private Date proximaDataRep;

	public ReposicaoVeiculo() {
	}

	public ReposicaoVeiculo(Integer numeroRep, Integer osRep, Date dataRep, String placaRep, 
			String clienteRep, Integer dddClienteRep, Integer telefoneClienteRep,  
			Integer codigoMaterialRep, String materialRep, Integer kmTrocaRep, 
			Integer proximaKmRep, Date proximaDataRep) {
		this.numeroRep = numeroRep;
		this.osRep = osRep;
		this.dataRep = dataRep;
		this.placaRep = placaRep;
		this.clienteRep = clienteRep;
		this.dddClienteRep = telefoneClienteRep;
		this.telefoneClienteRep = telefoneClienteRep;
		this.codigoMaterialRep = codigoMaterialRep;
		this.materialRep = materialRep;
		this.kmTrocaRep = kmTrocaRep;
		this.proximaKmRep = proximaKmRep;
		this.proximaDataRep = proximaDataRep;
	}

	public Integer getNumeroRep() {
		return numeroRep;
	}

	public void setNumeroRep(Integer numeroRep) {
		this.numeroRep = numeroRep;
	}

	public Integer getOsRep() {
		return osRep;
	}

	public void setOsRep(Integer osRep) {
		this.osRep = osRep;
	}

	public Date getDataRep() {
		return dataRep;
	}

	public void setDataRep(Date dataRep) {
		this.dataRep = dataRep;
	}

	public String getPlacaRep() {
		return placaRep;
	}

	public void setPlacaRep(String placaRep) {
		this.placaRep = placaRep;
	}

	public String getClienteRep() {
		return clienteRep;
	}

	public void setClienteRep(String clienteRep) {
		this.clienteRep = clienteRep;
	}

	public Integer getDddClienteRep() {
		return dddClienteRep;
	}

	public void setDddClienteRep(Integer dddClienteRep) {
		this.dddClienteRep = dddClienteRep;
	}

	public Integer getTelefoneClienteRep() {
		return telefoneClienteRep;
	}

	public void setTelefoneClienteRep(Integer telefoneClienteRep) {
		this.telefoneClienteRep = telefoneClienteRep;
	}

	public Integer getCodigoMaterialRep() {
		return codigoMaterialRep;
	}

	public void setCodigoMaterialRep(Integer codigoMaterialRep) {
		this.codigoMaterialRep = codigoMaterialRep;
	}

	public String getMaterialRep() {
		return materialRep;
	}

	public void setMaterialRep(String materialRep) {
		this.materialRep = materialRep;
	}

	public Integer getKmTrocaRep() {
		return kmTrocaRep;
	}

	public void setKmTrocaRep(Integer kmTrocaRep) {
		this.kmTrocaRep = kmTrocaRep;
	}

	public Integer getProximaKmRep() {
		return proximaKmRep;
	}

	public void setProximaKmRep(Integer proximaKmRep) {
		this.proximaKmRep = proximaKmRep;
	}

	public Date getProximaDataRep() {
		return proximaDataRep;
	}

	public void setProximaDataRep(Date proximaDataRep) {
		this.proximaDataRep = proximaDataRep;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroRep == null) ? 0 : numeroRep.hashCode());
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
		ReposicaoVeiculo other = (ReposicaoVeiculo) obj;
		if (numeroRep == null) {
			if (other.numeroRep != null)
				return false;
		} else if (!numeroRep.equals(other.numeroRep))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReposicaoVeiculo [numeroRep = " + numeroRep + ", osRep = " + osRep + ", dataRep = " + dataRep + ", placaRep = "
				+ placaRep + ", clienteRep = " + clienteRep + ", dddClienteRep = " + dddClienteRep + ", telefoneClienteRep = "
				+ telefoneClienteRep + ", codigoMaterialRep = " + codigoMaterialRep + ", materialRep = " + materialRep
				+ ", kmTrocaRep = " + kmTrocaRep + ", proximaKmRep = " + proximaKmRep + ", proximaDataRep = " + proximaDataRep
				+ "]";
	}

}
