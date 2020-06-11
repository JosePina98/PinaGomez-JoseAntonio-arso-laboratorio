package repositories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class UsuariosRepository {

	private static final String URL_SERVICIO = "http://localhost:8081/api/usuarios/estudiantes";
	
	public static List<String> getAllEstudiantes() throws IOException {
		String peticion = URL_SERVICIO;
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
			JsonArray array = jsonReader.readArray();
			jsonReader.close();

			List<String> listaRespuesta = new LinkedList<String>();
			for (int i = 0; i < array.size(); i++) {
				JsonObject object = array.getJsonObject(i);
				listaRespuesta.add(object.getString("correo"));
			}
			return listaRespuesta;
		}
	}
}
