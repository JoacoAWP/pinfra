package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.AnalistaDAO;
import com.dto.UsuarioDTO;
import com.entities.Analista;

import lombok.Data;
import java.io.Serializable;


//Este es un Service
@Data
@Stateless
@LocalBean
public class AnalistaService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private AnalistaDAO analistaDAO;
	
	public String registrarAnalista(UsuarioDTO usuDTO) {
		try {
			Analista analistaNuevo = dtoToAnalista(usuDTO);
			analistaDAO.crear(analistaNuevo);
//			// mensaje de actualizacion correcta
//			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
//					"Se ha registrado el usuario: " + usuarioSeleccionado.getNombreUsuario() + " correctamente", "");
//			FacesContext.getCurrentInstance().addMessage(null, facesMsg);

//			usuarioSeleccionado = new UsuarioDTO();
			return "";

		} catch (Exception e) {

//			Throwable rootException = ExceptionsTools.getCause(e);
//			String msg1 = e.getMessage();
//			String msg2 = ExceptionsTools.formatedMsg(rootException);
//
//			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg1, msg2);
//			FacesContext.getCurrentInstance().addMessage(null, facesMsg);

			e.printStackTrace();
		} finally {

		}
		return "";
	}
	public String modificarAnalista(UsuarioDTO usuDTO) {
		try {
			Analista analista = dtoToAnalista(usuDTO);
			analistaDAO.modificar(analista);
		} catch (Exception e) {

//			Throwable rootException = ExceptionsTools.getCause(e);
//			String msg1 = e.getMessage();
//			String msg2 = ExceptionsTools.formatedMsg(rootException);
//
//			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg1, msg2);
//			FacesContext.getCurrentInstance().addMessage(null, facesMsg);

			e.printStackTrace();
		} finally {

		}
		return "";
	}
	public List<Analista> obtenerTodosAnalista (){
		return analistaDAO.obtenerTodos();
	}
	public Analista buscarAnalistaPorId (long id) {
		return analistaDAO.buscarAnalista(id);
	}
	
	public Analista dtoToAnalista (UsuarioDTO usuDTO) {
		Analista analista = new Analista();
		analista.setIdUsuario(usuDTO.getIdUsuario());
		analista.setNombre(usuDTO.getNombre());
		analista.setApellido(usuDTO.getApellido());
		analista.setDocumento(Integer.parseInt(usuDTO.getDocumento()));
		analista.setFechaNacimiento(usuDTO.getFechaNacimiento());
		analista.setDireccion(usuDTO.getDireccion());
		analista.setNombreUsuario(usuDTO.getNombreUsuario());
		analista.setContrasenia(usuDTO.getContrasenia());
		analista.setTipoUsuario(usuDTO.getTipoUsuario());
		analista.setVerificacion(usuDTO.getVerificacion());
		analista.setEstadoUsuario(usuDTO.getEstadoUsuario());
		analista.setMail(usuDTO.getMail());
		analista.setTelefono(usuDTO.getTelefono());
		analista.setItr(usuDTO.getItr());

		return analista;
	}
	
	
}
