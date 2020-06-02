package schema.model;

public class Tarea {

	private String id;
	
	private String idCreador;
	private String idTarea;
	private String servicio;
	
	public Tarea(String id, String idCreador, String idTarea, String servicio) {
		super();
		this.id = id;
		this.idCreador = idCreador;
		this.idTarea = idTarea;
		this.servicio = servicio;
	}

	public String getIdCreador() {
		return idCreador;
	}

	public String getIdTarea() {
		return idTarea;
	}

	public String getServicio() {
		return servicio;
	}

	public String getId() {
		return id;
	}

}
