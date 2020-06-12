package controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.rabbitmq.client.Sender;

import dao.DAOController;
import exceptions.ArgumentException;
import exceptions.InternalException;
import exceptions.NotFoundException;
import repositories.UsuariosRepository;
import schema.Pregunta;
import schema.Sondeo;

public class Controller {
	
	private DAOController controladorDAO = DAOController.getUnicaInstancia();

	public Pregunta createPregunta(String textoPregunta, int minimoRespuestas, int maximoRespuestas) throws ArgumentException {
		
		//Controlar que todos los parametros tengan datos validos		
		if (textoPregunta == null || textoPregunta.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro textoPregunta no puede ser vacio o null\" }");
		}
		
		//Controlar que el numero minimo de respuestas sea mayor que 0
		if (minimoRespuestas <= 0) {
			throw new ArgumentException("{ \"error\" : \"El parametro minimoRespuestas no puede ser menor o igual que 0\" }");
		}
		
		//Controlar que el numero maximo de respuestas sea igual o mayor que el minimo
		if (maximoRespuestas < minimoRespuestas) {
			throw new ArgumentException("{ \"error\" : \"El parametro maximoRespuestas no puede ser menor que minimoRespuestas\" }");
		}
		
		return new Pregunta(textoPregunta, minimoRespuestas, maximoRespuestas);
	}

	public String createSondeo(String docenteId, String instruccionesAdicionales, Date fechaApertura, Date fechaCierre,
			Pregunta pregunta) throws ArgumentException, InternalException {
		
		//Controlar que todos los parametros tengan datos validos
		if (docenteId == null || docenteId.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro docenteId no puede ser vacio o null\" }");
		}
		
		if (instruccionesAdicionales == null || instruccionesAdicionales.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro instruccionesAdicionales no puede ser vacio o null\" }");
		}
		
		if (fechaApertura == null) {
			throw new ArgumentException("{ \"error\" : \"El parametro fechaApertura no puede ser null\" }");
		}
		
		if (fechaCierre == null) {
			throw new ArgumentException("{ \"error\" : \"El parametro fechaCierre no puede ser null\" }");
		}
		
		//Controlar que el id del docente exista
		boolean respuesta = false;
		try {
			respuesta = UsuariosRepository.existeProfesor(docenteId);
		} catch (IOException e1) {
			throw new InternalException("{ \"error\" : \"Error en la peticion a la API de Usuarios\" }");
		}
		
		if (respuesta == false) {
			throw new ArgumentException("{ \"error\" : \"El parametro docenteId identificador del profesor no es correcto\" }");
		}
		
		//Controlar que que la fecha de apertura sea despues de la actual
		if (fechaApertura.before(new Date())) {
			throw new ArgumentException("{ \"error\" : \"El parametro fechaApertura no puede ser anterior a la fecha actual\" }");
		}

		if (fechaApertura.equals(fechaCierre)) {
			throw new ArgumentException("{ \"error\" : \"El parametro fechaApertura no puede ser igual al parametro fechaCierre\" }");
		}
		
		//Controlar que la fecha de cierre sea despues de la de apertura
		if (fechaCierre.before(fechaApertura)) {
			throw new ArgumentException("{ \"error\" : \"El parametro fechaCierre no puede ser anterior al parametro fechaApertura\" }");
		}
		
		Sondeo sondeo = null;
		try {
			sondeo = this.controladorDAO.createSondeo(docenteId, instruccionesAdicionales, fechaApertura, fechaCierre, pregunta);
		
			Sender.notificarEventoNuevoSondeo(sondeo);
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error en la base de datos\" }");
		}
		
		return sondeo.getId();
	}

	public Sondeo getSondeo(String id) throws ArgumentException, NotFoundException, InternalException {
		
		//Comprobar que el id del sondeo tiene datos validos y existe
		if (id == null || id.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro id no puede ser vacio o null\" }");
		}
		
		if (! ObjectId.isValid(id)) {
			throw new ArgumentException("{ \"error\" : \"El parametro id debe no es una cadena hexadecimal correcta\" }");
		}
		
		Sondeo sondeo = null;
		try {
			sondeo = this.controladorDAO.getSondeo(id);
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error en la base de datos\" }");
		}	
		
		if (sondeo == null) {
			throw new NotFoundException("{ \"error\" : \"El sondeo indicado no existe\" }");
		}
		
		return sondeo;
	}

	public List<Sondeo> getSondeos() {
		return this.controladorDAO.getSondeos();
	}

	public void addOpcionSondeo(String id, String idDocente, String opcion) throws ArgumentException, NotFoundException, InternalException {

		//Comprobar que el id del sondeo tiene datos validos y existe
		if (opcion == null || opcion.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro opcion no puede ser vacio o null\" }");
		}
		
		Sondeo sondeo = this.getSondeo(id);
		
		//Comprobar que la opcion a a�adir tiene datos validos y no esta ya a�adida
		for (String s : sondeo.getPregunta().getOpciones()) {
			if (s.equalsIgnoreCase(opcion)) {
				throw new ArgumentException("{ \"error\" : \"La opcion que intenta anadir ya esta anadida\" }");
			}
		}
		
		//Comprobar que el que esta haciendo esta peticion es el docente que cre� el sondeo
		if (!idDocente.equals(sondeo.getDocenteId())) {
			throw new ArgumentException("{ \"error\" : \"Solo el docente que creo el sondeo puede anadirle opciones\" }");
		}
		
		sondeo.addOpcion(opcion);
		
		try {
			this.controladorDAO.updateOpciones(sondeo);
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error en la base de datos\" }");
		}
	}

	public void deleteOpcionSondeo(String id, String idDocente, int index) throws ArgumentException, NotFoundException, InternalException {
		
		Sondeo sondeo = this.getSondeo(id);
		
		//Comprobar que el que esta haciendo esta peticion es el docente que cre� el sondeo
		if (!idDocente.equals(sondeo.getDocenteId())) {
			throw new ArgumentException("{ \"error\" : \"Solo el docente que creo el sondeo puede borrarle opciones\" }");
		}
			
		
		boolean existe = false;
		
		try {
			existe = sondeo.deleteOpcion(index);
			this.controladorDAO.updateOpciones(sondeo);
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error en la base de datos\" }");
		}

		if (!existe) {
			throw new NotFoundException("{ \"error\" : \"El indice indicado no existe\" }");
		}
	}

	public HashMap<String, Integer> verResultados(String id) throws ArgumentException, NotFoundException, InternalException {
		
		Sondeo sondeo = this.getSondeo(id);
		
		HashMap<String, Integer> mapa = new HashMap<String, Integer>();
		
		List<String> opciones = sondeo.getPregunta().getOpciones();
		List<Integer> resultados = sondeo.getPregunta().getResultados();
		
		for (int i = 0; i < opciones.size(); i++) {
			mapa.put(opciones.get(i), resultados.get(i));
		}
		
		return mapa;
	}

	public void votar(String id, String alumnoId, List<Integer> respuestas) throws ArgumentException, NotFoundException, InternalException {
		

		//Comprobar que el id del sondeo tiene datos validos y existe
		Sondeo sondeo = this.getSondeo(id);
		
		//Comprobar que cumple las respuestas minimas y maximas
		if (respuestas.size() < sondeo.getPregunta().getMinimoRespuestas() ||
				respuestas.size() > sondeo.getPregunta().getMaximoRespuestas()) {
			throw new ArgumentException("{ \"error\" : \"El numero de respuestas rellenadas no es correcto\" }");
		}
		
		//Comprobar que no se responde dos o mas veces la misma opcion
		Set<Integer> set = new HashSet<Integer>(respuestas);
		if (set.size() < respuestas.size()){
			throw new ArgumentException("{ \"error\" : \"No se puede responder la misma opcion mas de una vez\" }");
		}
		
		//Comprobar que todas las opciones respondidas estan en el sondeo
		for (int indice : respuestas) {
			if (indice < 0 || indice >= sondeo.getPregunta().getOpciones().size()) {
				throw new ArgumentException("{ \"error\" : \"No se puede responder una opcion que no esta en el Sondeo\" }");
			}
			sondeo.addVoto(indice);
		}
		
		//Comprobar que el que esta respondiendo es un alumno
		boolean respuesta = false;
		try {
			respuesta = UsuariosRepository.existeEstudiante(alumnoId);
		} catch (IOException e1) {
			throw new InternalException("{ \"error\" : \"Error en la peticion a la API de Usuarios\" }");
		}
		
		if (respuesta == false) {
			throw new ArgumentException("{ \"error\" : \"El parametro alumnoId identificador del estudiante no es correcto\" }");
		}

		try {
			this.controladorDAO.updateVotos(sondeo);
			
			Sender.notificarEventoSondeoRespondido(sondeo, alumnoId);
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error en la base de datos\" }");
		}
	}

}
