package serviceREST;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
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
import exceptions.ArgumentException;
import exceptions.InternalException;
import exceptions.NotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import schema.Pregunta;
import schema.Sondeo;

@Path("/sondeos")
@Api
public class ControllerREST {

	private Controller controlador = new Controller();

	@Context
	private UriInfo uriInfo;
	
	@POST
	@Path("/sondeo")
	@ApiOperation(value = "Crea un nuevo sondeo", notes = "Devuelve la url del nuevo sondeo creado")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_CREATED, message = "201 Created"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request") })
	public Response createSondeo(@ApiParam(value = "Correo del docente que crea el sondeo", required = true) @FormParam("docenteId") String docenteId,
			@ApiParam(value = "Instrucciones adicionales del sondeo", required = true) @FormParam("instruccionesAdicionales") String instruccionesAdicionales, 
			@ApiParam(value = "Fecha de apertura. Formato MM/dd/YYYY hh:mm", required = true) @FormParam("fechaApertura") Date fechaApertura, 
			@ApiParam(value = "Fecha de cierre. Formato MM/dd/YYYY hh:mm", required = true) @FormParam("fechaCierre") Date fechaCierre, 
			@ApiParam(value = "Texto de la pregunta del sondeo", required = true) @FormParam("textoPregunta") String textoPregunta, 
			@ApiParam(value = "Numero de respuestas minimas del sondeo", required = true) @FormParam("minimoRespuestas") int minimoRespuestas, 
			@ApiParam(value = "Numero de respuestas maximas del sondeo", required = true) @FormParam("maximoRespuestas") int maximoRespuestas) throws ArgumentException, InternalException {
		
		Pregunta pregunta = controlador.createPregunta(textoPregunta, minimoRespuestas, maximoRespuestas);
		
		String id = controlador.createSondeo(docenteId, instruccionesAdicionales, fechaApertura, fechaCierre, pregunta);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		URI newURL = builder.build();

		return Response.created(newURL).build();
	}

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consulta de todos los sondeos", notes = "Devuelve los datos de todos los sondeos en formato JSON")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "404 Not Found") })
	public Response getSondeos() throws ArgumentException, NotFoundException, InternalException {
				
		List<Sondeo> listaSondeos = controlador.getSondeos();
		
		return Response.status(Response.Status.OK).entity(listaSondeos).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consulta del sondeo especificado en el id", notes = "Devuelve los datos del sondeo en formato JSON", response = Sondeo.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "404 Not Found") })
	public Response getSondeo(@ApiParam(value = "Id del sondeo") @PathParam("id") String id) throws ArgumentException, NotFoundException, InternalException {
				
		Sondeo sondeo = controlador.getSondeo(id);
		
		return Response.status(Response.Status.OK).entity(sondeo).build();
	}
	
	@POST
	@Path("/{id}/addOpcion")
	@ApiOperation(value = "Anade una opcion a un sondeo", notes = "Devuelve la url del sondeo modificado")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "204 No Content"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "404 Not Found") })
	public Response addOpcion(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id, 
			@ApiParam(value = "Id del docente que  que hace la peticion", required = true) @FormParam("docenteId") String docenteId,
			@ApiParam(value = "Opcion a anadir al sondeo", required = true) @FormParam("opcion") String opcion) throws ArgumentException, NotFoundException, InternalException {
		
		controlador.addOpcionSondeo(id, docenteId, opcion);
		
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@POST
	@Path("/{id}/deleteOpcion")
	@ApiOperation(value = "Borra una opcion de un sondeo", notes = "Devuelve la url del sondeo modificado")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "204 No Content"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "404 Not Found") })
	public Response deleteOpcion(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id, 
			@ApiParam(value = "Id del docente que hace la peticion", required = true) @FormParam("docenteId") String docenteId,
			@ApiParam(value = "Indice de la opcion a borrar del sondeo (Empieza en 1)", required = true) @FormParam("index") int index) throws ArgumentException, NotFoundException, InternalException {
		
		controlador.deleteOpcionSondeo(id, docenteId, index - 1);
		
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@POST
	@Path("/{id}/responderSondeo")
	@ApiOperation(value = "Responde a un sondeo", notes = "Devuelve la url del sondeo")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "204 No Content"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "404 Not Found") })
	public Response responderSondeo(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id, 
			@ApiParam(value = "Id del alumno que responde el sondeo", required = true) @FormParam("alumnoId") String alumnoId,
			@ApiParam(value = "Indices de las opciones marcadas como respuestas (Empiezan en 1)", required = true) @QueryParam("respuestas") List<String> respuestas) throws ArgumentException, NotFoundException, InternalException {
		
		List<Integer> lista = new LinkedList<Integer>();

		String string = respuestas.get(0);

		if (string == null || string.equals("")) {
			throw new ArgumentException("{ \"error\" : \"Las respuestas son no pueden estar vacías\" }");
		}

		String[] array = string.split(",");
		for (String i : array) {
			if (i == null || i.equals("")) {
				throw new ArgumentException("{ \"error\" : \"Las respuestas no pueden estar vacías\" }");
			}
			int aux = Integer.valueOf(i);
			lista.add(aux - 1);
		}
		
		controlador.votar(id, alumnoId, lista);		
	    
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@GET
	@Path("/{id}/verResultados")
	@ApiOperation(value = "Consulta los resultados de un sondeo", notes = "Devuelve los datos de los votos del sondeo")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "404 Not Found") })
	public Response verResultados(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id) throws ArgumentException, NotFoundException, InternalException {
				
		HashMap<String, Integer> mapa = controlador.verResultados(id);
		
		return Response.status(Response.Status.OK).entity(mapa).build();
	}
	
	
}
