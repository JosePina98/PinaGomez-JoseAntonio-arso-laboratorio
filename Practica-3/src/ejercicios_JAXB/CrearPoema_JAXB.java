package ejercicios_JAXB;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import poema.Poema;

public class CrearPoema_JAXB {

	public static void main(String[] args) throws Exception {
		
		JAXBContext contexto = JAXBContext.newInstance(Poema.class);
		
		Poema poema = new Poema();
		
		poema.setFecha("Abril de 1915");
		poema.setLugar("Granada");
		poema.setTitulo("Alba");
		poema.getVerso().add("Mi corazón oprimido");
		poema.getVerso().add("siente junto a la alborada");
		poema.getVerso().add("el dolor de sus amores");
		poema.getVerso().add("y el sueño de las distancias");
		
		Marshaller marshaller = contexto.createMarshaller();
		
		marshaller.setProperty("jaxb.formatted.output", true);
		
		marshaller.setProperty("jaxb.schemaLocation", "http://www.w3.org/2001/XMLSchema poema.xsd");
		marshaller.marshal(poema, new File("xml/poema_creado.xml"));
		
		System.out.println("Fin.");
	}
}
