package serviceREST;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
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
		
		Sondeo sondeo = controlador.getSondeo(id);
		
		return Response.status(Response.Status.OK).entity(sondeo).build();
	}
	
	@POST
	@Path("/{id}/addOpcion")
	@ApiOperation(value = "Anade una opcion a un sondeo", notes = "Devuelve la url del sondeo modificado")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "204 No Content") })
	public Response addOpcion(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id, 
			@ApiParam(value = "Opcion a anadir al sondeo", required = true) @FormParam("opcion") String opcion) {
		
		controlador.addOpcionSondeo(id, opcion);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		String aux[] = builder.toString().split("/");
		builder.path(aux[aux.length - 1]);
		URI newURL = builder.build();

		return Response.created(newURL).build();
	}
	
	@POST
	@Path("/{id}/deleteOpcion")
	@ApiOperation(value = "Borra una opcion de un sondeo", notes = "Devuelve la url del sondeo modificado")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "204 No Content") })
	public Response deleteOpcion(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id, 
			@ApiParam(value = "Indice de la opcion a borrar del sondeo", required = true) @FormParam("index") int index) {
		
		controlador.deleteOpcionSondeo(id, index);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		String aux[] = builder.toString().split("/");
		builder.path(aux[aux.length - 1]);
		URI newURL = builder.build();

		return Response.created(newURL).build();
	}
	
	@GET
	@Path("/{id}/verResultados")
	@ApiOperation(value = "Consulta los resultados de un sondeo", notes = "Devuelve los datos de los votos del sondeo")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK") })
	public Response verResultados(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id) {
		
		List<Integer> resultados = controlador.verResultados(id);
		
		return Response.status(Response.Status.OK).entity(resultados).build();

	}
	
	/*
	@POST
	@Path("/{id}/responderSondeo")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value = "Responde a un sondeo", notes = "Devuelve la url del sondeo")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK") })
	public Response responderSondeo(@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id, 
			@ApiParam(value = "Respuestas del sondeo", required = true) @FormParam("respuestas") MultivaluedMap respuestas) {
		
		return null;
	}
	*/
}
