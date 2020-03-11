package br.com.hackathon.auto.hackathon_auto.service;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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

@Service
public class GerenciadorService {

	private static final Logger logger = LogManager.getLogger(GerenciadorService.class);
	private ObjectMapper objectMapper = new ObjectMapper();

	public static final String qName = "TREINAMENTO.RQ";

//	@Autowired
//	private JmsTemplate jmsTemplate;

	@Autowired
	@Qualifier("mq")
	private JmsTemplate jmsTemplateMQ;

	
	@JmsListener(destination = "fila-entrada")
	public void processaMensagem(String mensagemContent) throws Exception {
		try {
//			Mensagem mensagem = objectMapper.readValue(mensagemContent, Mensagem.class);
			final Mensagem mensagem = jaxbXMLToObject(mensagemContent);
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
			jmsTemplateMQ.convertAndSend(fila, jaxbObjectToXML(mensagem).toString());
			logger.info("Mensagem enviada: [fila]: " + fila + " [mensagem] " + mensagem.toString());
		} catch (final Exception e) {
			throw e;
		}
	}
	
	private static StringWriter jaxbObjectToXML(final Mensagem  mensagem) throws JAXBException {
		final JAXBContext jaxbContext = JAXBContext.newInstance(Mensagem.class);
		final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		final StringWriter sw = new StringWriter();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		jaxbMarshaller.marshal(mensagem, sw);
		
		return sw;
	}
	
	private static Mensagem jaxbXMLToObject(final String xml) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Mensagem.class);    
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Mensagem mensagem = (Mensagem) jaxbUnmarshaller.unmarshal(new StringReader(xml));
	     
	    
		return mensagem;
	}

}
