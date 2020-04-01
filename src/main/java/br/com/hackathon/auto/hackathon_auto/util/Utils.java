package br.com.hackathon.auto.hackathon_auto.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.com.hackathon.auto.hackathon_auto.domain.Mensagem;

public final class Utils {
	public static String jaxbObjectToXML(final Mensagem mensagem) throws JAXBException {
		final JAXBContext jaxbContext = JAXBContext.newInstance(Mensagem.class);
		final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		final StringWriter sw = new StringWriter();
		sw.append("<?xml_version='1.0'_encoding='ISO-8859-1'?>");
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		jaxbMarshaller.marshal(mensagem, sw);
		return sw.toString().replaceAll("\n", "").replaceAll(" ", "").replaceAll("_", " ");
	}

	public static Mensagem jaxbXMLToObject(final String xml) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Mensagem.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Mensagem mensagem = (Mensagem) jaxbUnmarshaller.unmarshal(new StringReader(xml));
		return mensagem;
	}
}
