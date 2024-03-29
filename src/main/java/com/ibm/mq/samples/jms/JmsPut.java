/*
* (c) Copyright IBM Corporation 2018
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.ibm.mq.samples.jms;


import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

/**
 * A minimal and simple application for Point-to-point messaging.
 *
 * Application makes use of fixed literals, any customisations will require
 * re-compilation of this source file. Application assumes that the named queue
 * is empty prior to a run.
 *
 * Notes:
 *
 * API type: JMS API (v2.0, simplified domain)
 *
 * Messaging domain: Point-to-point
 *
 * Provider type: IBM MQ
 *
 * Connection mode: Client connection
 *
 * JNDI in use: No
 *
 */
public class JmsPut {

	// System exit status value (assume unset value to be 1)
	private static int status = 1;

	// Create variables for the connection to MQ
//	private static final String CONNECTION_NAME_LIST = "192.168.223.9(1419)"; // SIT
	private static final String CONNECTION_NAME_LIST = "192.168.223.4(1419),192.168.223.8(1419)"; // UAT
	private static final String CHANNEL = "EPM.SSL.SVRCONN"; // Channel name
	private static final String QMGR = "EMQ01"; // Queue manager name
	private static final String APP_USER = "appepm"; // Username that application uses to connect to MQ
	private static final String APP_PASSWORD = "BKSapr#5"; // Password that the application uses to connect to MQ
	private static final String QUEUE_NAME = "EPM.SUB.EVENT.PDPA.DISPOSAL.INFO.KMA"; // Queue that the application uses to put and get messages to and from
//	private static final String TOPIC_NAME = "EVENT/PDPA/DISPOSAL/INFO/KMA"; // Topic that the application uses to pub and sub messages to and from

	/**
	 * Main method
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		// System.setProperty("javax.net.ssl.trustStore", "/Dev/cert/truststore.jks");

		String trustStore = System.getProperty("user.dir") + "/keystore/truststore.jks";
		System.out.println("trustStore: " + trustStore);
		System.out.println("CONNECTION_NAME_LIST: " + CONNECTION_NAME_LIST);

		System.setProperty("javax.net.ssl.trustStore", trustStore); // change path on your computer
		System.setProperty("javax.net.ssl.trustStorePassword", "P@ssw0rd");
		System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "false");

		// Variables
		JMSContext context = null;
		Destination destination = null;
		JMSProducer producer = null;
		JMSConsumer consumer = null;

//		String kmaMsg = "{\n" +
//				"    \"trackingReference\": \"BAYPDP20230223\",\n" +
//				"    \"items\": [\n" +
//				"        {\n" +
//				"            \"trackingID\": \"BAYPDP20230223000001\",\n" +
//				"            \"CIF\": \"000000000000001\",\n" +
//				"            \"CustID\": \"KMA_USERNAME_1\",\n" +
//				"            \"REF\": null\n" +
//				"        },\n" +
//				"        {\n" +
//				"            \"trackingID\": \"BAYPDP20230223000002\",\n" +
//				"            \"CIF\": \"000000000000002\",\n" +
//				"            \"CustID\": \"KMA_USERNAME_2\",\n" +
//				"            \"REF\": \"REFERENCE_FROM_KMA_BACKEND\"\n" +
//				"        }\n" +
//				"    ]\n" +
//				"}";

//        String kmaMsg = "{\n" +
//                "  \"trackingReference\": \"BAYPDP20230615\",\n" +
//                "  \"items\": [\n" +
//                "    {\n" +
//                "      \"trackingId\": \"BAYPDP20230615000001\",\n" +
//                "      \"cifNumber\": \"00000001083075\",\n" +
//                "      \"customerId\": \"SHREYUNI\",\n" +
//                "      \"kmaReferenceId\": null\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"trackingId\": \"BAYPDP20230615000002\",\n" +
//                "      \"cifNumber\": \"00000030040719\",\n" +
//                "      \"customerId\": \"@RMU9B3ECEEC6A404A\",\n" +
//                "      \"kmaReferenceId\": \"BAYPDPA23061500003\"\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}";

		String kmaMsg = "{\n" +
				"  \"trackingReference\": \"BAYPDP20230629\",\n" +
				"  \"items\": [\n" +
				"    {\n" +
				"      \"trackingId\": \"BAYPDP20230629000001\",\n" +
				"      \"cifNumber\": \"00000001083075\",\n" +
				"      \"customerId\": \"uatew12\",\n" +
				"      \"kmaReferenceId\": null\n" +
				"    },\n" +
				"    {\n" +
				"      \"trackingId\": \"BAYPDP20230629000002\",\n" +
				"      \"cifNumber\": \"00000001083076\",\n" +
				"      \"customerId\": \"kkhun01\",\n" +
				"      \"kmaReferenceId\": null\n" +
				"    },\n" +
				"    {\n" +
				"      \"trackingId\": \"BAYPDP20230629000003\",\n" +
				"      \"cifNumber\": \"00000001083077\",\n" +
				"      \"customerId\": \"linepay3\",\n" +
				"      \"kmaReferenceId\": null\n" +
				"    },\n" +
				"    {\n" +
				"      \"trackingId\": \"BAYPDP20230629000004\",\n" +
				"      \"cifNumber\": \"00000001083078\",\n" +
				"      \"customerId\": \"ifin04\",\n" +
				"      \"kmaReferenceId\": null\n" +
				"    },\n" +
				"    {\n" +
				"      \"trackingId\": \"BAYPDP20230629000005\",\n" +
				"      \"cifNumber\": \"00000030040719\",\n" +
				"      \"customerId\": \"@RMU9B3ECEEC6A404A\",\n" +
				"      \"kmaReferenceId\": \"BAYPDP2023062900005\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"trackingId\": \"BAYPDP20230629000006\",\n" +
				"      \"cifNumber\": \"00000030040720\",\n" +
				"      \"customerId\": \"@RMU9B3ECEEC6A404B\",\n" +
				"      \"kmaReferenceId\": \"BAYPDP2023062900006\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"trackingId\": \"BAYPDP20230629000007\",\n" +
				"      \"cifNumber\": \"00000030040721\",\n" +
				"      \"customerId\": \"@RMU9B3ECEEC6A404C\",\n" +
				"      \"kmaReferenceId\": \"BAYPDP2023062900007\"\n" +
				"    }\n" +
				"  ]\n" +
				"}\n";

		try {
			// Create a connection factory
			JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
			JmsConnectionFactory cf = ff.createConnectionFactory();

			// Set the properties
			// cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
			// cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
			cf.setStringProperty(WMQConstants.WMQ_CONNECTION_NAME_LIST, CONNECTION_NAME_LIST);
			cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
			cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
			cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
			cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
			cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
			cf.setStringProperty(WMQConstants.USERID, APP_USER);
			cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);
			cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384");
			//cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, "*TLS12");

			// Create JMS objects
			context = cf.createContext();
			destination = context.createQueue("queue:///" + QUEUE_NAME);

			long uniqueNumber = System.currentTimeMillis() % 1000;
			//TextMessage message = context.createTextMessage("Your lucky number today is " + uniqueNumber);
			TextMessage message = context.createTextMessage(kmaMsg);

			producer = context.createProducer();
			producer.send(destination, message);
			System.out.println("Sent message:\n" + message);

			context.close();

			recordSuccess();
		} catch (JMSException jmsex) {
			recordFailure(jmsex);
		}

		System.exit(status);

	}

	/**
	 * Record this run as successful.
	 */
	private static void recordSuccess() {
		System.out.println("SUCCESS");
		status = 0;
		return;
	}

	/**
	 * Record this run as failure.
	 *
	 * @param ex
	 */
	private static void recordFailure(Exception ex) {
		if (ex != null) {
			if (ex instanceof JMSException) {
				processJMSException((JMSException) ex);
			} else {
				System.out.println(ex);
			}
		}
		System.out.println("FAILURE");
		status = -1;
		return;
	}

	/**
	 * Process a JMSException and any associated inner exceptions.
	 *
	 * @param jmsex
	 */
	private static void processJMSException(JMSException jmsex) {
		System.out.println(jmsex);
		Throwable innerException = jmsex.getLinkedException();
		if (innerException != null) {
			System.out.println("Inner exception(s):");
		}
		while (innerException != null) {
			System.out.println(innerException);
			innerException = innerException.getCause();
		}
		return;
	}

}
