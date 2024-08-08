package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.CarreraEspecialidadDAO;
import com.dao.DepartamentoDAO;
import com.dao.ItrDAO;
import com.dto.CarreraEspecialidadDTO;
import com.dto.ItrDTO;
import com.entities.CarreraEspecialidad;
import com.entities.Itr;

import lombok.Data;

@Data
@Stateless
@LocalBean
public class CarreraEspecialidadService {
	
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CarreraEspecialidadDAO carreraDAO;
	
	@EJB
	private ItrDAO itrDAO;
	
	public String registrarCarrera(CarreraEspecialidadDTO DTO) {
		try {
			CarreraEspecialidad carreraNueva = dtoToCarreraEspecialidad(DTO);
			carreraDAO.crear(carreraNueva);
			return "";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return "";
	}
	
	public String modificarCarrera(CarreraEspecialidadDTO DTO) {
		try {
			CarreraEspecialidad carrera = dtoToCarreraEspecialidad(DTO);
			carreraDAO.modificar(carrera);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return "";
	}
	public String borrarCarreraLogicamente(long id) {
		carreraDAO.borrarLogicamente(id);
		return "";
	}
	public List<CarreraEspecialidad> obtenerTodosCarreras(){
		return carreraDAO.obtenerTodos();
	}
	public CarreraEspecialidad buscarCarreraPorId (long id) {
		return carreraDAO.buscarCarrera(id);
	}
	
	public CarreraEspecialidad dtoToCarreraEspecialidad (CarreraEspecialidadDTO DTO) {
		CarreraEspecialidad carrera = new CarreraEspecialidad();
		carrera.setIdCarreraEspecialidad(DTO.getIdCarreraEspecialidad());
		carrera.setNombre(DTO.getNombre());
		carrera.setItr(DTO.getItr());
		carrera.setEstadoCarreraEspecialidad(DTO.getEstadoCarreraEspecialidad());

		return carrera;
	}
}
