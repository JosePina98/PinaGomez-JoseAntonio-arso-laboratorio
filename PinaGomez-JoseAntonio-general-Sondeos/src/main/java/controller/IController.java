package controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import exceptions.ArgumentException;
import exceptions.NotFoundException;
import schema.Pregunta;
import schema.Sondeo;

public interface IController {

	public Pregunta createPregunta(String textoPregunta, int minimoRespuestas, int maximoRespuestas) throws ArgumentException;

	public String createSondeo(String docenteId, String instruccionesAdicionales, Date fechaApertura, Date fechaCierre, Pregunta pregunta) throws ArgumentException;

	public Sondeo getSondeo(String id) throws ArgumentException, NotFoundException;

	public void addOpcionSondeo(String id, String idDocente, String opcion) throws ArgumentException, NotFoundException;

	public void deleteOpcionSondeo(String id, String idDocente, int index) throws ArgumentException, NotFoundException;

	public HashMap<String, Integer> verResultados(String id) throws ArgumentException, NotFoundException;
	
	public void votar(String id, String alumnoId, List<Integer> respuestas) throws ArgumentException, NotFoundException;

}
