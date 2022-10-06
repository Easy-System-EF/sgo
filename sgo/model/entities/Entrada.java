package sgo.model.entities;

import java.io.Serializable;
import java.util.Date;

import sgcp.model.entityes.Fornecedor;

public class Entrada implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer numeroEnt;
	private Integer nnfEnt;
	private Date dataEnt;
	private String nomeFornEnt;
	private String nomeMatEnt;
	private Double quantidadeMatEnt;
	private Double valorMatEnt;
	
	Fornecedor forn;
	Material mat;
	
	public Entrada() {
	}

	public Entrada(Integer numeroEnt, Integer nnfEnt, Date dataEnt, String nomeFornEnt, String nomeMatEnt,
			Double quantidadeMatEnt, Double valorMatEnt, Fornecedor forn, Material mat) {
 		this.numeroEnt = numeroEnt;
		this.nnfEnt = nnfEnt;
		this.dataEnt = dataEnt;
		this.nomeFornEnt = nomeFornEnt;
		this.nomeMatEnt = nomeMatEnt;
		this.quantidadeMatEnt = quantidadeMatEnt;
		this.valorMatEnt = valorMatEnt;
		this.forn = forn;
		this.mat = mat;
	}

	public Integer getNumeroEnt() {
		return numeroEnt;
	}

	public void setNumeroEnt(Integer numeroEnt) {
		this.numeroEnt = numeroEnt;
	}

	public Integer getNnfEnt() {
		return nnfEnt;
	}

	public void setNnfEnt(Integer nnfEnt) {
		this.nnfEnt = nnfEnt;
	}

	public Date getDataEnt() {
		return dataEnt;
	}

	public void setDataEnt(Date dataEnt) {
		this.dataEnt = dataEnt;
	}

	public String getNomeFornEnt() {
		return nomeFornEnt;
	}

	public void setNomeFornEnt(String nomeFornEnt) {
		this.nomeFornEnt = nomeFornEnt;
	}

	public String getNomeMatEnt() {
		return nomeMatEnt;
	}

	public void setNomeMatEnt(String nomeMatEnt) {
		this.nomeMatEnt = nomeMatEnt;
	}

	public Double getQuantidadeMatEnt() {
		return quantidadeMatEnt;
	}

	public void setQuantidadeMatEnt(Double quantidadeMatEnt) {
		this.quantidadeMatEnt = quantidadeMatEnt;
	}

	public Double getValorMatEnt() {
		return valorMatEnt;
	}

	public void setValorMatEnt(Double valorMatEnt) {
		this.valorMatEnt = valorMatEnt;
	}

	public Fornecedor getForn() {
		return forn;
	}

	public void setForn(Fornecedor forn) {
		this.forn = forn;
	}

	public Material getMat() {
		return mat;
	}

	public void setMat(Material mat) {
		this.mat = mat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroEnt == null) ? 0 : numeroEnt.hashCode());
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
		Entrada other = (Entrada) obj;
		if (numeroEnt == null) {
			if (other.numeroEnt != null)
				return false;
		} else if (!numeroEnt.equals(other.numeroEnt))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Entrada [numeroEnt=" + numeroEnt + ", nnfEnt=" + nnfEnt + ", dataEnt=" + dataEnt + ", nomeFornEnt="
				+ nomeFornEnt + ", nomeMatEnt=" + nomeMatEnt + ", quantidadeMatEnt=" + quantidadeMatEnt
				+ ", valorMatEnt=" + valorMatEnt + ", forn=" + forn + ", mat=" + mat + "]";
	}
 }
