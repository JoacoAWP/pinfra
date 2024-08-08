package com.client;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.util.LangUtils;


import com.dao.ItrDAO;
import com.dto.CarreraEspecialidadDTO;
import com.entities.CarreraEspecialidad;
import com.entities.Itr;
import com.enums.EstadoCarreraEspecialidad;
import com.services.CarreraEspecialidadService;
import com.utils.ValidationError;

import lombok.Data;



@Named(value = "gestionCarreraEspecialidad")
@SessionScoped
@Data
public class GestionCarreraEspecialidad implements Serializable{
	
	private static final long serialVersionUID = 2498755981981779919L;
	
	@Inject
	private CarreraEspecialidadService carreraEspecialidadService;
	
	@EJB
	private ItrDAO itrDAO;
	
	private ValidationError validationError = new ValidationError();
	
	private CarreraEspecialidad carreraEspecialidadSelecLista;
	private List<CarreraEspecialidad> listaCarrerasReales;
	
	private List<String> listaNombreItrs;
	
	private CarreraEspecialidadDTO carreraEspecialidadSeleccionada;
	private String nombreItrSeleccionado;
	private String estadoCarreraEspecialidadSeleccionado;
	
	public void LimpiarCampos() {
		carreraEspecialidadSelecLista = new CarreraEspecialidad();
		carreraEspecialidadSeleccionada = new CarreraEspecialidadDTO();
		nombreItrSeleccionado = new String();
		estadoCarreraEspecialidadSeleccionado = new String();

	}
	
	public void infoMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public boolean globalFilterFunctionCarreraEspecialidad(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isBlank(filterText)) {
			return true;
		}

		CarreraEspecialidad carrera = (CarreraEspecialidad) value;
		return (carrera.getIdCarreraEspecialidad() + "").toLowerCase().contains(filterText)
				|| carrera.getNombre().toLowerCase().contains(filterText)
				|| carrera.getItr().getNombre().toLowerCase().contains(filterText)
				|| carrera.getItr().getDepartamento().getNombre().toLowerCase().contains(filterText)
				|| carrera.getEstadoCarreraEspecialidad().getNombre().toLowerCase().contains(filterText);
	}
	
	public void registrarCarrera() {
		CarreraEspecialidadDTO carrera = new CarreraEspecialidadDTO();
		
		carrera.setNombre(carreraEspecialidadSeleccionada.getNombre());
		
		if(nombreItrSeleccionado != null) {
			carrera.setItr(itrDAO.obtenerPorNombre(nombreItrSeleccionado));
		}
		
		
		if(EstadoCarreraEspecialidad.ACTIVADO.getNombre().equals(estadoCarreraEspecialidadSeleccionado)) {
			carrera.setEstadoCarreraEspecialidad(EstadoCarreraEspecialidad.ACTIVADO);
		}else if(EstadoCarreraEspecialidad.DESACTIVADO.getNombre().equals(estadoCarreraEspecialidadSeleccionado)){
			carrera.setEstadoCarreraEspecialidad(EstadoCarreraEspecialidad.DESACTIVADO);
		}else {
			carrera.setEstadoCarreraEspecialidad(null);
		}
		
		
		if(validationError.validarErroresCarrera(carrera)) {
			FacesContext context = FacesContext.getCurrentInstance();
			carreraEspecialidadService.registrarCarrera(carrera);
			cargarMenuListaCarrera();
			LimpiarCampos();
			context.getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarCarrera').hide()");
		}
		
	}
	
	public void seleccionarCarrera(long id) {
		carreraEspecialidadSeleccionada = new CarreraEspecialidadDTO();
		carreraEspecialidadSelecLista = carreraEspecialidadService.buscarCarreraPorId(id);
		
		carreraEspecialidadSeleccionada.setNombre(carreraEspecialidadSelecLista.getNombre());
		nombreItrSeleccionado = carreraEspecialidadSelecLista.getItr().getNombre();
		estadoCarreraEspecialidadSeleccionado = carreraEspecialidadSelecLista.getEstadoCarreraEspecialidad().getNombre();
	}
	
	public String cargarMenuListaCarrera () {
		listaCarrerasReales = carreraEspecialidadService.obtenerTodosCarreras();
		return "/analista/mainContent_Lista_De_Carreras.xhtml?faces-redirect=true";
	}
	
	public List<String> cargarListaNombreItrs() {
		listaNombreItrs = new ArrayList<>();
		for (Itr itr : itrDAO.obtenerTodos()) {
			if("Activado".equals(itr.getEstadoItr().getNombre())) {

			listaNombreItrs.add(itr.getNombre());
			}
		}
		return listaNombreItrs;
	}
	
	
	
	
	public void modificarCarrera() {
		
		CarreraEspecialidadDTO carrera = new CarreraEspecialidadDTO();
		
		carrera.setIdCarreraEspecialidad(carreraEspecialidadSelecLista.getIdCarreraEspecialidad());
		carrera.setNombre(carreraEspecialidadSeleccionada.getNombre());
		carrera.setItr(itrDAO.obtenerPorNombre(nombreItrSeleccionado));
		
		if(EstadoCarreraEspecialidad.ACTIVADO.getNombre().equals(estadoCarreraEspecialidadSeleccionado)) {
			carrera.setEstadoCarreraEspecialidad(EstadoCarreraEspecialidad.ACTIVADO);
		}else {
			carrera.setEstadoCarreraEspecialidad(EstadoCarreraEspecialidad.DESACTIVADO);
		}
		
		
		if(validationError.validarErroresCarrera(carrera)) {
			FacesContext context = FacesContext.getCurrentInstance();
			carreraEspecialidadService.modificarCarrera(carrera);
			
			carreraEspecialidadSelecLista = new CarreraEspecialidad();
			carreraEspecialidadSeleccionada = new CarreraEspecialidadDTO();
			nombreItrSeleccionado = " ";
			estadoCarreraEspecialidadSeleccionado = " ";
			
			cargarMenuListaCarrera();
			
			
			context.getPartialViewContext().getEvalScripts().add("PF('dialogModificarCarrera').hide()");
			
		}
		
		
		
		
	}
	
	public void eliminarCarreraLogicamente(long id) {
		carreraEspecialidadService.borrarCarreraLogicamente(id);
		cargarMenuListaCarrera();
	}
}
