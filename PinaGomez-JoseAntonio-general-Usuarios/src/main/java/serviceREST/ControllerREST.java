package serviceREST;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import schema.Usuario;

@Path("/usuarios")
@Api
public class ControllerREST {

	private Controller controlador = new Controller();

	@Context
	private UriInfo uriInfo;
	
	@POST
	@Path("/usuario")
	@ApiOperation(value = "Crea un nuevo usuario", notes = "Devuelve la url del nuevo usuario creado")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_CREATED, message = "201 Created"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request") })
	public Response createSondeo(@ApiParam(value = "Nombre del nuevo usuario", required = true) @FormParam("nombre") String nombre,
			@ApiParam(value = "Correo electronico del nuevo usuario", required = true) @FormParam("correp") String correo, 
			@ApiParam(value = "Rol del nuevo usuario", required = true) @FormParam("rol") String rol) throws ArgumentException, InternalException {
		
		String correo_id = controlador.createUsuario(nombre, correo, rol);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(correo_id);
		URI newURL = builder.build();

		return Response.created(newURL).build();
	}

	@GET
	@Path("/{correo}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consulta del usuario especificado por el correo", notes = "Devuelve los datos del usuario en formato JSON", response = Usuario.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "404 Not Found") })
	public Response getUsuario(@ApiParam(value = "Correo del usuario") @PathParam("correo") String correo) throws ArgumentException, NotFoundException, InternalException {
				
		Usuario usuario = controlador.getUsuario(correo);
		
		return Response.status(Response.Status.OK).entity(usuario).build();
	}
	
	@DELETE
	@Path("/{correo}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Elimina del usuario especificado por el correo", notes = "Elimina el usuario de base de datos")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "204 No Content"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "404 Not Found") })
	public Response deleteUsuario(@ApiParam(value = "Correo del usuario") @PathParam("correo") String correo) throws ArgumentException, NotFoundException, InternalException {
				
		controlador.deleteUsuario(correo);
		
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@GET
	@Path("/estudiantes")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consulta los usuarios con el rol de estudiante", notes = "Devuelve una lista con los datos de los estudiantes en formato JSON")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error")})
	public Response getEstudiantes() throws InternalException {
				
		List<Usuario> lista = controlador.getEstudiantes();
		
		return Response.status(Response.Status.OK).entity(lista).build();
	}
	
	@GET
	@Path("/profesores")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consulta los usuarios con el rol de profesor", notes = "Devuelve una lista con los datos de los profesores en formato JSON")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error")})
	public Response getProfesores() throws InternalException {
				
		List<Usuario> lista = controlador.getProfesores();
		
		return Response.status(Response.Status.OK).entity(lista).build();
	}
	
	@GET
	@Path("/todos")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consulta todos los usuarios", notes = "Devuelve una lista con los datos de los usuarios en formato JSON")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error")})
	public Response getUsuarios() throws InternalException {
				
		List<Usuario> lista = controlador.getAllUsuarios();
		
		return Response.status(Response.Status.OK).entity(lista).build();
	}
	
}
