package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.client.GestionItr;
import com.client.GestionUsuario;
import com.entities.Itr;
import com.entities.Usuario;
import com.enums.EstadoItr;
import com.enums.EstadoUsuario;
import com.exceptions.ServiciosException;

@Stateless
public class ItrDAO {
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private GestionItr gestionItr;

	/**
	 * Default constructor.
	 */
	public ItrDAO() {
		// TODO Auto-generated constructor stub

	}

	public Itr crear(Itr i) throws ServiciosException {
		try {
			em.persist(i);
			em.flush();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito!",
					"El itr se registro exitosamente.");

			gestionItr.infoMessage(message);
			return i;
		} catch (PersistenceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"El itr no se pudo registrar.");

			gestionItr.infoMessage(message);
			throw new ServiciosException("No se pudo crear el Itr");
		}

	}

	public void modificar(Itr i) throws ServiciosException {
		try {
			em.merge(i);
			em.flush();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Itr modificado con éxito",
					"");

			gestionItr.infoMessage(message);
		} catch (PersistenceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"El itr no se pudo modificar.");

			gestionItr.infoMessage(message);
			throw new ServiciosException("No se pudo modificar el Itr");
		}

	}

	public void borrarLogicamente(long id) {
		try {
			Itr itr = em.find(Itr.class, id);
			itr.setEstadoItr(EstadoItr.DESACTIVADO);
			em.merge(itr);
			em.flush();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Itr desactivado con éxito",
					"La baja logica fue aplicada correctamente.");

			gestionItr.infoMessage(message);
		}catch (PersistenceException e) {
			e.printStackTrace();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR al desactivar itr",
					"La baja logica baja logica no se pudo aplicar, asegúrate de que no existan errores en la operación.");

			gestionItr.infoMessage(message);
		}
	}
	
	public List<Itr> obtenerTodos() {
		TypedQuery<Itr> query = em.createQuery("SELECT u FROM Itr u ORDER BY u.idItr ASC",
				Itr.class);
		return query.getResultList();
	}
	
	public Itr obtenerPorNombre(String nom) {
		TypedQuery<Itr> query = em.createQuery("SELECT i FROM Itr i WHERE i.nombre = :nom", Itr.class)
				.setParameter("nom", nom);
		return query.getSingleResult();
	}
	public Itr buscarItr(Long id) {
		Itr itr = em.find(Itr.class, id);
		return itr;
	}
}
