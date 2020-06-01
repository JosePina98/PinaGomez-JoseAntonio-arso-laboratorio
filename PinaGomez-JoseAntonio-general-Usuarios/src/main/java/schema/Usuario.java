package schema;

public class Usuario {
	
	private String id;
	private String nombre;
	private String correo;
	private TIPO_ROL rol;
	
	public Usuario(String id, String nombre, String correo, TIPO_ROL rol) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.correo = correo;
		this.rol = rol;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public TIPO_ROL getRol() {
		return rol;
	}

	public void setRol(TIPO_ROL rol) {
		this.rol = rol;
	}

}
