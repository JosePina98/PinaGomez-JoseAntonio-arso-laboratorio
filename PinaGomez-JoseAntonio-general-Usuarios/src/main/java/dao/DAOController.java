package dao;

import java.util.List;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import repositories.UsuariosRepository;
import schema.TIPO_ROL;
import schema.Usuario;

public class DAOController {
	
	private static UsuariosRepository usuariosRepository;
	private static MongoClient client;
	
    private static DAOController UNICA_INSTANCIA = null;

    private DAOController(){
    	initDB();
    }

    public static DAOController getUnicaInstancia() {
        if (UNICA_INSTANCIA == null) {
        	UNICA_INSTANCIA = new DAOController();
        }
        return UNICA_INSTANCIA;
    }

	private static void initDB() {
		client = MongoClients.create(
				"mongodb://arso:arso-20@cluster0-shard-00-00-xditi.azure.mongodb.net:27017,cluster0-shard-00-01-xditi.azure.mongodb.net:27017,cluster0-shard-00-02-xditi.azure.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority");

		MongoDatabase mongo = client.getDatabase("arso");

		usuariosRepository = new UsuariosRepository(mongo.getCollection("usuarios"));
	}

	public Usuario createUsuario(String nombre, String correo, TIPO_ROL rol) {

		return usuariosRepository.createUsuario(nombre, correo, rol);
	}
	
	public boolean deleteUsuario(String correo) {

		return usuariosRepository.deleteUsuario(correo);
	}

	public List<Usuario> getAllUsuarios() {

		return usuariosRepository.getAll();
	}
	
	public List<Usuario> getEstudiantes() {
		
		return usuariosRepository.getEstudiantes();
	}
	
	public List<Usuario> getProfesores() {
		
		return usuariosRepository.getProfesores();
	}

	public Usuario getUsuario(String correo) {

		return usuariosRepository.findByCorreo(correo);
	}

}
