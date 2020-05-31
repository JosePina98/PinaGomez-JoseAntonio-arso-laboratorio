package controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import dao.DAOController;
import schema.Pregunta;
import schema.Sondeo;

public class Controller {
	
	private DAOController controladorDAO = DAOController.getUnicaInstancia();

	public Pregunta createPregunta(String textoPregunta, int minimoRespuestas, int maximoRespuestas) {
		return new Pregunta(textoPregunta, minimoRespuestas, maximoRespuestas);
	}

	public String createSondeo(String docenteId, String instruccionesAdicionales, Date fechaApertura, Date fechaCierre,
			Pregunta pregunta) {

		Sondeo sondeo = this.controladorDAO.createSondeo(docenteId, instruccionesAdicionales, fechaApertura, fechaCierre, pregunta);
		
		return sondeo.getId();
	}

	public Sondeo getSondeo(String id) {
		
		Sondeo sondeo = this.controladorDAO.getSondeo(id);
		
		return sondeo;
	}

	public void addOpcionSondeo(String id, String opcion) {

		Sondeo sondeo = this.getSondeo(id);
		
		sondeo.addOpcion(opcion);
		
		this.controladorDAO.updateOpciones(sondeo);
	}

	public void deleteOpcionSondeo(String id, int index) {
		Sondeo sondeo = this.getSondeo(id);
		
		sondeo.deleteOpcion(index);
		
		this.controladorDAO.updateOpciones(sondeo);		
	}
	
	public void responderSondeo(MultivaluedMap<String, String> respuesta) {
		
	}

	public HashMap<String, Integer> verResultados(String id) {
		
		Sondeo sondeo = this.getSondeo(id);
		
		HashMap<String, Integer> mapa = new HashMap<String, Integer>();
		
		List<String> opciones = sondeo.getPregunta().getOpciones();
		List<Integer> resultados = sondeo.getPregunta().getResultados();
		
		for (int i = 0; i < opciones.size(); i++) {
			mapa.put(opciones.get(i), resultados.get(i));
		}
		
		return mapa;
	}

	public void votar(String id, List<Integer> respuestas) {

		Sondeo sondeo = this.getSondeo(id);
		
		for (int indice : respuestas) {
			sondeo.addVoto(indice);
		}
		
		this.controladorDAO.updateVotos(sondeo);
	}

}
