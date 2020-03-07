package br.com.hackathon.auto.hackathon_auto.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hackathon.auto.hackathon_auto.domain.Mensagem;

@Service
public class GerenciadorService {

	private static final Logger logger = LogManager.getLogger(GerenciadorService.class);
	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private JmsTemplate jmsTemplate;

	// fila-entrada
	// fila-saida

	// fila que vai receber
	@JmsListener(destination = "fila-entrada")
	public void processaMensagem(String mensagemContent) throws Exception {
		try {
			Mensagem mensagem = objectMapper.readValue(mensagemContent, Mensagem.class);
			mensagem.validar();
			logger.info("Mensagem recebida =" + mensagem.toString());

			if (mensagem.validacaoMensagens.isEmpty()) {
				// Regra dos Status x Filas
				// Depois repassa a mensagem para a fila desejada
				this.repassarMensagemParaFila("{\"origem\": " + mensagem.getOrigem() + "\"status\": "
						+ mensagem.getStatus() + "\"documento\": " + mensagem.getDocumento() + "}");
			} else {
				logger.info("Mensagem inválida");
				// Decidir o que fazer
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void repassarMensagemParaFila(String message) {
		try {
			jmsTemplate.convertAndSend("fila-saida", message);
		} catch (Exception e) {
			throw e;
		}
	}

	// MQ
//	static boolean warned = false;
//
//	@JmsListener(destination = HackathonAutoApplication.qName)
//	public void receiveMessage(String msg) {
//		infinityWarning();
//
//		System.out.println();
//		System.out.println("========================================");
//		System.out.println("Received message is: " + msg);
//		System.out.println("========================================");
//
//	}
//
//	void infinityWarning() {
//		if (!warned) {
//			warned = true;
//			System.out.println();
//			System.out.println("========================================");
//			System.out.println("MQ JMS Listener started for queue: " + HackathonAutoApplication.qName);
//			System.out.println("NOTE: This program does not automatically end - it continues to wait");
//			System.out.println("      for more messages, so you may need to hit BREAK to end it.");
//			System.out.println("========================================");
//		}
//	}
}
