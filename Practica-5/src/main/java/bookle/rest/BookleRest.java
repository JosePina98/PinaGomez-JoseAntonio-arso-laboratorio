package bookle.rest;

import java.net.URI;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import bookle.controlador.BookleControlador;
import bookle.controlador.BookleControladorImpl;
import bookle.controlador.BookleException;
import bookle.exceptions.RecursoNoEncontradoException;
import bookle.tipos.Actividad;
import bookle.tipos.ItemActividad;
import bookle.tipos.ListadoActividades;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@Path("actividades")
@Api
public class BookleRest {
	
	private BookleControlador controlador = new BookleControladorImpl();
	
	@Context
	private UriInfo uriInfo;
	
	@POST
	@ApiOperation(value = "Crea una actividad",
			    notes = "Retorna una la URL de la nueva actividad creada")
	@ApiResponses(value = { 
				@ApiResponse(code = HttpServletResponse.SC_CREATED, message =""),
				@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Peticion mal formada")})  
	public Response createActividad (
			@ApiParam(value = "titulo de la actividad", required = true) @FormParam("titulo") String titulo, 
			@ApiParam(value = "descripcion de la actividad", required = false) @FormParam("descripcion") String descripcion,
			@ApiParam(value = "profesor de la actividad", required = true) @FormParam("profesor") String profesor, 
			@ApiParam(value = "email de la actividad", required = false) @FormParam("email") String email) throws BookleException, IllegalArgumentException {
		
		String id = controlador.createActividad(titulo, descripcion, profesor, email);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		URI nuevaURL = builder.build();
		
		return Response.created(nuevaURL).build();
	}

	@GET
    @Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta una actividad",
	        notes = "Retorna una actividad utilizando su identificador",
	        response = Actividad.class)
	@ApiResponses(value = { 
				@ApiResponse(code = HttpServletResponse.SC_OK, message =""),
				@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Peticion mal formada"),
	            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Actividad no encontrada")})  
	public Response getActividad(@ApiParam(value = "id de la actividad", required = true) @PathParam("id") String id) throws BookleException, IllegalArgumentException, RecursoNoEncontradoException {

       Actividad actividad = controlador.getActividad(id);

       return Response.status(Response.Status.OK).entity(actividad).build();
    }
	
	@PUT
    @Path("{id}")
	@ApiOperation(value = "Actualiza una actividad", notes = "No retorna nada")
	@ApiResponses(value = { 
				@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message ="La actividad se ha actualizado"),
				@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Peticion mal formada"),
				@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Actividad no encontrada")})  
    public Response updateActividad (@ApiParam(value = "id de la actividad", required = true) @PathParam("id") String id, 
			@ApiParam(value = "titulo de la actividad", required = true) @FormParam("titulo") String titulo, 
			@ApiParam(value = "descripcion de la actividad", required = false) @FormParam("descripcion") String descripcion,
			@ApiParam(value = "profesor de la actividad", required = true) @FormParam("profesor") String profesor, 
			@ApiParam(value = "email de la actividad", required = false) @FormParam("email") String email) throws BookleException, IllegalArgumentException {

        controlador.updateActividad(id, titulo, descripcion, profesor, email);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
	
	@DELETE
	@Path("{id}")
	@ApiOperation(value = "Borra una actividad", notes = "No retorna nada")
	@ApiResponses(value = { 
				@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message ="La actividad se ha borrado"),
				@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Peticion mal formada")})  
	public Response removeActividad(@PathParam("id") String id) throws BookleException, IllegalArgumentException {
	
	    controlador.removeActividad(id);
	
	    return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Retorna un listado de actividades", response = ListadoActividades.class)
	public Response listado(
			@ApiParam(value = "profesor de la actividad", required = true) @QueryParam("profesor") String profesor,
			@ApiParam(value = "titulo de la actividad", required = true) @QueryParam("titulo") String titulo) throws BookleException {

	    Collection<Actividad> actividades = controlador.getActividades();

	    ListadoActividades listado = new ListadoActividades();

	    for (Actividad aux : actividades) {

	    	if (aux.getProfesor().equals(profesor)
	    			&& aux.getTitulo().contains(titulo)) {
		        
	    		// Cálculo de la URL
	
		        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		        builder.path(aux.getId()); 
	
		        String url = builder.build().toString();
		        String nombre = aux.getTitulo();
		        
		        listado.getActividad().add(new ItemActividad(nombre, url));
	    
	    	}
	    }

	    return Response.ok(listado).build(); 
	}
	
}
