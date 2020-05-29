package ejercicios_JAXB;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import acta.Acta;
import acta.TipoDiligencia;

public class ContarDiligencias_JABXB {

	public static void main(String[] args) throws Exception {

		JAXBContext contexto = JAXBContext.newInstance("acta");
		
		Unmarshaller unmarshaller = contexto.createUnmarshaller();
		Acta acta = (Acta) unmarshaller.unmarshal(new File("xml/acta.xml"));
		
		Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.DAY_OF_MONTH, -30);
		Date fecha_limite = calendario.getTime();
		
		int numeroDiligencias = 0;
		for (TipoDiligencia d : acta.getDiligencia()) {
			Calendar fecha_calendario = d.getFecha().toGregorianCalendar();
			Date fecha = fecha_calendario.getTime();
			if (fecha.after(fecha_limite) && fecha.before(Calendar.getInstance().getTime())) {
				System.out.println("Nueva diligencia: " + fecha.toString());
				numeroDiligencias++;
			}
		}

		System.out.println("Hay " + numeroDiligencias + " en los últimos 30 dias");
	}

}
