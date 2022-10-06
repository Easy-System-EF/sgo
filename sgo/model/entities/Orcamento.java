package sgo.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Orcamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numeroOrc;
	private Date dataOrc;
	private String clienteOrc;
	private String funcionarioOrc;
	private String placaOrc;
	private Integer kmInicialOrc;
	private Integer kmFinalOrc;
	private Double descontoOrc;
	private Double totalOrc;
	private Integer osOrc;

 	private Cliente cliente;
	private Funcionario funcionario;
	private Veiculo veiculo;
	private OrcVirtual virtual;
 	
	public Orcamento() {
	}

	public Orcamento(Integer numeroOrc, Date dataOrc, String clienteOrc, String funcionarioOrc, String placaOrc,
			Integer kmInicialOrc, Integer kmFinalOrc, Double descontoOrc, Double totalOrc, 
			Integer osOrc, Cliente cliente, Funcionario funcionario, Veiculo veiculo, 
			OrcVirtual virtual) {
		this.numeroOrc = numeroOrc;
		this.dataOrc = dataOrc;
		this.clienteOrc = clienteOrc;
		this.funcionarioOrc = funcionarioOrc;
		this.placaOrc = placaOrc;
		this.kmInicialOrc = kmInicialOrc;
		this.kmFinalOrc = kmFinalOrc;
		this.descontoOrc = descontoOrc;
		this.totalOrc = totalOrc;
		this.osOrc = osOrc;
		this.cliente = cliente;
		this.funcionario = funcionario;
		this.veiculo = veiculo;
		this.virtual = virtual;
	}

	private static double vlr = 0.0;

	public Integer getNumeroOrc() {
		return numeroOrc;
	}

	public void setNumeroOrc(Integer numeroOrc) {
		this.numeroOrc = numeroOrc;
	}

	public Date getDataOrc() {
		return dataOrc;
	}

	public void setDataOrc(Date dataOrc) {
		this.dataOrc = dataOrc;
	}

	public String getClienteOrc() {
		return clienteOrc;
	}

	public void setClienteOrc(String clienteOrc) {
		this.clienteOrc = clienteOrc;
	}

	public String getFuncionarioOrc() {
		return funcionarioOrc;
	}

	public void setFuncionarioOrc(String funcionarioOrc) {
		this.funcionarioOrc = funcionarioOrc;
	}

	public String getPlacaOrc() {
		return placaOrc;
	}

	public void setPlacaOrc(String placaOrc) {
		this.placaOrc = placaOrc;
	}

	public Integer getKmInicialOrc() {
		return kmInicialOrc;
	}

	public void setKmInicialOrc(Integer kmInicialOrc) {
		this.kmInicialOrc = kmInicialOrc;
	}

	public Integer getKmFinalOrc() {
		return kmFinalOrc;
	}

	public void setKmFinalOrc(Integer kmFinalOrc) {
		this.kmFinalOrc = kmFinalOrc;
	}

	public Double getDescontoOrc() {
		return descontoOrc;
	}

	public void setDescontoOrc(Double descontoOrc) {
		this.descontoOrc = descontoOrc;
	}

	public Double getTotalOrc() {
		return totalOrc;
	}

	public void setTotalOrc(Double totalOrc) {
		this.totalOrc = totalOrc;
	}

	public Integer getOsOrc() {
		return osOrc;
	}

	public void setOsOrc(Integer osOrc) {
		this.osOrc = osOrc;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public OrcVirtual getVirtual() {
		return virtual;
	}

	public void setVirtual(OrcVirtual virtual) {
		this.virtual = virtual;
	}

	public static double getVlr() {
		return vlr;
	}

	public static void setVlr(double vlr) {
		Orcamento.vlr = vlr;
	}

	public Double calculaTotalOrc(List<OrcVirtual> listVir) {
		vlr = 0.0;
		totalOrc = 0.0;
		for (OrcVirtual v : listVir) {
			if (v.getNumeroOrcVir().equals(numeroOrc)) {
					vlr += v.getTotalMatVir();
			}
		} 
		totalOrc = vlr - descontoOrc;
		return vlr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroOrc == null) ? 0 : numeroOrc.hashCode());
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
		Orcamento other = (Orcamento) obj;
		if (numeroOrc == null) {
			if (other.numeroOrc != null)
				return false;
		} else if (!numeroOrc.equals(other.numeroOrc))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Orcamento [numeroOrc = " + numeroOrc + ", dataOrc = " + dataOrc + ", clienteOrc = " + clienteOrc
				+ ", funcionarioOrc = " + funcionarioOrc + ", placaOrc = " + placaOrc + ", kmInicialOrc = " + kmInicialOrc
				+ ", kmFinalOrc = " + kmFinalOrc + ", descontoOrc = " + descontoOrc + ", totalOrc = " + totalOrc + ", osOrc = "
				+ osOrc + ", cliente = " + cliente + ", funcionario = " + funcionario + ", veiculo = " + veiculo
				+ ", virtual = " + virtual + "]";
	}
}
