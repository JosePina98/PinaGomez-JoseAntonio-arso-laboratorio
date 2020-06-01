package repositories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class UsuariosRepository {

	private static final String URL_SERVICIO = "http://localhost:8081/api/usuarios/";
	
	public static boolean existeEstudiante(String correo) throws IOException {
		String peticion = URL_SERVICIO + correo;
		URL url = new URL(peticion);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		
		int responsecode = conn.getResponseCode();
		if (responsecode != 200) {
			throw new RuntimeException("HttpResponseCode: " + responsecode);
		} else {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		     String inputLine;
		     StringBuffer response = new StringBuffer();
		     while ((inputLine = in.readLine()) != null) {
		     	response.append(inputLine);
		     }
		     in.close();
		     
		     JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
		     JsonObject object = jsonReader.readObject();
		     jsonReader.close();

		     String rol = object.getString("rol");
		     if (rol.equalsIgnoreCase("ESTUDIANTE")) {
		    	 return true;
		     } else {
		    	 return false;
		     }
		}
	}
	
	public static boolean existeProfesor(String correo) throws IOException {
		String peticion = URL_SERVICIO + correo;
		URL url = new URL(peticion);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		
		int responsecode = conn.getResponseCode();
		if (responsecode != 200) {
			return false;
		} else {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		     String inputLine;
		     StringBuffer response = new StringBuffer();
		     while ((inputLine = in.readLine()) != null) {
		     	response.append(inputLine);
		     }
		     in.close();
		     
		     JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
		     JsonObject object = jsonReader.readObject();
		     jsonReader.close();

		     String rol = object.getString("rol");
		     if (rol.equalsIgnoreCase("PROFESOR")) {
		    	 return true;
		     } else {
		    	 return false;
		     }
		}
	}
}
