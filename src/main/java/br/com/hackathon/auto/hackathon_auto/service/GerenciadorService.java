package br.com.hackathon.auto.hackathon_auto.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hackathon.auto.hackathon_auto.StatusFilasEnum;
import br.com.hackathon.auto.hackathon_auto.domain.Mensagem;
import br.com.hackathon.auto.hackathon_auto.util.Utils;

@Service
public class GerenciadorService {

	private static final Logger logger = LogManager.getLogger(GerenciadorService.class);
	private ObjectMapper objectMapper = new ObjectMapper();

	public static final String qName = "TREINAMENTO.RQ";

	// @Autowired
	// private JmsTemplate jmsTemplate;

	@Autowired
	@Qualifier("mq")
	private JmsTemplate jmsTemplateMQ;

	@Autowired
	@Qualifier("mq2")
	private JmsTemplate jmsTemplateMQ2;

	@JmsListener(destination = "fila-entrada")
	public void processaMensagem(String mensagemContent) throws Exception {
		try {
			// Mensagem mensagem = objectMapper.readValue(mensagemContent, Mensagem.class);
			final Mensagem mensagem = Utils.jaxbXMLToObject(mensagemContent);
			mensagem.validar();

			logger.info("Mensagem recebida =" + mensagem.toString());

			if (mensagem.validacaoMensagens.isEmpty()) {
				// Regra dos Status x Filas
				// Depois repassa a mensagem para a fila desejada
				final StatusFilasEnum statusFilasEnum = StatusFilasEnum.getStatus_processos().get(mensagem.getStatus());
				this.enviarFilaMQ(statusFilasEnum.getFila(), mensagem);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Metodo responsavel por enviar Mensagem para a fila MQ
	 * 
	 * @param message
	 * @throws Exception
	 */
	private void enviarFilaMQ(final String fila, final Mensagem mensagem) throws Exception {
		try {
			jmsTemplateMQ.convertAndSend("queue:///" + fila + "?targetClient=1",
					Utils.jaxbObjectToXML(mensagem).toString());
			logger.info("Mensagem enviada: [fila]: " + fila + " [mensagem] " + Utils.jaxbObjectToXML(mensagem));
		} catch (final Exception e) {
			throw e;
		}
	}

//	@JmsListener(containerFactory = "mq",destination = "JURIDICO.4GL.01R.RS")
//	private void receiveMessage(String message) {
//		System.out.println("DEV.QUEUE.2 received ~" + message + "~");
//	}

}
