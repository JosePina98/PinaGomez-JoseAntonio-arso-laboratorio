package bookle.tipos;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "actividades")
public class ItemActividad {
	
	@XmlElement(required = true)
	private String titulo;
	@XmlElement(required = true)
	private String enlace;
	
	public ItemActividad() {
		
	}
	
	public ItemActividad(String titulo, String enlace) {
		this.titulo = titulo;
		this.enlace = enlace;
	}
	
	public String getTitulo() {
		return titulo;
	}

	public String getEnlace() {
		return enlace;
	}

}
