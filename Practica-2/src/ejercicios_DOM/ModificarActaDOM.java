package ejercicios_DOM;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ModificarActaDOM {

	public static void main(String[] args) throws Exception {
		final String documento = "xml_ejercicios_DOM/prueba.xml";
		final String fichero_salida = "xml_ejercicios_DOM/pruebaModificada.xml";

		// Construye un analizador DOM
		
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
		factoria.setNamespaceAware(true);
		DocumentBuilder builder = factoria.newDocumentBuilder();

		Document doc = builder.parse(documento);
		
		Element raiz = doc.getDocumentElement();
		
		NodeList elementos = raiz.getElementsByTagName("calificacion");
		
		for (int i = 0; i < elementos.getLength(); i++) {
			Element elemento = (Element) elementos.item(i);
			
			Double nota = Double.parseDouble(elemento.getAttribute("nota"));
			System.out.println("Nota " + i + ": " + nota);
			
			if (nota <= 9.5) {
				Double notaModificada = nota + 0.5;
				elemento.setAttribute("nota", notaModificada.toString());
				System.out.println("Nota modificada " + i + ": " + notaModificada);
			}
		}
		
		TransformerFactory tFactoria = TransformerFactory.newInstance();
		Transformer transformacion = tFactoria.newTransformer();
		
		// 2. Establece la entrada, como un árbol DOM
		Source input = new DOMSource(doc);
		
		// 3. Establece la salida, un fichero en disco
		Result output =	new StreamResult(fichero_salida);
		
		// 4. Aplica la transformación
		transformacion.transform(input, output);
		
	}

}
