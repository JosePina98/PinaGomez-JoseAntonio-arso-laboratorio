package com.rabbitmq.client;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Reciever {

	public static List<JsonObject> recieveMessages() throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setUri("amqp://bawartxa:v9KM0UrC9D8qCJxmRyhClrjJlVOEd8gA@squid.rmq.cloudamqp.com/bawartxa");

	    Connection connection = factory.newConnection();

	    final Channel channel = connection.createChannel();
	    
	    final String queueName = "arso-queue";
	    
	    List<JsonObject> lista = new LinkedList<JsonObject>();

	    boolean autoAck = false;
	    channel.basicConsume(queueName, autoAck, "arso-consumidor", new DefaultConsumer(channel) {
	    	@Override
	    	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
	    			byte[] body) throws IOException {
	    		
	    		long deliveryTag = envelope.getDeliveryTag();
	    		
	    		String contenido = new String(body, "UTF-8");
	    		
	    		JsonReader jsonReader = Json.createReader(new StringReader(contenido));
	    		JsonObject objeto = jsonReader.readObject();
	    		
	    		lista.add(objeto);
	    		
	    		channel.basicAck(deliveryTag, false);
	    	}
	    });
	    
	    return lista;
	}

}
