package br.com.hackathon.auto.hackathon_auto.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "mq")
@XmlType(name = "mq", propOrder = { "status", "origemPrincipal", "numeroPrincipal", "tipoProcesso" })
public class Mensagem implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer status;
	private Integer origemPrincipal;
	private Integer numeroPrincipal;
	private String tipoProcesso;
	@XmlTransient
	public List<String> validacaoMensagens = new ArrayList<>();

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOrigemPrincipal() {
		return origemPrincipal;
	}

	public void setOrigemPrincipal(Integer origemPrincipal) {
		this.origemPrincipal = origemPrincipal;
	}

	public Integer getNumeroPrincipal() {
		return numeroPrincipal;
	}

	public void setNumeroPrincipal(Integer numeroPrincipal) {
		this.numeroPrincipal = numeroPrincipal;
	}

	public String getTipoProcesso() {
		return tipoProcesso;
	}

	public void setTipoProcesso(String tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	@Override
	public String toString() {
		return "Mensagem{" + "status=" + status + ", origemPrincipal=" + origemPrincipal + ", numeroPrincipal="
				+ numeroPrincipal + ", tipoProcesso=" + tipoProcesso + '}';
	}

	public void validar() {
		if (this.status == null) {
			validacaoMensagens.add("Status informado inválido.");
		}

		if (this.origemPrincipal == null) {
			validacaoMensagens.add("Origem Principal inválido.");
		}

		if (this.numeroPrincipal == null) {
			validacaoMensagens.add("Número Principal inválido.");
		}

		if (this.tipoProcesso == null || this.tipoProcesso.isEmpty()) {
			validacaoMensagens.add("Tipo Processo inválido.");
		}
	}

}
