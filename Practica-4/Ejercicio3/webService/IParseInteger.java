package webService;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface IParseInteger {
	
	@WebMethod
	int parseInteger(String cadena) throws ExcepcionConversion;
}
