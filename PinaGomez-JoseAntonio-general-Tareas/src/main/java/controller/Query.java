package controller;

import java.util.LinkedList;
import java.util.List;
import javax.json.JsonObject;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.rabbitmq.client.Reciever;

import repositories.TareasRepository;
import schema.model.Tarea;

public class Query implements GraphQLRootResolver {
	
	private TareasRepository tareasRepository;
	
	public Query(TareasRepository tareasRepository) {
		this.tareasRepository = tareasRepository;
	}

	public List<Tarea> getAllTareasPendientes() {
		List<Tarea> listaTareas = tareasRepository.getAllTareas();
		
		if (listaTareas == null) {
			listaTareas = new LinkedList<Tarea>();
		}

		boolean mensajesPendientes = true;
		try {			
			while (mensajesPendientes) {
				
				JsonObject objeto = Reciever.recieveMessage();
				
				if (objeto == null) {
					mensajesPendientes = false;
				} else {
					Tarea tarea = tareasRepository.saveTarea(objeto.getString("profesor"), objeto.getString("idTarea"), objeto.getString("servicio"));
					listaTareas.add(tarea);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listaTareas;
	}
}
