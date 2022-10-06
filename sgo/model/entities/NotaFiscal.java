package sgo.model.entities;

import java.io.Serializable;

public class NotaFiscal implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer codigoNF;
	private Integer numeroNF;
	private Integer balcaoNF;
	private Integer osNF;
	
	public NotaFiscal() {
	}

	public NotaFiscal(Integer codigoNF, Integer numeroNF,Integer balcaoNF, Integer osNF) {
		this.codigoNF = codigoNF;
		this.numeroNF = numeroNF;
		this.balcaoNF = balcaoNF;
		this.osNF = osNF;
	}

	public Integer getCodigoNF() {
		return codigoNF;
	}

	public void setCodigoNF(Integer codigoNF) {
		this.codigoNF = codigoNF;
	}

	public Integer getNumeroNF() {
		return numeroNF;
	}

	public void setNumeroNF(Integer numeroNF) {
		this.numeroNF = numeroNF;
	}

	public Integer getBalcaoNF() {
		return balcaoNF;
	}

	public void setBalcaoNF(Integer balcaoNF) {
		this.balcaoNF = balcaoNF;
	}

	public Integer getOsNF() {
		return osNF;
	}

	public void setOsNF(Integer osNF) {
		this.osNF = osNF;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroNF == null) ? 0 : numeroNF.hashCode());
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
		NotaFiscal other = (NotaFiscal) obj;
		if (numeroNF == null) {
			if (other.numeroNF != null)
				return false;
		} else if (!numeroNF.equals(other.numeroNF))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NotaFiscal [codigoNF = " + codigoNF + ", numeroNF = " + numeroNF + ", balcaoNF = " + balcaoNF + ", osNF = "
				+ osNF + "]";
	}
}
