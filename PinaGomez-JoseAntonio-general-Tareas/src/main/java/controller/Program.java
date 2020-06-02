package controller;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Program {

	public static void main(String[] args) throws IOException {
		final String consulta = "{getPublicationsByName{authorName}}";
		String consultaCodificada = URLEncoder.encode(consulta, "UTF-8");

		String urlString = "http://localhost:8080/graphql?query=" + consultaCodificada;

		URL url = new URL(urlString);
		        
		JsonReader jsonReader = Json.createReader(url.openStream());
		JsonObject obj = jsonReader.readObject();
		    
		System.out.println(obj.toString());
	}
}
