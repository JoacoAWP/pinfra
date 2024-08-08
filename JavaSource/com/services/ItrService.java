package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.AnalistaDAO;
import com.dao.DepartamentoDAO;
import com.dao.ItrDAO;
import com.dto.ItrDTO;
import com.dto.UsuarioDTO;
import com.entities.Analista;
import com.entities.Itr;

import lombok.Data;
import java.io.Serializable;


//Este es un Service
@Data
@Stateless
@LocalBean
public class ItrService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private ItrDAO itrDAO;
	@EJB
	private DepartamentoDAO departamentoDAO;
	
	public String registrarItr(ItrDTO DTO) {
		try {
			Itr itrNuevo = dtoToItr(DTO);
			itrDAO.crear(itrNuevo);
			return "";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return "";
	}
	public String modificarItr(ItrDTO DTO) {
		try {
			Itr itr = dtoToItr(DTO);
			itrDAO.modificar(itr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return "";
	}
	public String borrarItrLogicamente(long id) {
		itrDAO.borrarLogicamente(id);
		return "";
	}
	public List<Itr> obtenerTodosItr(){
		return itrDAO.obtenerTodos();
	}
	public Itr buscarItrPorId (long id) {
		return itrDAO.buscarItr(id);
	}
	
	public Itr dtoToItr (ItrDTO DTO) {
		Itr itr = new Itr();
		itr.setIdItr(DTO.getIdItr());
		itr.setNombre(DTO.getNombre());
		itr.setDepartamento(DTO.getDepartamento());
		itr.setEstadoItr(DTO.getEstadoItr());

		return itr;
	}
	
	
}
