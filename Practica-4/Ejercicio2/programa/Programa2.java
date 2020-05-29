package programa;

import com.cdyne.ws.IP2Geo;
import com.cdyne.ws.IP2GeoSoap;
import com.cdyne.ws.IPInformation;

public class Programa2 {

	public static void main(String[] args) {
		String ip = "193.110.128.199";
		String licenseKey = "0";
		
		IP2Geo servicio = new IP2Geo();
		IP2GeoSoap puerto = servicio.getIP2GeoSoap();
		
		IPInformation informacion = puerto.resolveIP(ip, licenseKey);
		
		System.out.println("La IP " + ip + " esta en el pais " + informacion.getCountry() + " y la ciudad " + informacion.getCity());
	}
}
