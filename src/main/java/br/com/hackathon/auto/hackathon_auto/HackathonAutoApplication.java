package br.com.hackathon.auto.hackathon_auto;

import java.util.Date;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
//@EnableJms
public class HackathonAutoApplication {

	public static final String qName = "DEV.QUEUE.1"; // A queue from the default MQ Developer container config
	@Value("${ibm.mq.queueName}")
	private static String queueName;

	public static void main(String[] args) throws JMSException {
//		SpringApplication.run(HackathonAutoApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(HackathonAutoApplication.class, args);

		JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("mq");
		String msg = "Hello from IBM MQ at " + new Date();

		jmsTemplate.convertAndSend(qName, msg);
//		   MQQueue queue = new MQQueue(qName);
//	        jmsTemplate.send(queue, session -> session.createTextMessage("A message you want to send"));
		status();
	}

	static void status() {
		System.out.println();
		System.out.println("========================================");
		System.out.println("MQ JMS Sample started. Message sent to queue: " + HackathonAutoApplication.qName);
		System.out.println("========================================");
	}

}
