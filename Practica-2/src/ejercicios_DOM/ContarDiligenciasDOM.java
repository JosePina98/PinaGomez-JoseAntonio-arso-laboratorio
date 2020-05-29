package ejercicios_DOM;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ContarDiligenciasDOM {

	public static void main(String[] args) throws Exception {
		final String documento = "xml_ejercicios_DOM/prueba.xml";
		
		// Construye un analizador DOM
		
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
		factoria.setNamespaceAware(true);
		DocumentBuilder builder = factoria.newDocumentBuilder();

		Document doc = builder.parse(documento);
		
		Element raiz = doc.getDocumentElement();
		
		NodeList elementos = raiz.getElementsByTagName("diligencia");
		
		Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.DAY_OF_MONTH, -30);
		Date fecha_limite = calendario.getTime();
		
		System.out.println("Compararemos con: " + fecha_limite);
		
		int numero_diligencias = 0;
		for (int i = 0; i < elementos.getLength(); i++) {
			Element elemento = (Element) elementos.item(i);
			
			String s_fecha = elemento.getAttribute("fecha");
			Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(s_fecha); 
			
			System.out.println("Fecha " + i + ": " + fecha);
			
			if (fecha.after(fecha_limite) && fecha.before(Calendar.getInstance().getTime())) {
				numero_diligencias++;
			}
		}
		
		System.out.println("\nEl numero de diligencias en los ultimos 30 días son: " + numero_diligencias);
	}

}
