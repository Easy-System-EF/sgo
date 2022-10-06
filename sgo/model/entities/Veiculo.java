package sgo.model.entities;

import java.io.Serializable;

public class Veiculo implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroVei;
	private String placaVei;
	private Integer kmInicialVei;
	private Integer kmFinalVei;
	private String modeloVei;
	private Integer anoVei;
	
	public Veiculo () {
	}

	public Veiculo(Integer numeroVei, String placaVei, Integer kmInicialVei, Integer kmFinalVei, 
			String modeloVei, Integer anoVei) {
		this.numeroVei = numeroVei;
		this.placaVei = placaVei;
		this.kmInicialVei = kmInicialVei;
		this.kmFinalVei = kmFinalVei;
		this.modeloVei = modeloVei;
		this.anoVei = anoVei;
	}

	public Integer getNumeroVei() {
		return numeroVei;
	}

	public void setNumeroVei(Integer numeroVei) {
		this.numeroVei = numeroVei;
	}

	public String getPlacaVei() {
		return placaVei;
	}

	public void setPlacaVei(String placaVei) {
		this.placaVei = placaVei;
	}

	public Integer getKmInicialVei() {
		return kmInicialVei;
	}

	public void setKmInicialVei(Integer kmInicialVei) {
		this.kmInicialVei = kmInicialVei;
	}

	public Integer getKmFinalVei() {
		return kmFinalVei;
	}

	public void setKmFinalVei(Integer kmFinalVei) {
		this.kmFinalVei = kmFinalVei;
	}

	public String getModeloVei() {
		return modeloVei;
	}

	public void setModeloVei(String modeloVei) {
		this.modeloVei = modeloVei;
	}

	public Integer getAnoVei() {
		return anoVei;
	}

	public void setAnoVei(Integer anoVei) {
		this.anoVei = anoVei;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((placaVei == null) ? 0 : placaVei.hashCode());
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
		Veiculo other = (Veiculo) obj;
		if (placaVei == null) {
			if (other.placaVei != null)
				return false;
		} else if (!placaVei.equals(other.placaVei))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Veiculo [numeroVei = " + numeroVei + ", placaVei = " + placaVei + ", kmInicialVei = " + kmInicialVei
				+ ", kmFinalVei = " + kmFinalVei + ", modeloVei = " + modeloVei + ", anoVei = " + anoVei + "]";
	}
}
