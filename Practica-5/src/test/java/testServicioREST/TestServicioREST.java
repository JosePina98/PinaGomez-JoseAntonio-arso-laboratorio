package testServicioREST;

import static org.junit.jupiter.api.Assertions.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import bookle.tipos.Actividad;
import bookle.tipos.ItemActividad;
import bookle.tipos.ListadoActividades;

class TestServicioREST {
	
	public final static String URL_SERVICIO = "http://localhost:8080/api/";
	
	public Client cliente;
	
//	@BeforeEach
//	void cleanDataBase() {
//		File dir = new File("actividades/");
//
//		for (File file : dir.listFiles()) {
//			if (!file.isDirectory()) {
//				System.out.println("Borrando fichero: " + file.getName());
//				file.delete();
//			}
//		}
//	}

	@Test
	void testCreateActividad() {
		String path = "actividades";
		
		cliente = Client.create();
		
		WebResource recurso = cliente.resource(URL_SERVICIO + path);
		
		MultivaluedMap<String, String> parametros = new MultivaluedMapImpl();
        parametros.add("titulo", "Actividad 1");
        parametros.add("descripcion", "Ejemplo");
        parametros.add("profesor", "Pepe");
        parametros.add("email", "pepe@gmail.com");
        
        ClientResponse respuesta = recurso.method("POST", ClientResponse.class, parametros);
		
        int codigoRespuesta = respuesta.getStatusInfo().getStatusCode();
        
        assertEquals(201, codigoRespuesta);

        String actividad1URL = respuesta.getLocation().toString();
        
        assertNotNull(actividad1URL);
        
        assertTrue(actividad1URL.startsWith("http://localhost:8080/api/actividades/"));
	}

	@Test
	void testGetActividad() {
		String path = "actividades";
		
		cliente = Client.create();
		
		WebResource recurso = cliente.resource(URL_SERVICIO + path);
		
		final String titulo = "Actividad 1";
		final String descripcion = "Ejemplo";
		final String profesor = "Pepe";
		final String email = "pepe@gmail.com";
		MultivaluedMap<String, String> parametros = new MultivaluedMapImpl();
        parametros.add("titulo", titulo);
        parametros.add("descripcion", descripcion);
        parametros.add("profesor", profesor);
        parametros.add("email", email);
        
        ClientResponse respuesta = recurso.method("POST", ClientResponse.class, parametros);

        String actividad1URL = respuesta.getLocation().toString();
        
        assertNotNull(actividad1URL);
        
        WebResource recursoActividad1 = cliente.resource(actividad1URL);
        
        ClientResponse respuesta2 = recursoActividad1
                .accept(MediaType.APPLICATION_XML)
                .method("GET", ClientResponse.class);
        
        assertEquals(200, respuesta2.getStatusInfo().getStatusCode());
        
        Actividad actividad = respuesta2.getEntity(Actividad.class);
        
        assertNotNull(actividad);
        assertEquals(actividad.getProfesor(), profesor);
        assertEquals(actividad.getEmail(), email);
        assertEquals(actividad.getTitulo(), titulo);
        assertEquals(actividad.getDescripcion(), descripcion);
	}

	@Test
	void testUpdateActividad() {
		String path = "actividades";
		
		cliente = Client.create();
		
		WebResource recurso = cliente.resource(URL_SERVICIO + path);
		
		final String titulo = "Actividad 1";
		final String descripcion = "Ejemplo";
		final String descripcion2 = "Cambio";
		final String profesor = "Pepe";
		final String email = "pepe@gmail.com";
		MultivaluedMap<String, String> parametros = new MultivaluedMapImpl();
        parametros.add("titulo", titulo);
        parametros.add("descripcion", descripcion);
        parametros.add("profesor", profesor);
        parametros.add("email", email);
        
        ClientResponse respuesta = recurso.method("POST", ClientResponse.class, parametros);

        String actividad1URL = respuesta.getLocation().toString();
        
        assertNotNull(actividad1URL);
        
        WebResource recursoActividad1 = cliente.resource(actividad1URL);
        
        parametros.putSingle("descripcion", descripcion2);

        ClientResponse respuesta2 = recursoActividad1
                .accept(MediaType.APPLICATION_XML)
                .method("PUT", ClientResponse.class, parametros);
        
        assertEquals(204, respuesta2.getStatusInfo().getStatusCode());
        
        ClientResponse respuesta3 = recursoActividad1
                .accept(MediaType.APPLICATION_XML)
                .method("GET", ClientResponse.class);
        
        assertEquals(200, respuesta3.getStatusInfo().getStatusCode());
        
        Actividad actividad = respuesta3.getEntity(Actividad.class);
        
        assertNotNull(actividad);
        assertEquals(actividad.getProfesor(), profesor);
        assertEquals(actividad.getEmail(), email);
        assertEquals(actividad.getTitulo(), titulo);
        assertNotEquals(actividad.getDescripcion(), descripcion);
        assertEquals(actividad.getDescripcion(), descripcion2);
	}

	@Test
	void testRemoveActividad() {
		String path = "actividades";
		
		cliente = Client.create();
		
		WebResource recurso = cliente.resource(URL_SERVICIO + path);
		
		final String titulo = "Actividad 1";
		final String descripcion = "Ejemplo";
		final String profesor = "Pepe";
		final String email = "pepe@gmail.com";
		MultivaluedMap<String, String> parametros = new MultivaluedMapImpl();
        parametros.add("titulo", titulo);
        parametros.add("descripcion", descripcion);
        parametros.add("profesor", profesor);
        parametros.add("email", email);
        
        ClientResponse respuesta = recurso.method("POST", ClientResponse.class, parametros);

        String actividad1URL = respuesta.getLocation().toString();
        
        assertNotNull(actividad1URL);
        
        WebResource recursoActividad1 = cliente.resource(actividad1URL);
        
        ClientResponse respuesta2 = recursoActividad1
                .accept(MediaType.APPLICATION_XML)
                .method("DELETE", ClientResponse.class);
        
        assertEquals(204, respuesta2.getStatusInfo().getStatusCode());
        
        ClientResponse respuesta3 = recursoActividad1
                .accept(MediaType.APPLICATION_XML)
                .method("GET", ClientResponse.class);
        
        assertEquals(404, respuesta3.getStatusInfo().getStatusCode());
	}

	//Falla pero no se por que
	@Test
	void testListado() {
		
		String path = "actividades";
		
		cliente = Client.create();
		
		WebResource recurso = cliente.resource(URL_SERVICIO + path);
		
		final String titulo = "Actividad 1";
		final String descripcion = "Ejemplo";
		final String profesor = "Pepe";
		final String email = "pepe@gmail.com";
		MultivaluedMap<String, String> parametros = new MultivaluedMapImpl();
        parametros.add("titulo", titulo);
        parametros.add("descripcion", descripcion);
        parametros.add("profesor", profesor);
        parametros.add("email", email);
        
        recurso.method("POST", ClientResponse.class, parametros);
        
        parametros.putSingle("titulo", "Actividad 2");
        
        recurso.method("POST", ClientResponse.class, parametros);

		String path2 = URL_SERVICIO + "actividades?profesor=Pepe&titulo=Actividad";
		
		cliente = Client.create();
		
		WebResource recurso2 = cliente.resource(URL_SERVICIO + path2);
		
		System.out.println(path2);
		
        ClientResponse respuesta = recurso2
                .accept(MediaType.APPLICATION_XML)
        		.method("GET", ClientResponse.class);
        
        assertNotNull(respuesta);
        assertEquals(200, respuesta.getStatusInfo().getStatusCode());

        ListadoActividades actividades = respuesta.getEntity(ListadoActividades.class);
        
        ItemActividad a1 = actividades.getActividad().get(0);
        ItemActividad a2 = actividades.getActividad().get(1);
        
        assertEquals(a1.getTitulo(), "Actividad 1");
        assertEquals(a2.getTitulo(), "Actividad 2");
	}

}
