package controller;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import repositories.TareasRepository;

public class Mutation implements GraphQLRootResolver {
	private TareasRepository tareasRepository;

	public Mutation(TareasRepository tareasRepository) {
		this.tareasRepository = tareasRepository;
	}

	public boolean removeTarea(String idTarea) {
		
		return tareasRepository.removeTarea(idTarea);
	}

}
