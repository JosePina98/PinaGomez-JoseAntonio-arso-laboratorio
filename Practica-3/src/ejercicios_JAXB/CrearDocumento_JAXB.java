package ejercicios_JAXB;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import acta.Acta;
import acta.TipoCalificacion;
import acta.TipoDiligencia;

public class CrearDocumento_JAXB {

	public static void main(String[] args) throws Exception {

		JAXBContext contexto = JAXBContext.newInstance(Acta.class);
		
		Acta acta = new Acta();
		acta.setConvocatoria("febrero");
		
		XMLGregorianCalendar fecha1 = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		fecha1.setYear(2019);
		acta.setCurso(fecha1);
	
		acta.setCurso(null);
		
		acta.setAsignatura("1092");
		
		TipoCalificacion calificacion1 = new TipoCalificacion();
		calificacion1.setNif("23322156M");
		calificacion1.setNota(10.0);
		
		acta.getCalificacion().add(calificacion1);
		
		TipoCalificacion calificacion2 = new TipoCalificacion();
		calificacion2.setNif("13322156M");
		calificacion2.setNombre("Pepe");
		calificacion2.setNota(8.0);
		
		acta.getCalificacion().add(calificacion2);
		
		TipoDiligencia diligencia = new TipoDiligencia();
		diligencia.setNif("13322156M");
		diligencia.setNota(9.0);
		
		XMLGregorianCalendar fecha2 = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		fecha2.setYear(2020);
		fecha2.setMonth(2);
		fecha2.setDay(12);
		diligencia.setFecha(fecha2);
		
		acta.getDiligencia().add(diligencia);
		
		Marshaller marshaller = contexto.createMarshaller();
		
		marshaller.setProperty("jaxb.formatted.output", true);
		
		marshaller.marshal(acta, new File("xml/acta_creada.xml"));
		
		System.out.println("Fin.");
	}

}
