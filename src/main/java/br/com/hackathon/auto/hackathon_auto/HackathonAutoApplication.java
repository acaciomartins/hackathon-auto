package br.com.hackathon.auto.hackathon_auto;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HackathonAutoApplication {

	@Value("${ibm.mq.queueName}")
	private static String queueName;

	public static void main(String[] args) throws JMSException {
		SpringApplication.run(HackathonAutoApplication.class, args);
	}

}
