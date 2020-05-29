package schema;

import java.util.LinkedList;
import java.util.List;

public class Pregunta {

	protected String textoPregunta;
	protected int minimoRespuestas;
	protected int maximoRespuestas;
	protected List<String> opciones;
	protected List<Integer> resultados;
	
	public Pregunta(String textoPregunta, int minimoRespuestas, int maximoRespuestas) {
		super();
		this.textoPregunta = textoPregunta;
		this.minimoRespuestas = minimoRespuestas;
		this.maximoRespuestas = maximoRespuestas;
		this.opciones = new LinkedList<String>();
		this.resultados = new LinkedList<Integer>();
	}
	
	//Metodos
	public void addOpcion(String opcion) {
		this.opciones.add(opcion);
		this.resultados.add(0);
	}
	
	public void deleteOpcion(int index) {
		if (index >= 0 && index < this.opciones.size()) {
			this.opciones.remove(index);
			this.resultados.remove(index);
		}
	}
	
	public void setVotos(int index, int votos) {
		if (index >= 0 && index < this.resultados.size()) {			
			this.resultados.set(index, votos);
		}
	}
	
	public void addVoto(int index) {
		if (index >= 0 && index < this.opciones.size()) {
			int votosActuales = this.resultados.get(index);
			this.resultados.set(index, votosActuales + 1);
		}
	}
	
	//Getters y Setters

	public String getTextoPregunta() {
		return textoPregunta;
	}

	public void setTextoPregunta(String textoPregunta) {
		this.textoPregunta = textoPregunta;
	}

	public int getMinimoRespuestas() {
		return minimoRespuestas;
	}

	public void setMinimoRespuestas(int minimoRespuestas) {
		this.minimoRespuestas = minimoRespuestas;
	}

	public int getMaximoRespuestas() {
		return maximoRespuestas;
	}

	public void setMaximoRespuestas(int maximoRespuestas) {
		this.maximoRespuestas = maximoRespuestas;
	}

	public List<String> getOpciones() {
		return opciones;
	}

	public void setOpciones(List<String> opciones) {
		this.opciones = opciones;
	}

	public List<Integer> getResultados() {
		return resultados;
	}

	public void setResultados(List<Integer> resultados) {
		this.resultados = resultados;
	}
	
}
