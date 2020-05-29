package poema;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"titulo",
		"verso"
})
@XmlRootElement(name = "acta")
public class Poema {
	
	@XmlElement(required = true)
	protected List<String> verso;
	@XmlElement(required = true)
	protected String titulo;
	
	@XmlAttribute(name = "fecha", required = true)
	protected String fecha;
	@XmlAttribute(name = "lugar", required = true)
	protected String lugar;
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public String getLugar() {
		return lugar;
	}
	
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	
	public List<String> getVerso() {
		if (verso == null) {
			verso = new ArrayList<String>();
		}
		return this.verso;
	}

}
