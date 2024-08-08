package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.client.GestionUsuario;
import com.entities.Analista;
import com.exceptions.ServiciosException;

@Stateless
public class AnalistaDAO {
	@PersistenceContext
	private EntityManager em;

	@Inject
	private GestionUsuario usuarioService;

	/**
	 * Default constructor.
	 */
	public AnalistaDAO() {
		// TODO Auto-generated constructor stub

	}

	public Analista crear(Analista ana) throws ServiciosException {
		try {
			em.persist(ana);
			em.flush();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro de analista correcto",
					"El analista se registro correctamente, este usuario debe ser verificado para poder logearse.");

			usuarioService.infoMessage(message);

			return ana;
		} catch (PersistenceException e) {
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de registro",
					"El analista no se pudo registrar, Asegúrate de que los datos ingresados son correctos o inténtalo más tarde.");

			usuarioService.infoMessage(message);
			
			throw new ServiciosException("No se pudo crear el Analista");
		
		}

	}

	public void modificar(Analista ana) throws ServiciosException {
		try {
			em.merge(ana);
			em.flush();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario analista modificado",
					"El usuario fue modificado correctamente.");

			usuarioService.infoMessage(message);
			
		} catch (PersistenceException e) {
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR en la modificación",
					"Asegúrate de que los datos ingresados del analista son correctos.");

			usuarioService.infoMessage(message);
			
			throw new ServiciosException("No se pudo modificar el Analista");
		}

	}

	public void borrar(Long id) {
		try {
			Analista ana = em.find(Analista.class, id);
			em.remove(ana);
			em.flush();
			
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			
			
		}
	}

	public List<Analista> obtenerTodos() {
		TypedQuery<Analista> query = em.createQuery("SELECT u FROM Analista u ORDER BY u.idUsuario ASC",
				Analista.class);
		return query.getResultList();
	}

	public Analista buscarAnalista(Long id) {
		Analista ana = em.find(Analista.class, id);
		return ana;
	}

}
