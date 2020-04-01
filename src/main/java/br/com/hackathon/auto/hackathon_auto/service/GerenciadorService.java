package br.com.hackathon.auto.hackathon_auto.service;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import br.com.hackathon.auto.hackathon_auto.StatusFilasEnum;
import br.com.hackathon.auto.hackathon_auto.domain.Mensagem;
import br.com.hackathon.auto.hackathon_auto.util.Utils;

@Service
public class GerenciadorService {

	private static final Logger LOGGER = LogManager.getLogger(GerenciadorService.class);

	public static final String qName = "TREINAMENTO.RQ";

	@Autowired
	@Qualifier("mq")
	private JmsTemplate jmsTemplateMQ;

	// Listeners
	/**
	 * Metodo responsavel pelo Listener da Fila MQ
	 * 
	 * @param mensagemContent
	 *            - Contem a Mensagem para processamento
	 * @throws JAXBException
	 *             - {@link JAXBException}
	 * @throws Exception
	 *             - {@link Exception}
	 */
	@JmsListener(containerFactory = "mq_listener", destination = "JURIDICO.4GL.01R.RS")
	private void receiveMessage(final String mensagemContent) throws JAXBException, Exception {
		LOGGER.info("Mensagem Recebida: [fila]: JURIDICO.4GL.01R.RS [mensagem] " + mensagemContent);
		this.processarMensagem(mensagemContent);
	}

	/**
	 * Metodo responsavel pelo Listener da Fila SQS
	 * 
	 * @param mensagemContent
	 *            - Contem a Mensagem para processamento
	 * @throws Exception
	 *             - {@link Exception}
	 */
	@JmsListener(destination = "fila-entrada", containerFactory = "sqs_listener")
	public void processaMensagem(final String mensagemContent) throws Exception {
		try {
			this.processarMensagem(mensagemContent);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Metodo responsavel pela regra de roteamento das mensagens conforme status
	 * 
	 * @param mensagemContent
	 *            - Contem a Mensagem para processamento
	 * @throws JAXBException
	 *             - {@link JAXBException}
	 * @throws Exception
	 *             - {@link Exception}
	 */
	private void processarMensagem(final String mensagemContent) throws JAXBException, Exception {
		// Mensagem mensagem = objectMapper.readValue(mensagemContent, Mensagem.class);
		final Mensagem mensagem = Utils.jaxbXMLToObject(mensagemContent);
		mensagem.validar();

		LOGGER.info("Mensagem recebida =" + mensagem.toString());

		if (mensagem.validacaoMensagens.isEmpty()) {
			final StatusFilasEnum statusFilasEnum = StatusFilasEnum.getStatus_processos().get(mensagem.getStatus());
			this.enviarFilaMQ(statusFilasEnum.getFila(), mensagem);
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
			LOGGER.info("Mensagem enviada: [fila]: " + fila + " [mensagem] " + Utils.jaxbObjectToXML(mensagem));
		} catch (final Exception e) {
			throw e;
		}
	}
}
