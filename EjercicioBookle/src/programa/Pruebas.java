package programa;

import java.util.Date;
import java.util.List;

import controlador.Controlador;
import tipos.Actividad;
import tipos.BookleException;

public class Pruebas {

	public static void main(String[] args) throws BookleException {
		
		Controlador controlador = new Controlador();
		
		String idActividad = controlador.createActividad("actividad1", "primera prueba creando una actividad", "profesor1", null);
		
		String idActividad2 = controlador.createActividad("actividad2", "sera borrada",	"profesor2", null);
		
		controlador.updateActividad(idActividad, "actividad1", "primera pprueba creando una actividad", "profesor1", "email@um.es");
		
		Actividad actividad = controlador.getActividad(idActividad);
		
		controlador.removeActividad(idActividad2);
		
		Date fecha = new Date();
		controlador.addDiaActividad(idActividad, fecha, 3);
		
		controlador.removeDiaActividad(idActividad, fecha);
		
		controlador.addTurnoActividad(idActividad, fecha);
		
		controlador.removeTurnoActividad(idActividad, fecha, 2);
		
		controlador.setHorario(idActividad, fecha, 1, "10:10:10");
		
		String idReserva1 = controlador.createReserva(idActividad, fecha, 1, "jose", "jose@um.es");
		
		String idReserva2 = controlador.createReserva(idActividad, fecha, 3, "pina", "pina@um.es");
		
		controlador.removeReserva(idActividad, idReserva2);
		
		List<Actividad> lista = controlador.getActividades();
		
		for (Actividad a : lista) {
			System.out.println(a.getTitulo());
		}
	}
}
