package programa;

import cliente.conversor.ExcepcionConversion;
import cliente.conversor.IParseInteger;
import cliente.conversor.ParseIntegerService;

public class Programa3 {
	public static void main(String[] args) {
		String cadenaNumero = "256";
		
		ParseIntegerService servicio = new ParseIntegerService();
		IParseInteger puerto = servicio.getParseIntegerPort();
		
		Integer numero = null;
		try {
			numero = puerto.parseInteger(cadenaNumero);
		} catch (ExcepcionConversion e) {
			e.printStackTrace();
		}
		
		System.out.println("La cadena " + cadenaNumero + " es el numero " + numero);
	}
}
