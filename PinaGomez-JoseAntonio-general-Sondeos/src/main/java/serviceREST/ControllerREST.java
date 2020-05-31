package serviceREST;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import controller.Controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import schema.Pregunta;
import schema.Sondeo;

@Path("/sondeo")
@Api
public class ControllerREST {

	private Controller controlador = new Controller();

	@Context
	private UriInfo uriInfo;
	
	@POST
	@Path("/prueba")
	@ApiOperation(value = "Crea un nuevo sondeo", notes = "Devuelve la url del nuevo sondeo creado")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_CREATED, message = "201 Created") })
	public Response createSondeo(@ApiParam(value = "Id del docente que crea el sondeo", required = true) @FormParam("docenteId") String docenteId,
			@ApiParam(value = "Instrucciones adicionales del sondeo", required = true) @FormParam("instruccionesAdicionales") String instruccionesAdicionales, 
			@ApiParam(value = "Fecha de apertura del sondeo", required = true) @FormParam("fechaApertura") Date fechaApertura, 
			@ApiParam(value = "Fecha de cierre del sondeo", required = true) @FormParam("fechaCierre") Date fechaCierre, 
			@ApiParam(value = "Texto de la pregunta del sondeo", required = true) @FormParam("textoPregunta") String textoPregunta, 
			@ApiParam(value = "Numero de respuestas minimas del sondeo", required = true) @FormParam("minimoRespuestas") int minimoRespuestas, 
			@ApiParam(value = "Numero de respuestas maximas del sondeo", required = true) @FormParam("maximoRespuestas") int maximoRespuestas) {
		
		//Controlar que todos los parametros tengan datos validos
		//Controlar que el id del docente exista
		//Controlar que que la fecha de apertura sea despues de la actual
		//Controlar que la fecha de cierre sea despues de la de apertura
		//Ver por que la hora de la fecha se escribe como 1 hora menos en bbdd
		//Controlar que el numero minimo de respuestas sea mayor que 0
		//Controlar que el numero maximo de respuestas sea igual o mayor que el minimo
		
		Pregunta pregunta = controlador.createPregunta(textoPregunta, minimoRespuestas, maximoRespuestas);
		
		String id = controlador.createSondeo(docenteId, instruccionesAdicionales, fechaApertura, fechaCierre, pregunta);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		URI newURL = builder.build();

		return Response.created(newURL).build();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consulta del sondeo especificado en el id", notes = "Devuelve los datos del sondeo en formato JSON", response = Sondeo.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK") })
	public Response getSondeo(@ApiParam(value = "Id del sondeo") @PathParam("id") String id) {
		
		//Comprobar que el id del sondeo tiene datos validos y existe
		
		Sondeo sondeo = controlador.getSondeo(id);
		
		return Response.status(Response.Status.OK).entity(sondeo).build();
	}
	
	@POST
	@Path("/{id}/addOpcion")
	@ApiOperation(value = "Anade una opcion a un sondeo", notes = "Devuelve la url del sondeo modificado")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "204 No Content") })
	public Response addOpcion(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id, 
			@ApiParam(value = "Opcion a anadir al sondeo", required = true) @FormParam("opcion") String opcion) {
		
		//Comprobar que el id del sondeo tiene datos validos y existe
		//Comprobar que la opcion a añadir tiene datos validos y no esta ya añadida
		//Comprobar que el que esta haciendo esta peticion es el docente que creó el sondeo
		
		controlador.addOpcionSondeo(id, opcion);
		
		String oldURL = uriInfo.getAbsolutePath().toString();
		String newURL = oldURL.substring(0, oldURL.lastIndexOf('/'));
		URI uri = URI.create(newURL);
		
		return Response.created(uri).build();
	}
	
	@POST
	@Path("/{id}/deleteOpcion")
	@ApiOperation(value = "Borra una opcion de un sondeo", notes = "Devuelve la url del sondeo modificado")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "204 No Content") })
	public Response deleteOpcion(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id, 
			@ApiParam(value = "Indice de la opcion a borrar del sondeo", required = true) @FormParam("index") int index) {
		
		//Comprobar que el id del sondeo tiene datos validos y existe
		//Comprobar que el indice a borrar esta en el sondeo
		//Comprobar que el que esta haciendo esta peticion es el docente que creó el sondeo
		
		controlador.deleteOpcionSondeo(id, index);
		
		String oldURL = uriInfo.getAbsolutePath().toString();
		String newURL = oldURL.substring(0, oldURL.lastIndexOf('/'));
		URI uri = URI.create(newURL);
		
		return Response.created(uri).build();
	}
	
	@GET
	@Path("/{id}/verResultados")
	@ApiOperation(value = "Consulta los resultados de un sondeo", notes = "Devuelve los datos de los votos del sondeo")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK") })
	public Response verResultados(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id) {
		
		//Comprobar que el id del sondeo tiene datos validos y existe
		
		HashMap<String, Integer> mapa = controlador.verResultados(id);
		
		return Response.status(Response.Status.OK).entity(mapa).build();
	}
	
	
	@POST
	@Path("/{id}/responderSondeo")
	@ApiOperation(value = "Responde a un sondeo", notes = "Devuelve la url del sondeo")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK") })
	public Response responderSondeo(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id, 
			@ApiParam(value = "Respuestas del sondeo", required = true) @QueryParam("respuestas") List<String> respuestas) {
		
		//Comprobar que el id del sondeo tiene datos validos y existe
		//Comprobar que cumple las respuestas minimas y maximas
		//Comprobar que no se responde dos o mas veces la misma opcion
		//Comprobar que el que esta respondiendo es un alumno
		//Comprobar que todas las opciones respondidas estan en el sondeo
		
		List<Integer> lista = new LinkedList<Integer>();

		String string = respuestas.get(0);
		String[] array = string.split(",");
		for (String i : array) {
			int aux = Integer.valueOf(i);
			lista.add(aux);
		}
		
		controlador.votar(id, lista);		
	    
		String oldURL = uriInfo.getAbsolutePath().toString();
		String newURL = oldURL.substring(0, oldURL.lastIndexOf('/'));
		URI uri = URI.create(newURL);
		
		return Response.created(uri).build();
	}
	
}
