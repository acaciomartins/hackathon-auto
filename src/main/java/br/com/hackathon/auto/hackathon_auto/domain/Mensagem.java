package br.com.hackathon.auto.hackathon_auto.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mensagem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String origem;
	private String status;
	private Long documento;

	public List<String> validacaoMensagens = new ArrayList<>();

	public String getOrigem() {
		return this.origem;
	}

	public void setOrigem(final String origem) {
		this.origem = origem;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public Long getDocumento() {
		return this.documento;
	}

	public void setDocumento(final Long documento) {
		this.documento = documento;
	}

	@Override
	public String toString() {
		return "Mensagem{" + "origem=" + origem + ", status='" + status + '\'' + ", documento=" + documento + '}';
	}

	public void validar() {
		if (this.origem == null || this.origem.equals("")) {
			validacaoMensagens.add("Origem zuada.");
		}

		if (this.status == null || this.status.equals("")) {
			validacaoMensagens.add("Status zuada.");
		}

		if (this.documento == null) {
			validacaoMensagens.add("Documento zuado.");
		}
	}

}
