package bookle.tipos;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "actividades")
public class ListadoActividades {

    private LinkedList<ItemActividad> actividad;
    
    public ListadoActividades() {
        
        this.actividad = new LinkedList<>();
    }
    
    public LinkedList<ItemActividad> getActividad() {
        return actividad;
    }   
}
