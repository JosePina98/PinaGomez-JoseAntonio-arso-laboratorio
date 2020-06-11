package repositories;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import schema.model.Tarea;


public class TareasRepository {

	private final MongoCollection<Document> tareas;

	public TareasRepository(MongoCollection<Document> tareas) {
		this.tareas = tareas;
	}

	public Tarea saveTarea(String idCreador, String idEstudiante, String idTarea, String servicio) {
		Document doc = new Document();
		doc.append("idCreador", idCreador);
		doc.append("idEstudiante", idEstudiante);
		doc.append("idTarea", idTarea);
		doc.append("servicio", servicio);
		
		tareas.insertOne(doc);
		return docToTarea(doc);
	}

	private Tarea docToTarea(Document doc) {
		return new Tarea(doc.get("_id").toString(), doc.getString("idCreador"), doc.getString("idEstudiante"), 
				doc.getString("idTarea"), doc.getString("servicio"));
	}

	public List<Tarea> getAllTareas() {
		List<Tarea> allTareas = new ArrayList<Tarea>();
		for (Document doc : tareas.find()) {
			allTareas.add(docToTarea(doc));
		}
		return allTareas;
	}

	public boolean removeTarea(String idTarea, String idEstudiante) {
		if (this.findByIdTarea(idTarea) != null) {
			tareas.deleteOne(Filters.and(Filters.eq("idTarea", idTarea), Filters.eq("idEstudiante", idEstudiante)));
			return true;
		} else {
			return false;
		}
	}

	private Tarea findByIdTarea(String idTarea) {
		FindIterable<Document> listaDocs = tareas.find(Filters.eq("idTarea", idTarea));
		
		Document documento = listaDocs.first();
		
		if (documento == null) {
			return null;
		} else {
			return docToTarea(documento);
		}
	}

}
