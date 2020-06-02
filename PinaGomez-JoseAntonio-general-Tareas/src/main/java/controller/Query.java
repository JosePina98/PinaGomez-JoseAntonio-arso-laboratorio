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

		System.out.println("Antiguas: " + listaTareas.size());
		
		List<JsonObject> listaJson = null;
		try {
			listaJson = Reciever.recieveMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (listaJson != null) {
			System.out.println("Nuevas: " + listaJson.size());
			for (JsonObject o : listaJson) {
				Tarea tarea = tareasRepository.saveTarea(o.getString("profesor"), o.getString("idTarea"), o.getString("servicio"));
				listaTareas.add(tarea);
			}
		}
		
		System.out.println("Total: " + listaTareas.size());
		return listaTareas;
	}
	
	public List<Tarea> getTareasPendientesSondeos() {
		return tareasRepository.getAllTareasSondeos();
	}
	
	public List<Tarea> getTareasPendientesApuntate() {
		return tareasRepository.getAllTareasApuntate();
	}
}
