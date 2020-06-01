package controller;

import java.util.List;

import dao.DAOController;
import exceptions.ArgumentException;
import exceptions.InternalException;
import schema.TIPO_ROL;
import schema.Usuario;

public class Controller {
	
	private DAOController controladorDAO = DAOController.getUnicaInstancia();

	public String createUsuario(String nombre, String correo, String rol) throws ArgumentException, InternalException {
		
		if (nombre == null || nombre.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro nombre no puede ser vacio o null\" }");
		}
		
		if (correo == null || correo.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro correo no puede ser vacio o null\" }");
		}
		
		if (rol == null || rol.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro rol no puede ser vacio o null\" }");
		}
		
		TIPO_ROL tipo_rol = null;
		if (rol.equalsIgnoreCase(TIPO_ROL.ESTUDIANTE.toString())) {
			tipo_rol = TIPO_ROL.ESTUDIANTE;
		} else {
			if (rol.equalsIgnoreCase(TIPO_ROL.PROFESOR.toString())) {
				tipo_rol = TIPO_ROL.PROFESOR;
			} else {
				throw new ArgumentException("{ \"error\" : \"El rol indicado no es correcto. Debe ser Estudiante o Profesor.\" }");
			}
		}
		
		Usuario usuario = null;
		try {
			usuario = this.controladorDAO.createUsuario(nombre, correo, tipo_rol);
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error en la base de datos\" }");
		}
		
		if (usuario == null) {
			throw new ArgumentException("{ \"error\" : \"Ya existe un usuario con ese correo.\" }");
		}
		
		return usuario.getCorreo();
	}

	public Usuario getUsuario(String correo) throws ArgumentException, InternalException {
		if (correo == null || correo.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro correo no puede ser vacio o null\" }");
		}
		
		Usuario usuario = null;
		try {
			usuario = this.controladorDAO.getUsuario(correo);
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error en la base de datos\" }");			
		}
		
		if (usuario == null) {
			throw new ArgumentException("{ \"error\" : \"No existe un usuario con ese correo.\" }");		
		}
		
		return usuario;
	}

	public List<Usuario> getEstudiantes() throws InternalException {

		List<Usuario> lista = null;
		try {
			lista = this.controladorDAO.getEstudiantes();
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error en la base de datos\" }");
		}
		return lista;
	}

	public List<Usuario> getProfesores() throws InternalException {
		
		List<Usuario> lista = null;
		try {
			lista = this.controladorDAO.getProfesores();
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error en la base de datos\" }");
		}
		return lista;
	}

	public boolean deleteUsuario(String correo) throws ArgumentException, InternalException {
		if (correo == null || correo.isEmpty()) {
			throw new ArgumentException("{ \"error\" : \"El parametro correo no puede ser vacio o null\" }");
		}
		
		boolean resultado = false;
		try {
			resultado = this.controladorDAO.deleteUsuario(correo);
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error en la base de datos\" }");
		}
		
		if (resultado == false) {
			throw new ArgumentException("{ \"error\" : \"No existe un usuario con ese correo.\" }");
		}
		return resultado;
	}

	public List<Usuario> getAllUsuarios() throws InternalException {

		List<Usuario> lista = null;
		try {
			lista = this.controladorDAO.getAllUsuarios();
		} catch (Exception e) {
			throw new InternalException("{ \"error\" : \"Error en la base de datos\" }");
		}
		return lista;
	}

}
