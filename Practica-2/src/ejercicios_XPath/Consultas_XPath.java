package ejercicios_XPath;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;

public class Consultas_XPath {

	public static void main(String[] args) throws Exception {
		final String documento = "xml_ejercicios_DOM/prueba.xml";
		
		// 1. Obtener la factoria
		
		XPathFactory factoria = XPathFactory.newInstance();
				 
		// 2. Construir el evaluador XPath
				
		XPath xpath = factoria.newXPath();

		xpath.setNamespaceContext( new EspacioNombres());
		
		// 3. Consultar todas las notas
				
		XPathExpression consulta1 = xpath.compile("//@nota"); 		
				
		NodeList resultado1 = (NodeList) consulta1.evaluate(
		  new org.xml.sax.InputSource(documento), XPathConstants.NODESET); 
		
		System.out.println("Consultar todas las notas: ");
		for (int i = 0; i < resultado1.getLength(); i++) {
			System.out.println("Nota " + i + ": " + resultado1.item(i).getTextContent());
		}
		
		System.out.println();
		
		// 4. Las notas de las calificaciones
		
		XPathExpression consulta2 = xpath.compile("//c:calificacion/@nota"); 		
		
		NodeList resultado2 = (NodeList) consulta2.evaluate(
		  new org.xml.sax.InputSource(documento), XPathConstants.NODESET); 
		
		System.out.println("Consultar las notas de las calificaciones: ");
		for (int i = 0; i < resultado2.getLength(); i++) {
			System.out.println("Nota Calificaciones " + i + ": " + resultado2.item(i).getTextContent());
		}
		
		System.out.println();
		
		// 5. Las fechas de las diligencias
		
		XPathExpression consulta3 = xpath.compile("//c:diligencia/@fecha"); 		
		
		NodeList resultado3 = (NodeList) consulta3.evaluate(
		  new org.xml.sax.InputSource(documento), XPathConstants.NODESET); 
		
		System.out.println("Consultar las fechas de las diligencias: ");
		for (int i = 0; i < resultado3.getLength(); i++) {
			System.out.println("Fecha Diligencia " + i + ": " + resultado3.item(i).getTextContent());
		}
		
		System.out.println();
		
		// 6. Diligencias que tengan el atributo extraordinaria REVISAR
		
		XPathExpression consulta4 = xpath.compile("//c:diligencia[@extraordinaria]"); 		
		
		NodeList resultado4 = (NodeList) consulta4.evaluate(
		  new org.xml.sax.InputSource(documento), XPathConstants.NODESET); 
		
		System.out.println("Consultar las diligencias que tengan el atributo extraordinaria: ");
		for (int i = 0; i < resultado4.getLength(); i++) {
			System.out.println("Diligencia " + i + ": " + resultado4.item(i).getTextContent());
		}
		
		System.out.println();

		// 7. Calificaciones con nota >= 9 REVISAR
		
		XPathExpression consulta5 = xpath.compile("//c:calificacion[nota >= 9]"); 		
		
		NodeList resultado5 = (NodeList) consulta5.evaluate(
		  new org.xml.sax.InputSource(documento), XPathConstants.NODESET); 
		
		System.out.println("Consultar las calificaciones con nota >= 9: ");
		for (int i = 0; i < resultado5.getLength(); i++) {
			System.out.println("Calificacion " + i + ": " + resultado5.item(i).getTextContent());
		}
		
		System.out.println();

		// 8. Calificaciones o diligencias para el NIF "13322156M" 
		
		
		System.out.println();

		// 9. Calificaciones cuyo NIF comience por "2"
		
		
		System.out.println();

		// 10. La segunda calificacion
		
		System.out.println("fin.");

	}

}
