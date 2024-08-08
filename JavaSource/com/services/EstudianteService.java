package com.services;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.EstudianteDAO;
import com.dto.EstudianteDTO;
import com.dto.UsuarioDTO;
import com.entities.Estudiante;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

//Este es un Service
@Data
@Stateless
@LocalBean
public class EstudianteService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private EstudianteDAO estudianteBean;
	
	public String registrarEstudiante(UsuarioDTO usuDTO,EstudianteDTO estDTO) {
		try {
			Estudiante estudianteNuevo = dtoToEstudiante(usuDTO, estDTO);
			estudianteBean.crear(estudianteNuevo);
			
			return "";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return "";
	}
	public String modificarEstudiante(UsuarioDTO usuDTO,EstudianteDTO estDTO) {
		try {
			Estudiante estudiante = dtoToEstudiante(usuDTO, estDTO);
			estudianteBean.modificar(estudiante);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return "";
	}
	public List<Estudiante> obtenerTodosAnalista (){
		return estudianteBean.obtenerTodos();
	}
	public Estudiante buscarEstudiantePorId (long id) {
		return estudianteBean.buscarEstudiante(id);
	}
	public Estudiante dtoToEstudiante (UsuarioDTO usuDTO,EstudianteDTO estDTO) {
		Estudiante estudiante = new Estudiante();
		estudiante.setIdUsuario(usuDTO.getIdUsuario());
		estudiante.setNombre(usuDTO.getNombre());
		estudiante.setApellido(usuDTO.getApellido());
		estudiante.setDocumento(Integer.parseInt(usuDTO.getDocumento()));
		estudiante.setFechaNacimiento(usuDTO.getFechaNacimiento());
		estudiante.setDireccion(usuDTO.getDireccion());
		estudiante.setNombreUsuario(usuDTO.getNombreUsuario());
		estudiante.setContrasenia(usuDTO.getContrasenia());
		estudiante.setTipoUsuario(usuDTO.getTipoUsuario());
		estudiante.setVerificacion(usuDTO.getVerificacion());
		estudiante.setEstadoUsuario(usuDTO.getEstadoUsuario());
		estudiante.setMail(usuDTO.getMail());
		estudiante.setTelefono(usuDTO.getTelefono());
		estudiante.setItr(usuDTO.getItr());
		
		estudiante.setCarreraEspecialidad(estDTO.getCarreraEspecialidad());
		estudiante.setIdEstudiantil(Integer.parseInt(estDTO.getIdEstudiantil()));

		return estudiante;
	}
}
