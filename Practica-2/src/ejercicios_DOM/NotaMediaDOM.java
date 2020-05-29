package ejercicios_DOM;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class NotaMediaDOM {
	
	public static void main(String[] args) throws Exception {
		final String documento = "xml_ejercicios_DOM/prueba.xml";
		
		// Construye un analizador DOM
		
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
		factoria.setNamespaceAware(true);
		DocumentBuilder builder = factoria.newDocumentBuilder();

		Document doc = builder.parse(documento);
		
		Element raiz = doc.getDocumentElement();
		
		NodeList elementos = raiz.getElementsByTagName("calificacion");
		
		Double notaTotal = 0.0;
		for (int i = 0; i < elementos.getLength(); i++) {
			Element elemento = (Element) elementos.item(i);
			
			Double nota = Double.parseDouble(elemento.getAttribute("nota"));
			System.out.println("Nota " + i + ": " + nota);
			notaTotal += nota;
		}
		
		System.out.println("\nNota media: " + notaTotal / elementos.getLength());
	}
}
