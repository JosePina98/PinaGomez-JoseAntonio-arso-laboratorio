package programa;

import java.math.BigDecimal;
import java.math.BigInteger;
import com.dataaccess.webservicesserver.NumberConversion;
import com.dataaccess.webservicesserver.NumberConversionSoapType;

public class Programa {

	public static void main(String[] args) {
		final String numero = "123";
		final String moneda = "32.2";
	
		NumberConversion servicio = new NumberConversion();
		NumberConversionSoapType puerto = servicio.getNumberConversionSoap();
		
		String respuesta1 = puerto.numberToWords(new BigInteger(numero));
		String respuesta2 = puerto.numberToDollars(new BigDecimal(moneda));
		
		System.out.printf("%s a texto: %s \n", numero, respuesta1);
		System.out.printf("%s a moneda: %s \n", moneda, respuesta2);
	}
}