package bookle.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import bookle.exceptions.RecursoNoEncontradoException;

@Provider
public class TratamientoRecursoNoEncontradoException implements ExceptionMapper<RecursoNoEncontradoException> {
    
	@Override
    public Response toResponse(RecursoNoEncontradoException arg0) {
        
		return Response.status(Response.Status.NOT_FOUND).entity(arg0.getMessage()).build();
    }
}
