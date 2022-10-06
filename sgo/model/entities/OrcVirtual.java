package sgo.model.entities;

import java.io.Serializable;

/*
 * orçamento virtual para mater preço número de materiais
 */

public class OrcVirtual implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroVir;
	private String nomeMatVir;
	private Double quantidadeMatVir;
	private Double precoMatVir;
	private Double totalMatVir;
	private Integer numeroOrcVir;
	private Integer numeroBalVir;

	private Material material;
//	OrcVirtual [numeroVir = 13981, nomeMatVir = SUBSTITUIR CORREIA DENT. FORN, quantidadeMatVir = 1.0, 
//	precoMatVir = 0.0, totalMatVir = 0.0, numeroOrcVir = 17555, numeroBalVir = 0, 
//	material = Material [codigoMat = 4522, grupoMat = 1, nomeMat = SUBSTITUIR CORREIA DENT. FORN , 
//	entradaMat = 1.0, saidaMat = 1.0, saldoMat = 0.0, precoMat = 0.0, vendaMat = 0.0, 
//	vidaKmMat = 0, vidaMesMat = 0, cmmMat = 1.0, dataCadastroMat = null, grupo = null]]
	
	public OrcVirtual() {
	}

	public OrcVirtual(Integer numeroVir, String nomeMatVir, Double quantidadeMatVir, 
			Double precoMatVir, Double totalMatVir, Integer numeroOrcVir, 
			Integer numeroBalVir, Material material) {
		this.numeroVir = numeroVir;
		this.nomeMatVir = nomeMatVir;
		this.quantidadeMatVir = quantidadeMatVir;
		this.precoMatVir = precoMatVir;
		this.totalMatVir = totalMatVir;
		this.numeroOrcVir = numeroOrcVir;
		this.numeroBalVir = numeroBalVir;
		this.material = material;
	}

	public Integer getNumeroVir() {
		return numeroVir;
	}

	public void setNumeroVir(Integer numeroVir) {
		this.numeroVir = numeroVir;
	}

	public String getNomeMatVir() {
		return nomeMatVir;
	}

	public void setNomeMatVir(String nomeMatVir) {
		this.nomeMatVir = nomeMatVir;
	}

	public Double getQuantidadeMatVir() {
		return quantidadeMatVir;
	}

	public void setQuantidadeMatVir(Double quantidadeMatVir) {
		this.quantidadeMatVir = quantidadeMatVir;
	}

	public Double getPrecoMatVir() {
		return precoMatVir;
	}

	public void setPrecoMatVir(Double precoMatVir) {
		this.precoMatVir = precoMatVir;
	}

	public Double getTotalMatVir() {
		return totalMatVir = totalMatVir;
	}

	public void setTotalMatVir(Double totalMatVir) {
		this.totalMatVir = totalMatVir;
	}

	public Integer getNumeroOrcVir() {
		return numeroOrcVir;
	}

	public void setNumeroOrcVir(Integer numeroOrcVir) {
		this.numeroOrcVir = numeroOrcVir;
	}

	public Integer getNumeroBalVir() {
		return numeroBalVir;
	}

	public void setNumeroBalVir(Integer numeroBalVir) {
		this.numeroBalVir = numeroBalVir;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroOrcVir == null) ? 0 : numeroOrcVir.hashCode());
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
		OrcVirtual other = (OrcVirtual) obj;
		if (numeroOrcVir == null) {
			if (other.numeroOrcVir != null)
				return false;
		} else if (!numeroOrcVir.equals(other.numeroOrcVir))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrcVirtual [numeroVir = " + numeroVir + ", nomeMatVir = " + nomeMatVir + ", quantidadeMatVir = "
				+ quantidadeMatVir + ", precoMatVir = " + precoMatVir + ", totalMatVir = " + totalMatVir + ", numeroOrcVir = "
				+ numeroOrcVir + ", numeroBalVir = " + numeroBalVir + ", material = " + material + "]";
	}
}
