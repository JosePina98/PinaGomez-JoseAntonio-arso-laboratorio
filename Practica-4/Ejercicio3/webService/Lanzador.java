package webService;

import javax.xml.ws.Endpoint;

public class Lanzador {

	public static void main(String[] args) {
		Endpoint.publish("http://localhost:9999/ws/parseInteger", new ParseInteger());
	}
}
