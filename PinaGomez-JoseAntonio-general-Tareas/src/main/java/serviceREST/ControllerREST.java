package serviceREST;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import controller.Controller;
import io.swagger.annotations.Api;

@Path("/tareas")
@Api
public class ControllerREST {

	private Controller controlador = new Controller();

	@Context
	private UriInfo uriInfo;

}
