package serviceREST;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import exceptions.ArgumentException;

@Provider
public class ArgumentExceptionWrapper implements ExceptionMapper<ArgumentException> {

	public Response toResponse(ArgumentException exception) {
		return Response.status(Response.Status.BAD_REQUEST).entity(exception.getFaultInfo()).build();
	}

}
