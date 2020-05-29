package webService;

import javax.jws.WebService;

@WebService(endpointInterface = "webService.IParseInteger")
public class ParseInteger implements IParseInteger {

	@Override
	public int parseInteger(String cadena) throws ExcepcionConversion {
		
		int numero = Integer.parseInt(cadena);
		return numero;
	}

}
