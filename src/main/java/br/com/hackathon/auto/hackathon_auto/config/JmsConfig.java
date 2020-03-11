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

@Configuration
@EnableJms
public class JmsConfig {
	@Value("${amazon.accessKey}")
	private String accessKey;
	@Value("${amazon.secretKey}")
	private String secretKey;

	@Autowired
	private SQSConnectionFactory connectionFactory;

//	@Value("${ibm.mq.queueManager}")
//	private String queueManagerName;

//	@Value("${ibm.mq.channel}")
//	private String channel;

//	@Value("${ibm.mq.connName}")
//	private String connName;

//	@Value("${ibm.mq.queueName}")
//	private String queueName;

//	@Value("${ibm.mq.clientmode.localAddress}")
//	private String localAddress;

//	@Value("${ibm.mq.clientmode.port}")
//	private Integer port;

	// SQS AWS \\
	@Bean
	public SQSConnectionFactory createConnectionFactory() {
		return SQSConnectionFactory.builder().withRegion(Region.getRegion(Regions.US_EAST_1))
				.withAWSCredentialsProvider(
						new StaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.build();
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(this.connectionFactory);
		factory.setDestinationResolver(new DynamicDestinationResolver());
		factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);

		return factory;
	}

//	@Bean
//	@Primary
//	public JmsTemplate defaultJmsTemplate() {
//		return new JmsTemplate(this.connectionFactory);
//	}

	@Bean("mq")
	public JmsTemplate jmsTemplate(@Autowired MQQueueConnectionFactory mqQueueConnectionFactory) throws JMSException {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(mqQueueConnectionFactory);
		return jmsTemplate;
	}

	/**
	 * Setting up the MQQueueConnectionFactory in a Client mode
	 */
	@Bean
	@Qualifier("clientmode")
	public MQQueueConnectionFactory ibmMQConnectionFactoryInClientMode() throws JMSException {
		final MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
		mqQueueConnectionFactory.setQueueManager("QMLI1062");
		mqQueueConnectionFactory.setTransportType(1);
		mqQueueConnectionFactory.setConnectionNameList("LI1062");
		mqQueueConnectionFactory.setChannel("SRVLI1062");

		return mqQueueConnectionFactory;
	}
}
