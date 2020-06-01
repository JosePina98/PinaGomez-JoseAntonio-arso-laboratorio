package controller;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import dao.DAOController;
import exceptions.ArgumentException;
import exceptions.NotFoundException;
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
		if (minimoRespuestas < 0) {
			throw new ArgumentException("{ \"error\" : \"El parametro minimoRespuestas no puede ser menor que 0\" }");
		}
		
		//Controlar que el numero maximo de respuestas sea igual o mayor que el minimo
		if (maximoRespuestas < minimoRespuestas) {
			throw new ArgumentException("{ \"error\" : \"El parametro maximoRespuestas no puede ser menor que minimoRespuestas\" }");
		}
		
		return new Pregunta(textoPregunta, minimoRespuestas, maximoRespuestas);
	}

	public String createSondeo(String docenteId, String instruccionesAdicionales, Date fechaApertura, Date fechaCierre,
			Pregunta pregunta) throws ArgumentException {
		
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
		
		//Controlar que que la fecha de apertura sea despues de la actual
		if (fechaApertura.before(new Date())) {
			throw new ArgumentException("{ \"error\" : \"El parametro fechaApertura no puede ser anterior a la fecha actual\" }");
		}
		
		//Controlar que la fecha de cierre sea despues de la de apertura
		if (fechaCierre.before(fechaApertura)) {
			throw new ArgumentException("{ \"error\" : \"El parametro fechaCierre no puede ser anterior al parametro fechaApertura\" }");
		}
		
		//Ver por que la hora de la fecha se escribe como 1 hora menos en bbdd
		System.out.println("Fecha apertura: " + fechaApertura.toString());
		System.out.println("Fecha cierre: " + fechaCierre.toString());
		
		Sondeo sondeo = this.controladorDAO.createSondeo(docenteId, instruccionesAdicionales, fechaApertura, fechaCierre, pregunta);
		
		return sondeo.getId();
	}

	public Sondeo getSondeo(String id) throws ArgumentException, NotFoundException {
		
		//Comprobar que el id del sondeo tiene datos validos y existe
		if (id == null || id.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro id no puede ser vacio o null\" }");
		}
		
		if (! ObjectId.isValid(id)) {
			throw new ArgumentException("{ \"error\" : \"El parametro id debe no es una cadena hexadecimal correcta\" }");
		}
		
		Sondeo sondeo = this.controladorDAO.getSondeo(id);
		
		if (sondeo == null) {
			throw new NotFoundException("{ \"error\" : \"El sondeo indicado no existe\" }");
		}
		
		return sondeo;
	}

	public void addOpcionSondeo(String id, String idDocente, String opcion) throws ArgumentException, NotFoundException {

		//Comprobar que el id del sondeo tiene datos validos y existe
		if (opcion == null || opcion.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro opcion no puede ser vacio o null\" }");
		}
		
		Sondeo sondeo = this.getSondeo(id);
		
		//Comprobar que la opcion a añadir tiene datos validos y no esta ya añadida
		for (String s : sondeo.getPregunta().getOpciones()) {
			if (s.equalsIgnoreCase(opcion)) {
				throw new ArgumentException("{ \"error\" : \"La opcion que intenta anadir ya esta anadida\" }");
			}
		}
		
		//Comprobar que el que esta haciendo esta peticion es el docente que creó el sondeo
		if (!idDocente.equals(sondeo.getDocenteId())) {
			throw new ArgumentException("{ \"error\" : \"Solo el docente que creo el sondeo puede anadirle opciones\" }");
		}
		
		sondeo.addOpcion(opcion);
		
		this.controladorDAO.updateOpciones(sondeo);
	}

	public void deleteOpcionSondeo(String id, String idDocente, int index) throws ArgumentException, NotFoundException {
		
		Sondeo sondeo = this.getSondeo(id);
		
		//Comprobar que el que esta haciendo esta peticion es el docente que creó el sondeo
		if (!idDocente.equals(sondeo.getDocenteId())) {
			throw new ArgumentException("{ \"error\" : \"Solo el docente que creo el sondeo puede anadirle opciones\" }");
		}
				
		sondeo.deleteOpcion(index);
		
		this.controladorDAO.updateOpciones(sondeo);		
	}

	public HashMap<String, Integer> verResultados(String id) throws ArgumentException, NotFoundException {
		
		Sondeo sondeo = this.getSondeo(id);
		
		HashMap<String, Integer> mapa = new HashMap<String, Integer>();
		
		List<String> opciones = sondeo.getPregunta().getOpciones();
		List<Integer> resultados = sondeo.getPregunta().getResultados();
		
		for (int i = 0; i < opciones.size(); i++) {
			mapa.put(opciones.get(i), resultados.get(i));
		}
		
		return mapa;
	}

	public void votar(String id, String alumnoId, List<Integer> respuestas) throws ArgumentException, NotFoundException {
		

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
			if (indice >= sondeo.getPregunta().getOpciones().size()) {
				throw new ArgumentException("{ \"error\" : \"No se puede responder una opcion que no esta en el Sondeo\" }");
			}
			sondeo.addVoto(indice);
		}
		
		//Comprobar que el que esta respondiendo es un alumno

		this.controladorDAO.updateVotos(sondeo);
	}

}
