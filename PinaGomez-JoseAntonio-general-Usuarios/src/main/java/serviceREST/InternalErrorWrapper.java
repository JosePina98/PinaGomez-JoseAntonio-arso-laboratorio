package serviceREST;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import exceptions.InternalException;

@Provider
public class InternalErrorWrapper implements ExceptionMapper<InternalException> {

	public Response toResponse(InternalException exception) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
	}

}
