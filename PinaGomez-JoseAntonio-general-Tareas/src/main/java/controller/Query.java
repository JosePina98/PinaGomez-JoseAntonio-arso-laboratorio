package controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.json.JsonObject;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.rabbitmq.client.Reciever;

import repositories.TareasRepository;
import repositories.UsuariosRepository;
import schema.model.Tarea;

public class Query implements GraphQLRootResolver {
	
	private static final String TIPO_NEW = "new";
	private static final String TIPO_REMOVE = "remove";
	private static final String STRING_TIPO = "tipo";
	private static final String STRING_PROFESOR = "profesor";
	private static final String STRING_ID_TAREA = "idTarea";
	private static final String STRING_SERVICIO = "servicio";
	private static final String STRING_ESTUDIANTE = "estudiante";
	
	private TareasRepository tareasRepository;
	
	public Query(TareasRepository tareasRepository) {
		this.tareasRepository = tareasRepository;
	}

	public List<Tarea> getAllTareasPendientes() {
		List<Tarea> listaTareas = new LinkedList<Tarea>();

		List<String> listaEstudiantes = null;
		try {
			listaEstudiantes = UsuariosRepository.getAllEstudiantes();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		boolean mensajesPendientes = true;
		try {			
			while (mensajesPendientes) {
				
				JsonObject objeto = Reciever.recieveMessage();
				
				if (objeto == null) {
					mensajesPendientes = false;
				} else {
					
					String tipo = objeto.getString(STRING_TIPO);
					if (tipo.equals(TIPO_NEW)) {		
						
						for (String idEstudiante : listaEstudiantes) {
							Tarea tarea = tareasRepository.saveTarea(objeto.getString(STRING_PROFESOR), idEstudiante,
									objeto.getString(STRING_ID_TAREA), objeto.getString(STRING_SERVICIO));
							
							listaTareas.add(tarea);
						}
					} else {
						if (tipo.equals(TIPO_REMOVE)) {
							
							tareasRepository.removeTarea(objeto.getString(STRING_ID_TAREA), objeto.getString(STRING_ESTUDIANTE));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Tarea> listaAux = tareasRepository.getAllTareas();
		
		for (Tarea tarea : listaAux) {
			listaTareas.add(tarea);
		}
		return listaTareas;
	}
}
