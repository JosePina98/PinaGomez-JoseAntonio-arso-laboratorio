package controlador;

import java.util.Date;
import java.util.LinkedList;

import tipos.BookleException;
import tipos.Actividad;

// Controlador Caso de uso

/*
 * Todos los m�todos declaran que pueden lanzar la excepci�n BookleException.
 */

public interface BookleControlador {

	/** 
	 * Metodo de creacion de una actividad.
	 * Los parametros email y descripcion son opcionales (aceptan valor nulo).
	 * El metodo retorna el id de la nueva actividad
	 */
	String createActividad(String titulo, String descripcion, String profesor, String email) throws BookleException;
	
	/**
	 * Metodo de actualizacion de la actividad.
	 * En relacion al metodo de creacion, añade un primer parametro con el id de la actividad.
	 */
	void updateActividad(String id, String titulo, String descripcion, String profesor, String email) throws BookleException;
	
	/**
	 * Recupera la informacion de una actividad utilizando el identificador. 	
	 */
	Actividad getActividad(String id)  throws BookleException;
	
	/**
	 * Elimina una actividad utilizando el identificador.
	 */
	boolean removeActividad(String id)  throws BookleException;
	
	/**
	 * Esta operacion añade un dia a una actividad.
	 * El dia de actividad esta identificado por la fecha.
	 * Al menos debe establecerse un turno.
	 */
	void addDiaActividad(String id, Date fecha, int turnos)  throws BookleException;
	
	/**
	 * Elimina un dia de una actividad.
	 * El id de la actividad y la fecha identifican el dia.
	 */
	boolean removeDiaActividad(String id, Date fecha)  throws BookleException;
	
	/**
	 * Añade un nuevo turno a un día de una actividad.
	 * Los parametros id y fecha identifican el dia.
	 * Retorna el numero de turno. El primer turno es el 1.
	 */
	int addTurnoActividad(String id, Date fecha) throws BookleException;
	
	/**
	 * Elimina un turno de un día de la actividad.
	 * El primer turno es el 1.
	 */
	boolean removeTurnoActividad(String id, Date fecha, int turno) throws BookleException;
	
	/**
	 * Establece la franja horaria de un turno de una actividad.
	 * El primer turno de un día de actividad es el 1.
	 */
	void setHorario(String idActividad, Date fecha, int indice, String horario) throws BookleException;
	
	/**
	 * Realiza la reserva de un turno de la actividad.
	 * Retorna el identificador de la reserva.
	 */
	String createReserva(String idActividad, Date fecha, int indice, String alumno, String email) throws BookleException;
	
	/**
	 * Elimina una reserva de una actividad. 
	 */
	boolean removeReserva(String idActividad, String ticket) throws BookleException;
	
	/**
	 * Metodo de consulta de todas las actividades.	
	 */
	LinkedList<Actividad> getActividades() throws BookleException;
}
