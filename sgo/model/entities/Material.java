package sgo.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Material implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer codigoMat;
	private Integer grupoMat;
	private String nomeMat;
	private double entradaMat;
	private double saidaMat;
	private double saldoMat;
	private double precoMat;
	private double vendaMat;
	private Integer vidaKmMat;
	private Integer vidaMesMat;
	private double cmmMat;
	private Date dataCadastroMat;

  	private Grupo grupo;

	public Material() {
	}

	public Material(Integer codigoMat, Integer grupoMat, String nomeMat, double entradaMat, 
				double saidaMat, double saldoMat, double precoMat, double vendaMat, Integer vidaKmMat, 
				Integer vidaMesMat, double cmmMat, Date dataCadastroMat, Grupo grupo) {
 		this.codigoMat = codigoMat; 
		this.grupoMat = grupoMat;
		this.nomeMat = nomeMat;
		this.entradaMat = entradaMat;
		this.saidaMat = saidaMat;
		this.saldoMat = saldoMat;
		this.precoMat = precoMat;
		this.vendaMat = vendaMat;
		this.vidaKmMat = vidaKmMat;
		this.vidaMesMat = vidaMesMat;
		this.cmmMat = cmmMat;
		this.dataCadastroMat = dataCadastroMat;
		this.grupo = grupo;
	}

	public Integer getCodigoMat() {
		return codigoMat;
	}

	public void setCodigoMat(Integer codigoMat) {
		this.codigoMat = codigoMat;
	}

	public Integer getGrupoMat() {
		return grupoMat;
	}

	public void setGrupoMat(Integer grupoMat) {
		this.grupoMat = grupoMat;
	}

	public String getNomeMat() {
		return nomeMat;
	}

	public void setNomeMat(String nomeMat) {
		this.nomeMat = nomeMat;
	}

	public double getEntradaMat() {
		return entradaMat;
	}

	public void setEntradaMat(double entradaMat) {
		this.entradaMat =  entradaMat;
	}

	public double getSaidaMat() {
		return saidaMat;
	}

	public void setSaidaMat(double saidaMat) {
		this.saidaMat = saidaMat;
	}

	public double getSaldoMat() {
		return saldoMat = entradaMat - saidaMat;
	}

	public double getPrecoMat() {
		return precoMat;
	}

	public void setPrecoMat(double precoMat) {
		this.precoMat = precoMat;
	}

	public double getVendaMat() {
		return vendaMat;
	}

	public void setVendaMat(double vendaMat) {
		this.vendaMat = vendaMat;
	}

	public Integer getVidaKmMat() {
		return vidaKmMat;
	}

	public void setVidaKmMat(Integer vidaKmMat) {
		this.vidaKmMat = vidaKmMat;
	}

	public Integer getVidaMesMat() {
		return vidaMesMat;
	}

	public void setVidaMesMat(Integer vidaMesMat) {
		this.vidaMesMat = vidaMesMat;
	}

	public double getCmmMat() {
		return cmmMat;
	}

	public void setCmmMat(double result) {
		this.cmmMat = result;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	
	public double calculaCmm() {
		double result = 0.00;
		Date dataHoje = new Date();
		int meses = 0;
  		long dif = dataHoje.getTime() - dataCadastroMat.getTime();
  		long  dias = TimeUnit.DAYS.convert(dif, TimeUnit.MILLISECONDS);
  		if (dias > 30) {
  			meses = (int) (dias / 30);
  			result = saidaMat / meses;
  		}
  		else {
  			result = saidaMat;
  		}
  			
			return result;
	}

	public Date getDataCadastroMat() {
		return dataCadastroMat;
	}

	public void setDataCadastroMat(Date dataCadastroMat) {
		this.dataCadastroMat = dataCadastroMat;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoMat == null) ? 0 : codigoMat.hashCode());
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
		Material other = (Material) obj;
		if (codigoMat == null) {
			if (other.codigoMat != null)
				return false;
		} else if (!codigoMat.equals(other.codigoMat))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Material [codigoMat = " + codigoMat + ", grupoMat = " + grupoMat + ", nomeMat = " + nomeMat + ", entradaMat = "
				+ entradaMat + ", saidaMat = " + saidaMat + ", saldoMat = " + saldoMat + ", precoMat = " + precoMat
				+ ", vendaMat = " + vendaMat + ", vidaKmMat = " + vidaKmMat + ", vidaMesMat = " + vidaMesMat + ", cmmMat = "
				+ cmmMat + ", dataCadastroMat = " + dataCadastroMat + ", grupo = " + grupo + "]";
	}
 	
// 		Date dt  = new Date();
// 		Calendar cal = Calendar.getInstance();
// 		cal.setTime(dt);
// 		int aa = cal.get(Calendar.YEAR);
// 		int mm = cal.get(Calendar.MONTH);
}
