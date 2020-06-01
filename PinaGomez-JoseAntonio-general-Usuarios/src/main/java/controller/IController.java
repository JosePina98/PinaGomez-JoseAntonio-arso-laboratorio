package controller;

import java.util.List;

import exceptions.ArgumentException;
import exceptions.InternalException;
import schema.Usuario;

public interface IController {

	public String createUsuario(String nombre, String correo, String rol) throws ArgumentException, InternalException;

	public Usuario getUsuario(String correo) throws ArgumentException, InternalException;

	public List<Usuario> getEstudiantes() throws InternalException;

	public List<Usuario> getProfesores() throws InternalException;

	public boolean deleteUsuario(String correo) throws ArgumentException, InternalException;

	public List<Usuario> getAllUsuarios() throws InternalException;
}
