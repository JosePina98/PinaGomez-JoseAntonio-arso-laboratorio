package dao;

import java.util.Date;
import java.util.List;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import repositories.SondeosRepository;
import schema.Pregunta;
import schema.Sondeo;

public class DAOController {
	
	private static SondeosRepository sondeoRepository;
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

		sondeoRepository = new SondeosRepository(mongo.getCollection("sondeos"));
	}

	public Sondeo createSondeo(String docenteId, String instruccionesAdicionales, Date fechaApertura, Date fechaCierre,
			Pregunta pregunta) {

		return sondeoRepository.createSondeo(docenteId, instruccionesAdicionales, fechaApertura, fechaCierre, pregunta);
	}

	public Sondeo getSondeo(String id) {

		return sondeoRepository.findById(id);
	}

	public List<Sondeo> getSondeos() {

		return sondeoRepository.getAll();
	}

	public void updateOpciones(Sondeo sondeo) {
		
		sondeoRepository.updateOpciones(sondeo);
		sondeoRepository.updateResultados(sondeo);
	}

	public void updateVotos(Sondeo sondeo) {
		sondeoRepository.updateResultados(sondeo);
	}

}
