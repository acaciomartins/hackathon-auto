package br.com.hackathon.auto.hackathon_auto.config;

import javax.jms.JMSException;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

@Configuration
@EnableJms
public class JmsConfig {
	@Value("${amazon.accessKey}")
	private String accessKey;
	@Value("${amazon.secretKey}")
	private String secretKey;

	@Autowired
	private SQSConnectionFactory connectionFactory;

	@Autowired 
	private MQQueueConnectionFactory mqQueueConnectionFactory;

	//Connections factory
	@Bean
	public SQSConnectionFactory createConnectionFactory() {
		return SQSConnectionFactory.builder().withRegion(Region.getRegion(Regions.US_EAST_1))
				.withAWSCredentialsProvider(
						new StaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.build();
	}
	
	@Bean
	@Qualifier("clientmode")
	public MQQueueConnectionFactory ibmMQConnectionFactoryInClientMode() throws JMSException {
		final MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
		mqQueueConnectionFactory.setQueueManager("QMLI1850");
		mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
		mqQueueConnectionFactory.setConnectionNameList("172.26.163.71");
		mqQueueConnectionFactory.setChannel("SRVLI1850");

		return mqQueueConnectionFactory;
	}

	//Default Listeners
	@Bean(name= "sqs_listener")
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(this.connectionFactory);
		factory.setDestinationResolver(new DynamicDestinationResolver());
		factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);

		return factory;
	}

	@Bean(name= "mq_listener")
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactoryMQ() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(this.mqQueueConnectionFactory);
		factory.setDestinationResolver(new DynamicDestinationResolver());
		factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);

		return factory;
	}
	
	//Jms Templates
	@Bean("mq")
//	public JmsTemplate jmsTemplate(@Autowired MQQueueConnectionFactory mqQueueConnectionFactory) throws JMSException {
	public JmsTemplate jmsTemplate() throws JMSException {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(this.mqQueueConnectionFactory);
		return jmsTemplate;
	}
}
