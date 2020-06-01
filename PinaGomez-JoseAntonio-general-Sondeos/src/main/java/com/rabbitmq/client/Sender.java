package com.rabbitmq.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import exceptions.InternalException;
import schema.Sondeo;

public class Sender {

	private static void enviar(String mensaje) throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setUri("amqp://bawartxa:v9KM0UrC9D8qCJxmRyhClrjJlVOEd8gA@squid.rmq.cloudamqp.com/bawartxa");

	    Connection connection = factory.newConnection();

	    Channel channel = connection.createChannel();

	    final String exchangeName = "arso-exchange";
	    final String queueName = "arso-queue";
	    final String routingKey = "arso-queue";

	    try {
	        boolean durable = true;
	        channel.exchangeDeclare(exchangeName, "direct", durable);

	        boolean exclusive = false;
	        boolean autodelete = false;
	        Map<String, Object> properties = null;
	        channel.queueDeclare(queueName, durable, exclusive, autodelete, properties);    

	        channel.queueBind(queueName, exchangeName, routingKey);

	        channel.basicPublish(exchangeName, routingKey, 
	                new AMQP.BasicProperties.Builder()
	                    .contentType("application/json")
	                    .build()                
	                , mensaje.getBytes());
	        
	        channel.close();
	        connection.close();
	    } catch (IOException e) {

	        mensaje = e.getMessage() == null ? e.getCause().getMessage() : e.getMessage();

	        System.out.println("No se ha podido establecer la conexion con el exchange o la cola: \n\t->" + mensaje);
	        return;
	    }
	}

	public static void notificarEventoNuevoSondeo(Sondeo sondeo) throws InternalException, IOException {
		
		String mensaje = "{ \"profesor\" : \"" + sondeo.getDocenteId() + "\", \"sondeo\" : \"" + sondeo.getId() +
				"\", \"servicio\" : \"sondeos\" }";
		
		try {
			enviar(mensaje);
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error interno del sistema\" }");
		}
	}

}
