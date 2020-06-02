package controller;

import javax.servlet.annotation.WebServlet;

import com.coxautodev.graphql.tools.SchemaParser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;
import repositories.TareasRepository;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {
	private static TareasRepository tareasRepository;
	private static MongoClient client;

	private static void initDB() {
		client = MongoClients.create(
				"mongodb://arso:arso-20@cluster0-shard-00-00-xditi.azure.mongodb.net:27017,cluster0-shard-00-01-xditi.azure.mongodb.net:27017,cluster0-shard-00-02-xditi.azure.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority");

		MongoDatabase mongo = client.getDatabase("arso");

		tareasRepository = new TareasRepository(mongo.getCollection("tareas"));
	}

	@Override
	public void destroy() {
		super.destroy();
		client.close();
	}

	public GraphQLEndpoint() {
		super(buildSchema());
	}

	private static GraphQLSchema buildSchema() {

		initDB();

		return SchemaParser.newParser().file("schema.graphqls")
				.resolvers(new Query(tareasRepository), new Mutation(tareasRepository)).build()
				.makeExecutableSchema();
	}
}
