package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.client.GestionCarreraEspecialidad;
import com.client.GestionItr;
import com.entities.CarreraEspecialidad;
import com.entities.Itr;
import com.enums.EstadoCarreraEspecialidad;
import com.enums.EstadoItr;
import com.exceptions.ServiciosException;

@Stateless
public class CarreraEspecialidadDAO {
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private GestionCarreraEspecialidad gestionCarreraEspecialidad;
	/**
	 * Default constructor.
	 */
	public CarreraEspecialidadDAO() {
		// TODO Auto-generated constructor stub

	}
	public CarreraEspecialidad crear(CarreraEspecialidad carEsp) throws ServiciosException {
		try {
			em.persist(carEsp);
			em.flush();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito!",
					"La carrera se registro exitosamente.");

			gestionCarreraEspecialidad.infoMessage(message);
			
			return carEsp;
		} catch (PersistenceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"La carrera no se pudo registrar.");
			
			gestionCarreraEspecialidad.infoMessage(message);
			
			throw new ServiciosException("No se pudo crear la Carrera/Especialidad");
		}

	}

	public void modificar(CarreraEspecialidad carEsp) throws ServiciosException {
		try {
			em.merge(carEsp);
			em.flush();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Carrera modificada con éxito",
					"");
			
			gestionCarreraEspecialidad.infoMessage(message);
			
		} catch (PersistenceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"No se pudo modificar la carrera.");
			
			gestionCarreraEspecialidad.infoMessage(message);
			throw new ServiciosException("No se pudo modificar la Carrera/Especialidad");
		}

	}

	
	public void borrarLogicamente(long id) {
		try {
			CarreraEspecialidad carrera = em.find(CarreraEspecialidad.class, id);
			carrera.setEstadoCarreraEspecialidad(EstadoCarreraEspecialidad.DESACTIVADO);
			em.merge(carrera);
			em.flush();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Carrera desactivada con éxito",
					"La baja logica fue aplicada correctamente.");

			gestionCarreraEspecialidad.infoMessage(message);
		}catch (PersistenceException e) {
			e.printStackTrace();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR al desactivar la carrera",
					"La baja logica baja logica no se pudo aplicar, asegúrate de que no existan errores en la operación.");

			gestionCarreraEspecialidad.infoMessage(message);
		}
	}
	
	public void borrar(Long id){
		try {
			CarreraEspecialidad carEsp = em.find(CarreraEspecialidad.class, id);
			em.remove(carEsp);
			em.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}
	
	public List<CarreraEspecialidad> obtenerTodos() {
		TypedQuery<CarreraEspecialidad> query = em.createQuery("SELECT u FROM CarreraEspecialidad u ORDER BY u.idCarreraEspecialidad ASC",
				CarreraEspecialidad.class);
		return query.getResultList();
	}
	public CarreraEspecialidad obtenerPorNombre(String nom) {
		TypedQuery<CarreraEspecialidad> query = em.createQuery("SELECT i FROM CarreraEspecialidad i WHERE i.nombre = :nom", CarreraEspecialidad.class)
				.setParameter("nom", nom);
		return query.getSingleResult();
	}
	
	public CarreraEspecialidad buscarCarrera(Long id) {
		CarreraEspecialidad carrera = em.find(CarreraEspecialidad.class, id);
		return carrera;
	}
}
