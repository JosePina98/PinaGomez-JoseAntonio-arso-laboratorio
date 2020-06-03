package com.rabbitmq.client;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Reciever {

	public static JsonObject recieveMessage() throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setUri("amqp://bawartxa:v9KM0UrC9D8qCJxmRyhClrjJlVOEd8gA@squid.rmq.cloudamqp.com/bawartxa");

	    Connection connection = factory.newConnection();

	    final Channel channel = connection.createChannel();
	    
	    final String queueName = "arso-queue";
	    
	    boolean autoAck = false;
	    
	    GetResponse response = channel.basicGet(queueName, autoAck);
	    if (response == null) {
	    	return null;
	    } else {
	    	
	    	String contenido = new String(response.getBody(), "UTF-8");
    		JsonReader jsonReader = Json.createReader(new StringReader(contenido));
    		JsonObject objeto = jsonReader.readObject();
    		
    		long deliveryTag = response.getEnvelope().getDeliveryTag();
    		channel.basicAck(deliveryTag, false);

    		return objeto;
	    }
	    
	}

}
