package schema;

import java.util.Date;

public class Sondeo {
	
	protected String id;
	protected String docenteId;
	protected String instruccionesAdicionales;
	protected Date fechaApertura;
	protected Date fechaCierre;
	protected Pregunta pregunta;
	
	public Sondeo(String id, String docenteId, String instruccionesAdicionales, Date fechaApertura, Date fechaCierre,
			Pregunta pregunta) {
		super();
		this.id = id;
		this.docenteId = docenteId;
		this.instruccionesAdicionales = instruccionesAdicionales;
		this.fechaApertura = fechaApertura;
		this.fechaCierre = fechaCierre;
		this.pregunta = pregunta;
	}
	
	//Metodos
	
	public void addOpcion(String opcion) {
		this.pregunta.addOpcion(opcion);
	}
	
	public void deleteOpcion(int index) {
		this.pregunta.deleteOpcion(index);
	}
	
	public void setVotos(int index, int votos) {
		this.pregunta.setVotos(index, votos);
	}
	
	public void addVoto(int index) {
		this.pregunta.addVoto(index);
	}

	//Getters y Setters
	
	public String getDocenteId() {
		return docenteId;
	}

	public void setDocenteId(String docenteId) {
		this.docenteId = docenteId;
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

	public Pregunta getPregunta() {
		return pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}

	public String getId() {
		return id;
	}
	
}
