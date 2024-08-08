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

import com.dao.DepartamentoDAO;
import com.dto.ItrDTO;
import com.entities.Departamento;
import com.entities.Itr;
import com.enums.EstadoItr;
import com.services.ItrService;
import com.utils.ValidationError;

import lombok.Data;

@Named(value = "gestionItr")
@SessionScoped
@Data
public class GestionItr implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 2498755981981779919L;
	
	@Inject
	private ItrService itrService;
	
	@EJB
	private DepartamentoDAO departamentoDAO;
	
	private ValidationError validationError = new ValidationError();
	
	private Itr itrSelecLista = new Itr();
	private List<Itr> listaItrsReales;
	
	private List<String> listaNombreDepartamentos;
	
	private ItrDTO itrSeleccionado = new ItrDTO();
	private String nombreDepartamentoSeleccionado;
	private String estadoItrSeleccionado;
	
	public void LimpiarCampos() {
		itrSelecLista = new Itr();
		itrSeleccionado = new ItrDTO();
		nombreDepartamentoSeleccionado = new String();
		estadoItrSeleccionado = new String();

	}
	
	public void infoMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public String cargarMenuListaItr () {
		listaItrsReales = itrService.obtenerTodosItr();
		return "/analista/mainContent_Lista_De_Itr.xhtml?faces-redirect=true";
	}
	
	public boolean globalFilterFunctionItr(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isBlank(filterText)) {
			return true;
		}

		Itr itr = (Itr) value;
		return (itr.getIdItr() + "").toLowerCase().contains(filterText)
				|| itr.getNombre().toLowerCase().contains(filterText)
				|| itr.getDepartamento().getNombre().toLowerCase().contains(filterText)
				|| itr.getEstadoItr().getNombre().toLowerCase().contains(filterText);
	}
	
	public List<String> cargarListaNombreDepartamentos() {
		listaNombreDepartamentos = new ArrayList<>();
		for (Departamento depto : departamentoDAO.obtenerTodos()) {
			listaNombreDepartamentos.add(depto.getNombre());
		}
		return listaNombreDepartamentos;
	}
	
	public void seleccionarItr(long id) {
		itrSeleccionado = new ItrDTO();
		itrSelecLista = itrService.buscarItrPorId(id);
		
		itrSeleccionado.setNombre(itrSelecLista.getNombre());
		nombreDepartamentoSeleccionado = itrSelecLista.getDepartamento().getNombre();
		estadoItrSeleccionado = itrSelecLista.getEstadoItr().getNombre();
	}
	
	public void registrarItr() {
		
		ItrDTO itr = new ItrDTO();
		
		itr.setNombre(itrSeleccionado.getNombre());
		
		if(nombreDepartamentoSeleccionado != null) {
			itr.setDepartamento(departamentoDAO.obtenerPorNombre(nombreDepartamentoSeleccionado));
		}
		
		if(EstadoItr.ACTIVADO.getNombre().equals(estadoItrSeleccionado)) {
			itr.setEstadoItr(EstadoItr.ACTIVADO);
		}else if(EstadoItr.DESACTIVADO.getNombre().equals(estadoItrSeleccionado)){
			itr.setEstadoItr(EstadoItr.DESACTIVADO);
		}else {
			itr.setEstadoItr(null);
		}
		
		if(validationError.validarErroresItr(itr)) {
			FacesContext context = FacesContext.getCurrentInstance();
			itrService.registrarItr(itr);
			cargarMenuListaItr();
			LimpiarCampos();
			
			context.getPartialViewContext().getEvalScripts().add("PF('dialogRegistrarItr').hide()");
		}
		
	}
	
	public void modificarItr() {
		
		ItrDTO itr = new ItrDTO();
		
		itr.setIdItr(itrSelecLista.getIdItr());
		itr.setNombre(itrSeleccionado.getNombre());
		
		if(nombreDepartamentoSeleccionado != null) {
			itr.setDepartamento(departamentoDAO.obtenerPorNombre(nombreDepartamentoSeleccionado));
		}
		
		if(EstadoItr.ACTIVADO.getNombre().equals(estadoItrSeleccionado)) {
			itr.setEstadoItr(EstadoItr.ACTIVADO);
		}else if(EstadoItr.DESACTIVADO.getNombre().equals(estadoItrSeleccionado)){
			itr.setEstadoItr(EstadoItr.DESACTIVADO);
		}else {
			itr.setEstadoItr(null);
		}
		
		if(validationError.validarErroresItr(itr)) {
			FacesContext context = FacesContext.getCurrentInstance();
			itrService.modificarItr(itr);
			
			itrSelecLista = new Itr();
			itrSeleccionado = new ItrDTO();
			nombreDepartamentoSeleccionado = " ";
			estadoItrSeleccionado = " ";
			cargarMenuListaItr();
			context.getPartialViewContext().getEvalScripts().add("PF('dialogModificarItr').hide()");
		}
		
	}
	
	public void eliminarItrLogicamente(long id) {
		itrService.borrarItrLogicamente(id);
		cargarMenuListaItr();
	}

}
