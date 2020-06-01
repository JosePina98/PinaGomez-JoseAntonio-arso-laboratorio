package repositories;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import schema.Pregunta;
import schema.Sondeo;

public class SondeoRepository {

	private final MongoCollection<Document> sondeos;

	public SondeoRepository(MongoCollection<Document> sondeos) {
		this.sondeos = sondeos;
	}
	
	@SuppressWarnings("unchecked")
	private Sondeo docToSondeo(Document doc) {
		Document docPregunta = doc.get("pregunta", Document.class);
		
		Pregunta pregunta = new Pregunta(docPregunta.getString("textoPregunta"), 
				docPregunta.getInteger("minimoRespuestas"),
				docPregunta.getInteger("maximoRespuestas"));
		
		List<String> listaOpciones = (List<String>) docPregunta.get("opciones", List.class);
		for (int i = 0; i < listaOpciones.size(); i++) {
			pregunta.addOpcion(listaOpciones.get(i));
		}
		
		List<Integer> listaResultados = (List<Integer>) docPregunta.get("resultados", List.class);
		for (int i = 0; i < listaResultados.size(); i++) {
			pregunta.setVotos(i, listaResultados.get(i));
		}
		
		return new Sondeo(doc.get("_id").toString(), doc.getString("docenteId"),
				doc.getString("instruccionesAdicionales"), doc.getDate("fechaApertura"), 
				doc.getDate("fechaCierre"), pregunta);
	}

	public Sondeo createSondeo(String docenteId, String instruccionesAdicionales, Date fechaApertura, Date fechaCierre,
			Pregunta pregunta) {
		Document preguntaDoc = new Document();
		preguntaDoc.append("textoPregunta", pregunta.getTextoPregunta());
		preguntaDoc.append("minimoRespuestas", pregunta.getMinimoRespuestas());
		preguntaDoc.append("maximoRespuestas", pregunta.getMaximoRespuestas());
		LinkedList<String> listaOpciones = new LinkedList<String>();
		preguntaDoc.append("opciones", listaOpciones);
		LinkedList<String> listaResultados = new LinkedList<String>();
		preguntaDoc.append("resultados", listaResultados);
		
		Document doc = new Document();
		doc.append("docenteId", docenteId);
		doc.append("instruccionesAdicionales", instruccionesAdicionales);
		doc.append("fechaApertura", fechaApertura);
		doc.append("fechaCierre", fechaCierre);
		doc.append("pregunta", preguntaDoc);
		
		sondeos.insertOne(doc);
		return docToSondeo(doc);
	}
	
	public Sondeo findById(String id) throws IllegalArgumentException {

		FindIterable<Document> listaDocs = sondeos.find(Filters.eq("_id", new ObjectId(id)));
		
		Document documento = listaDocs.first();
		
		if (documento != null) {
			return docToSondeo(documento);			
		} else {
			return null;
		}
	}
	
	public void updateOpciones(Sondeo sondeo) {
		
		sondeos.updateOne(Filters.eq("_id", new ObjectId(sondeo.getId())), 
				new Document("$set", new Document("pregunta.opciones", sondeo.getPregunta().getOpciones())));
	}
	
	public void updateResultados(Sondeo sondeo) {
		
		sondeos.updateOne(Filters.eq("_id", new ObjectId(sondeo.getId())), 
				new Document("$set", new Document("pregunta.resultados", sondeo.getPregunta().getResultados())));
	}

}
