package serviceREST;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.datatype.XMLGregorianCalendar;

import controller.Controller;
import exceptions.ArgumentException;
import exceptions.InternalException;
import exceptions.NotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/sondeos")
@Api
public class ControllerREST {

	private Controller controlador = new Controller();

	@Context
	private UriInfo uriInfo;

	/*
	 
	@GET
	@Path("/authors")
	@Produces({ MediaType.APPLICATION_ATOM_XML, "application/hal+json" })
	@ApiOperation(value = "Consulta de los autores que tengan el nombre name", notes = "Devuelve una colección en formato Atom", response = Feed.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request") })
	public Response searchAuthors(@Context HttpHeaders httpHeaders,
			@ApiParam(value = "parámetro de búsqueda", required = true) @QueryParam("name") String name)
			throws InternalException, ArgumentException {

		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		URI url = builder.build();
		
		List<ResponseAuthors> authorsList = controlador.searchAuthors(name);

		String accept = httpHeaders.getRequestHeader("Accept").get(0);

		// Dependiendo del tipo solicitado creamos la respuesta correspondiente
		if (accept.contains("json")) {
			// Creamos las respuesta JSON (HAL)
			JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
					.add("_links", 
							Json.createObjectBuilder()
							.add("self", 
									Json.createObjectBuilder()
									.add("href", url.toString())
									.build()
							).build()
					)
					.add("total", authorsList.size());
		
			
			JsonArrayBuilder authorsArray = Json.createArrayBuilder();
			
			for (ResponseAuthors a : authorsList) {
				authorsArray.add(Json.createObjectBuilder()
									.add("_links", 
										Json.createObjectBuilder()
										.add("self", 
												Json.createObjectBuilder()
												.add("href", a.getUrl().getSrc())
												.build()
										).build()
									)
									.add("authorName", a.getName())
									.add("url", a.getUrl().getSrc())
									.build());
			}
			
			JsonObject _embedded = Json.createObjectBuilder().add("authors", authorsArray.build()).build();
			
			JsonObject json = jsonBuilder.add("_embedded", _embedded).build();

			return Response.status(Response.Status.OK).entity(json.toString()).build();
		} else {
			
			// Creamos la respuesta XML (ATOM)
			Feed atomList = new Feed();
			atomList.setTitle("Lista de autores");

			TypeLinkCollection link = new TypeLinkCollection();
			link.setHref(url.toString());
			atomList.setLink(link);

			XMLGregorianCalendar valueDateTime = DateUtils.createFechaTime(new Date());
			atomList.setUpdated(valueDateTime);

			TypeAuthor author = new TypeAuthor();
			author.setName("Briam y Jose");
			atomList.setAuthor(author);

			atomList.setId(url.toString());

			for (ResponseAuthors aux : authorsList) {
				TypeEntry entry = new TypeEntry();

				entry.setTitle(aux.getName());

				TypeLinkCollection linkAuthor = new TypeLinkCollection();
				linkAuthor.setHref(aux.getUrl().getSrc());
				entry.setLink(linkAuthor);

				entry.setId(linkAuthor.getHref());

				entry.setUpdated(valueDateTime);

				entry.setSummary("Un resultado de la búsqueda");

				atomList.getEntry().add(entry);
			}

			return Response.status(Response.Status.OK).entity(atomList).build();
		}
	}

	@GET
	@Path("/authors/{url}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta del autor especificado en el id", notes = "Devuelve los datos de un Autor en formato Atom", response = Author.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request") })
	public Response getInfoAuthor(@PathParam("url") String URL) throws InternalException, ArgumentException {

		Author author = controlador.getInfoAuthor(URL);

		return Response.status(Response.Status.OK).entity(author).build();
	}

	@POST
	@Path("/favDocuments")
	@ApiOperation(value = "Creacion de un nuevo documento de autores favoritos", notes = "Devuelve la URL completa del nuevo documento de favoritos")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_CREATED, message = "201 Created"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request") })
	public Response createFavoritesDocument() throws InternalException {

		String id = controlador.createFavoritesDocument();

		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		URI newURL = builder.build();

		return Response.created(newURL).build();
	}

	@POST
	@Path("/favDocuments/{id}/author")
	@ApiOperation(value = "Añade el nuevo autor identificado por el parametro URL al documento de favoritos identificado por el parametro ID", notes = "")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "204 No Content"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "404 Not Found") })
	public Response addFavoriteAuthor(@FormParam("URL") String URL, @PathParam("id") String idDocument)
			throws InternalException, ArgumentException, NotFoundException {

		controlador.addFavoriteAuthor(URL, idDocument);

		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		String aux[] = URL.split("/");
		builder.path(aux[aux.length - 1]);
		URI newURL = builder.build();

		return Response.created(newURL).build();
	}

	@DELETE
	@Path("/favDocuments/{id}/author")
	@ApiOperation(value = "Elimina el autor identificado por el parametro URL del documento de favoritos identificado por el parametro ID", notes = "")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "204 No Content"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "404 Not Found") })
	public Response removeFavoriteAuthor(@FormParam("URL") String URL, @PathParam("id") String idDocument)
			throws InternalException, ArgumentException, NotFoundException {

		controlador.removeFavoriteAuthor(URL, idDocument);

		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@GET
	@Path("/favDocuments/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta del documento de favoritos especificado en el id", notes = "Devuelve los datos de un documento de favoritos en formato Atom", response = FavoritesDocument.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internar Server Error"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "404 Not Found") })
	public Response getFavoritesDocument(@PathParam("id") String idDocument)
			throws InternalException, ArgumentException, NotFoundException {

		FavoritesDocument favDoc = controlador.getFavoritesDocument(idDocument);

		return Response.status(Response.Status.OK).entity(favDoc).build();
	}

	@GET
	@Path("/favDocuments")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta de los ids de todos los documentos de favoritos", notes = "Devuelve los ids de todos los documentos de favoritos en formato Atom")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "200 OK") })
	public Response getAllFavoritesDocument() {

		List<String> favDocs = controlador.getAllFavoritesDocuments();

		Feed atomList = new Feed();
		atomList.setTitle("Lista de id de documentos de autores favoritos");

		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		URI url = builder.build();

		TypeLinkCollection link = new TypeLinkCollection();
		link.setHref(url.toString());
		atomList.setLink(link);

		XMLGregorianCalendar valueDateTime = DateUtils.createFechaTime(new Date());
		atomList.setUpdated(valueDateTime);

		TypeAuthor author = new TypeAuthor();
		author.setName("Briam y Jose");
		atomList.setAuthor(author);

		atomList.setId(url.toString());

		for (String aux : favDocs) {
			TypeEntry entry = new TypeEntry();

			entry.setTitle(aux);

			TypeLinkCollection linkAuthor = new TypeLinkCollection();
			linkAuthor.setHref(url + "/" + aux);
			entry.setLink(linkAuthor);

			entry.setId(linkAuthor.getHref());

			entry.setUpdated(valueDateTime);

			entry.setSummary("Un resultado de la búsqueda");

			atomList.getEntry().add(entry);
		}

		return Response.status(Response.Status.OK).entity(atomList).build();
	}

	@DELETE
	@Path("/favDocuments/{id}")
	@ApiOperation(value = "Elimina el documento de favoritos identificado con el parametro id", notes = "")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "204 No Content"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "400 Bad Request"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "500 Internal Server Error") })
	public Response removeFavoritesDocument(@PathParam("id") String idDocument)
			throws ArgumentException, InternalException {

		controlador.removeFavoritesDocument(idDocument);

		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	*/
}
