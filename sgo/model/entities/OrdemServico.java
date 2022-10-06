package sgo.model.entities;

import java.io.Serializable;
import java.util.Date;

public class OrdemServico implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroOS;
	private Date dataOS;
	private Integer orcamentoOS;
	private String placaOS;
	private String clienteOS;
	private Double valorOS;
	private Integer parcelaOS;
	private Integer prazoOS;
	private Integer pagamentoOS;
	private Date dataPrimeiroPagamentoOS;
	private Integer nnfOS;
	private String observacaoOS;
	private Integer kmOS;
	private Integer mesOs;
	private Integer anoOs;

	public Orcamento orcamento;

	public OrdemServico() {
	}

	public OrdemServico(Integer numeroOS, Date dataOS, Integer orcamentoOS, String placaOS, String clienteOS,
			Double valorOS, Integer parcelaOS, Integer prazoOS, Integer pagamentoOS, Date dataPrimeiroPagamentoOS,
			Integer nnfOS, String observacaoOS, Integer kmOS, Integer mesOs, Integer anoOs,
			Orcamento orcamento) {
		this.numeroOS = numeroOS;
		this.dataOS = dataOS;
		this.orcamentoOS = orcamentoOS;
		this.placaOS = placaOS;
		this.clienteOS = clienteOS;
		this.valorOS = valorOS;
		this.parcelaOS = parcelaOS;
		this.prazoOS = prazoOS;
		this.pagamentoOS = pagamentoOS;
		this.dataPrimeiroPagamentoOS = dataPrimeiroPagamentoOS;
		this.nnfOS = nnfOS;
		this.observacaoOS = observacaoOS;
		this.kmOS = kmOS;
		this.mesOs = mesOs;
		this.anoOs = anoOs;
		this.orcamento = orcamento;
	}

	public Integer getNumeroOS() {
		return numeroOS;
	}

	public void setNumeroOS(Integer numeroOS) {
		this.numeroOS = numeroOS;
	}

	public Date getDataOS() {
		return dataOS;
	}

	public void setDataOS(Date dataOS) {
		this.dataOS = dataOS;
	}

	public Integer getOrcamentoOS() {
		return orcamentoOS;
	}

	public void setOrcamentoOS(Integer orcamentoOS) {
		this.orcamentoOS = orcamentoOS;
	}

	public String getPlacaOS() {
		return placaOS;
	}

	public void setPlacaOS(String placaOS) {
		this.placaOS = placaOS;
	}

	public String getClienteOS() {
		return clienteOS;
	}

	public void setClienteOS(String clienteOS) {
		this.clienteOS = clienteOS;
	}

	public Double getValorOS() {
		return valorOS;
	}

	public void setValorOS(Double valorOS) {
		this.valorOS = valorOS;
	}

	public Integer getParcelaOS() {
		return parcelaOS;
	}

	public void setParcelaOS(Integer parcelaOS) {
		this.parcelaOS = parcelaOS;
	}

	public Integer getPrazoOS() {
		return prazoOS;
	}

	public void setPrazoOS(Integer prazoOS) {
		this.prazoOS = prazoOS;
	}

	public Integer getPagamentoOS() {
		return pagamentoOS;
	}

	public void setPagamentoOS(Integer pagamentoOS) {
		this.pagamentoOS = pagamentoOS;
	}

	public Date getDataPrimeiroPagamentoOS() {
		return dataPrimeiroPagamentoOS;
	}

	public void setDataPrimeiroPagamentoOS(Date dataPrimeiroPagamentoOS) {
		this.dataPrimeiroPagamentoOS = dataPrimeiroPagamentoOS;
	}

	public Integer getNnfOS() {
		return nnfOS;
	}

	public void setNnfOS(Integer nnfOS) {
		this.nnfOS = nnfOS;
	}

	public String getObservacaoOS() {
		return observacaoOS;
	}

	public void setObservacaoOS(String observacaoOS) {
		this.observacaoOS = observacaoOS;
	}

	public Integer getKmOS() {
		return kmOS;
	}

	public void setKmOS(Integer kmOS) {
		this.kmOS = kmOS;
	}

	public Integer getMesOs() {
		return mesOs;
	}

	public void setMesOs(Integer mesOs) {
		this.mesOs = mesOs;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public Integer getAnoOs() {
		return anoOs;
	}

	public void setAnoOs(Integer anoOs) {
		this.anoOs = anoOs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroOS == null) ? 0 : numeroOS.hashCode());
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
		OrdemServico other = (OrdemServico) obj;
		if (numeroOS == null) {
			if (other.numeroOS != null)
				return false;
		} else if (!numeroOS.equals(other.numeroOS))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrdemServico [numeroOS = " + numeroOS + ", dataOS = " + dataOS + ", orcamentoOS = " + orcamentoOS
				+ ", placaOS = " + placaOS + ", clienteOS = " + clienteOS + ", valorOS = " + valorOS + ", parcelaOS = "
				+ parcelaOS + ", prazoOS = " + prazoOS + ", pagamentoOS = " + pagamentoOS + ", dataPrimeiroPagamentoOS = "
				+ dataPrimeiroPagamentoOS + ", nnfOS = " + nnfOS + ", observacaoOS = " + observacaoOS + ", kmOS = " + kmOS
				+ ", mesOs = " + mesOs + ", anoOs = " + anoOs + ", orcamento = " + orcamento + "]";
	}
}