package serviceREST;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import exceptions.NotFoundException;

public class NotFoundExceptionWrapper implements ExceptionMapper<NotFoundException> {

	public Response toResponse(NotFoundException exception) {
		return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
	}

}
