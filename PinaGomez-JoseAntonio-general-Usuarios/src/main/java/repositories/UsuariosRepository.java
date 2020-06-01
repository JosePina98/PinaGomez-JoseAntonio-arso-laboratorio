package repositories;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import schema.TIPO_ROL;
import schema.Usuario;

public class UsuariosRepository {

	private final MongoCollection<Document> usuarios;

	public UsuariosRepository(MongoCollection<Document> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario createUsuario(String nombre, String correo, TIPO_ROL rol) {
		if (this.findByCorreo(correo) != null) {
			return null;
		}
		
		Document doc = new Document();
		doc.append("nombre", nombre);
		doc.append("correo", correo);
		doc.append("rol", rol.toString());
		
		usuarios.insertOne(doc);
		return docToUsuario(doc);
	}

	private Usuario docToUsuario(Document doc) {
		TIPO_ROL rol = null;
		
		String sRol = doc.getString("rol");
		if (sRol.equalsIgnoreCase(TIPO_ROL.ESTUDIANTE.toString())) {
			rol = TIPO_ROL.ESTUDIANTE;
		} else {
			rol = TIPO_ROL.PROFESOR;
		}
		
		Usuario usuario = new Usuario(doc.get("_id").toString(), doc.getString("nombre"), doc.getString("correo"), rol);
		
		return usuario;
	}

	public boolean deleteUsuario(String correo) {
		
		if (this.findByCorreo(correo) != null) {
			usuarios.deleteOne(Filters.eq("correo", correo));
			return true;
		} else {
			return false;
		}
	}

	public List<Usuario> getAll() {
		FindIterable<Document> doc = usuarios.find();
		LinkedList<Usuario> usuariosList = new LinkedList<Usuario>();
		
		MongoCursor<Document> cursor = doc.iterator();
		try {
			while (cursor.hasNext()) {
				usuariosList.add(docToUsuario(cursor.next()));
			}
		} finally {
			cursor.close();
		}

		return usuariosList;
	}

	public List<Usuario> getEstudiantes() {
		FindIterable<Document> doc = usuarios.find(Filters.eq("rol", "ESTUDIANTE"));
		LinkedList<Usuario> usuariosList = new LinkedList<Usuario>();
		
		MongoCursor<Document> cursor = doc.iterator();
		try {
			while (cursor.hasNext()) {
				usuariosList.add(docToUsuario(cursor.next()));
			}
		} finally {
			cursor.close();
		}

		return usuariosList;
	}

	public List<Usuario> getProfesores() {
		FindIterable<Document> doc = usuarios.find(Filters.eq("rol", "PROFESOR"));
		LinkedList<Usuario> usuariosList = new LinkedList<Usuario>();
		
		MongoCursor<Document> cursor = doc.iterator();
		try {
			while (cursor.hasNext()) {
				usuariosList.add(docToUsuario(cursor.next()));
			}
		} finally {
			cursor.close();
		}

		return usuariosList;
	}

	public Usuario findByCorreo(String correo) {
		FindIterable<Document> listaDocs = usuarios.find(Filters.eq("correo", correo));
		
		Document documento = listaDocs.first();
		
		if (documento == null) {
			return null;
		} else {
			return docToUsuario(documento);
		}
	}

}
