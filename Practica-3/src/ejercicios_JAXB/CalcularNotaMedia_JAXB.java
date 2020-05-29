package ejercicios_JAXB;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import acta.Acta;
import acta.TipoCalificacion;

public class CalcularNotaMedia_JAXB {

	public static void main(String[] args) throws Exception {

		JAXBContext contexto = JAXBContext.newInstance("acta");
		
		Unmarshaller unmarshaller = contexto.createUnmarshaller();
		Acta acta = (Acta) unmarshaller.unmarshal(new File("xml/acta.xml"));
		
		int numeroNotas = 0;
		int sumaNotas = 0;
		for (TipoCalificacion c : acta.getCalificacion()) {
			sumaNotas += c.getNota();
			numeroNotas++;
		}
		
		System.out.println("Numero de notas: " + numeroNotas);
		System.out.println("Suma de las notas: " + sumaNotas);
		System.out.println("Nota Media: " + sumaNotas / numeroNotas);
	}

}
