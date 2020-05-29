package controlador;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.io.File;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import tipos.Actividad;
import tipos.BookleException;
import tipos.TipoAgenda;
import tipos.TipoReserva;
import tipos.TipoTurno;
import utils.Utils;

public class Controlador implements BookleControlador {
	
	static int actividadId = 0;

	@Override
	public String createActividad(String titulo, String descripcion, String profesor, String email)
			throws BookleException {
		
		if (titulo == null || profesor == null) {
			throw new NullPointerException();
		}
		
		Actividad actividad = new Actividad();
		actividad.setId(String.valueOf(actividadId++));
		actividad.setTitulo(titulo);
		if (descripcion != null) {
			actividad.setDescripcion(descripcion);
		}
		actividad.setNombreProfesor(profesor);
		if (email != null) {
			actividad.setEmail(email);			
		}
		
		try {
			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Marshaller marshaller = contexto.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.marshal(actividad, new File("actividades/" + actividad.getId() + ".xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return actividad.getId();
	}

	@Override
	public void updateActividad(String id, String titulo, String descripcion, String profesor, String email)
			throws BookleException {
		
		if (titulo == null || profesor == null) {
			throw new NullPointerException();
		}
		
		Actividad actividad = new Actividad();
		actividad.setId(id);
		actividad.setTitulo(titulo);
		if (descripcion != null) {
			actividad.setDescripcion(descripcion);
		}
		actividad.setNombreProfesor(profesor);
		if (email != null) {
			actividad.setEmail(email);			
		}
		
		try {
			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Marshaller marshaller = contexto.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.marshal(actividad, new File("actividades/" + actividad.getId() + ".xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Actividad getActividad(String id) throws BookleException {
		String documento = "actividades/" + id + ".xml";
		try {
			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Unmarshaller unmarshaller = contexto.createUnmarshaller();
			return (Actividad) unmarshaller.unmarshal(new File(documento));
		} catch (Exception e) {
			throw new BookleException("Error al cargar la actividad", e);
		}
	}

	@Override
	public boolean removeActividad(String id) throws BookleException {
		String documento = "actividades/" + id + ".xml";
		File fichero = new File(documento);
		
		return fichero.delete();
	}

	@Override
	public void addDiaActividad(String id, Date fecha, int turnos) throws BookleException {
		Actividad actividad = getActividad(id);
		List<TipoAgenda> listaAgenda = actividad.getAgenda();
		
		TipoAgenda agenda = new TipoAgenda();
		agenda.setDia(Utils.createFecha(fecha));
		List<TipoTurno> listaTurnos = agenda.getTurno();
		for (int i = 0; i < turnos; i++) {
			TipoTurno turno = new TipoTurno();
			turno.setTurno(BigInteger.valueOf(i+1));
			listaTurnos.add(turno);
		}
		
		listaAgenda.add(agenda);
	}

	@Override
	public boolean removeDiaActividad(String id, Date fecha) throws BookleException {
		Actividad actividad = getActividad(id);
		List<TipoAgenda> listaAgenda = actividad.getAgenda();
		
		ListIterator<TipoAgenda> iter = listaAgenda.listIterator();
		while (iter.hasNext()) {
			if (iter.next().getDia().equals(Utils.createFecha(fecha))) {
				iter.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public int addTurnoActividad(String id, Date fecha) throws BookleException {
		Actividad actividad = getActividad(id);
		List<TipoAgenda> listaAgenda = actividad.getAgenda();
		List<TipoTurno> listaTurnos = null;
		for (TipoAgenda t : listaAgenda) {
			if (t.getDia().equals(Utils.createFecha(fecha))) {
				listaTurnos = t.getTurno();
			}
		}
		
		int valorDevuelto = -1;
		if (listaTurnos != null) {
			int turnosActuales = listaTurnos.size();
			TipoTurno nuevoTurno = new TipoTurno();
			nuevoTurno.setTurno(BigInteger.valueOf(turnosActuales + 1));
			valorDevuelto = turnosActuales + 1;
			listaTurnos.add(nuevoTurno);
		}
		
		return valorDevuelto;
	}

	@Override
	public boolean removeTurnoActividad(String id, Date fecha, int turno) throws BookleException {
		Actividad actividad = getActividad(id);
		List<TipoAgenda> listaAgenda = actividad.getAgenda();
		List<TipoTurno> listaTurnos = null;
		for (TipoAgenda t : listaAgenda) {
			if (t.getDia().equals(Utils.createFecha(fecha))) {
				listaTurnos = t.getTurno();
			}
		}
		
		if (listaTurnos != null && turno > 0 && turno < listaTurnos.size()) {
			listaTurnos.remove(turno);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setHorario(String idActividad, Date fecha, int indice, String horario) throws BookleException {
		Actividad actividad = getActividad(idActividad);
		List<TipoAgenda> listaAgenda = actividad.getAgenda();
		List<TipoTurno> listaTurnos = null;
		for (TipoAgenda t : listaAgenda) {
			if (t.getDia().equals(Utils.createFecha(fecha))) {
				listaTurnos = t.getTurno();
			}
		}
		
		if (listaTurnos != null && indice > 0 && indice < listaTurnos.size()) {
			TipoTurno turno = listaTurnos.get(indice);
			turno.setHorario(Utils.createFecha(Utils.hourFromString(horario)));
		}
	}

	@Override
	public String createReserva(String idActividad, Date fecha, int indice, String alumno, String email)
			throws BookleException {
		Actividad actividad = getActividad(idActividad);
		List<TipoAgenda> listaAgenda = actividad.getAgenda();
		List<TipoTurno> listaTurnos = null;
		for (TipoAgenda t : listaAgenda) {
			if (t.getDia().equals(Utils.createFecha(fecha))) {
				listaTurnos = t.getTurno();
			}
		}
		
		String generatedId = idActividad + "-" + alumno;
		
		if (listaTurnos != null && indice > 0 && indice < listaTurnos.size()) {
			TipoTurno turno = listaTurnos.get(indice);
			TipoReserva reserva = new TipoReserva();
			reserva.setNombreAlumno(alumno);
			reserva.setEmail(email);
			reserva.setId(generatedId);
			turno.setReserva(reserva);
			return reserva.getId();
		} else {
			return null;
		}
	}

	@Override
	public boolean removeReserva(String idActividad, String ticket) throws BookleException {
		Actividad actividad = getActividad(idActividad);
		List<TipoAgenda> listaAgenda = actividad.getAgenda();

		for (TipoAgenda t : listaAgenda) {
			for (TipoTurno t2 : t.getTurno()) {
				if (t2.getReserva().getId().equals(ticket)) {
					t2.setReserva(null);
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public LinkedList<Actividad> getActividades() throws BookleException {
		LinkedList<Actividad> lista = new LinkedList<Actividad>();
		
		File carpeta = new File("actividades/");
		for (File ficheroEntrada : carpeta.listFiles()) {
	        if (!ficheroEntrada.isDirectory()) {
	        	lista.add(getActividad(ficheroEntrada.getName().substring(0, ficheroEntrada.getName().length() - 4)));
	        }
	    }
		
		return lista;
	}

}
