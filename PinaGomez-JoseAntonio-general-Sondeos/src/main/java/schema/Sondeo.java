package schema;

import java.util.Date;
import java.util.List;

public class Sondeo {
	
	protected String pregunta;
	protected String instruccionesAdicionales;
	protected Date fechaApertura;
	protected Date fechaCierre;
	protected int frecuencia; //enum
	protected List<String> listaOpciones;
	protected int minimoRespuestas;
	protected int maximoRespuestas;
	protected int visibilidadResultados; //enum
	protected List<String> listaRespuestas;
	protected String docenteId;

	public Sondeo(String pregunta, String instruccionesAdicionales, Date fechaApertura, Date fechaCierre,
			int frecuencia, List<String> listaOpciones, int minimoRespuestas, int maximoRespuestas,
			int visibilidadResultados, List<String> listaRespuestas, String docenteId) {
		super();
		this.pregunta = pregunta;
		this.instruccionesAdicionales = instruccionesAdicionales;
		this.fechaApertura = fechaApertura;
		this.fechaCierre = fechaCierre;
		this.frecuencia = frecuencia;
		this.listaOpciones = listaOpciones;
		this.minimoRespuestas = minimoRespuestas;
		this.maximoRespuestas = maximoRespuestas;
		this.visibilidadResultados = visibilidadResultados;
		this.listaRespuestas = listaRespuestas;
		this.docenteId = docenteId;
	}

	public void addOpcion(String nuevaOpcion) {
		listaOpciones.add(nuevaOpcion);
	}
	
	public void removeOpcion(String opcion) {
		listaOpciones.remove(opcion);
	}
	
	//GETTERS Y SETTERS 

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public String getInstruccionesAdicionales() {
		return instruccionesAdicionales;
	}

	public void setInstruccionesAdicionales(String instruccionesAdicionales) {
		this.instruccionesAdicionales = instruccionesAdicionales;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public int getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(int frecuencia) {
		this.frecuencia = frecuencia;
	}

	public List<String> getListaOpciones() {
		return listaOpciones;
	}

	public void setListaOpciones(List<String> listaOpciones) {
		this.listaOpciones = listaOpciones;
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

	public int getVisibilidadResultados() {
		return visibilidadResultados;
	}

	public void setVisibilidadResultados(int visibilidadResultados) {
		this.visibilidadResultados = visibilidadResultados;
	}

	public List<String> getListaRespuestas() {
		return listaRespuestas;
	}

	public void setListaRespuestas(List<String> listaRespuestas) {
		this.listaRespuestas = listaRespuestas;
	}

	public String getDocenteId() {
		return docenteId;
	}

	public void setDocenteId(String docenteId) {
		this.docenteId = docenteId;
	}	
	
}
