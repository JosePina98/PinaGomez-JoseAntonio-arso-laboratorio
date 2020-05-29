package ejercicios_JAXB;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import acta.Acta;
import acta.TipoCalificacion;

public class ModificarActa_JAXB {

	public static void main(String[] args) throws Exception {

		JAXBContext contexto = JAXBContext.newInstance("acta");
		
		Unmarshaller unmarshaller = contexto.createUnmarshaller();
		Acta acta = (Acta) unmarshaller.unmarshal(new File("xml/acta.xml"));
		
		for (TipoCalificacion c : acta.getCalificacion()) {
			Double nota = c.getNota();
			if (nota <= 9.5) {				
				c.setNota(nota + 0.5);
			}
		}
		
		Marshaller marshaller = contexto.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", true);
		marshaller.setProperty("jaxb.schemaLocation", "http://www.um.es/acta acta.xsd");
		marshaller.marshal(acta, new File("xml/acta_modificado.xml"));
	
		System.out.println("Fin.");
	}

}
