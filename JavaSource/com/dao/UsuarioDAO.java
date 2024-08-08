package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.client.GestionUsuario;
import com.entities.Usuario;
import com.enums.EstadoUsuario;
import com.exceptions.ServiciosException;

@Stateless
public class UsuarioDAO {
	@PersistenceContext
	private EntityManager em;

	@Inject
	private GestionUsuario gestionUsuario;

	/**
	 * Default constructor.
	 */
	public UsuarioDAO() {
		// TODO Auto-generated constructor stub

	}

	public Usuario crear(Usuario usu) throws ServiciosException {
		try {
			em.persist(usu);
			em.flush();
			return usu;
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo crear el Usuario");
		}

	}

	public void modificar(Usuario usu) throws ServiciosException {
		try {
			em.merge(usu);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo modificar el usuario");
		}

	}

	public void borrarLogicamente(long id) {
		try {
			Usuario usu = em.find(Usuario.class, id);
			usu.setEstadoUsuario(EstadoUsuario.INACTIVO);
			em.merge(usu);
			em.flush();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario borrado con éxito",
					"La baja logica fue aplicada correctamente.");

			gestionUsuario.infoMessage(message);
		} catch (PersistenceException e) {
			e.printStackTrace();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR al borrar usuario",
					"La baja logica baja logica no se pudo aplicar, asegúrate de que no existan errores en la operación.");

			gestionUsuario.infoMessage(message);
		}
	}

	public void borrar(Long id) {
		try {
			Usuario usuario = em.find(Usuario.class, id);
			em.remove(usuario);
			em.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	public List<Usuario> obtenerTodos() {
		TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u ORDER BY u.idUsuario ASC", Usuario.class);
		return query.getResultList();
	}

	public Usuario buscarUsuario(Long id) {
		Usuario usu = em.find(Usuario.class, id);
		return usu;
	}

	public List<Usuario> obtenerPorFiltro(long idUsuario, String nombreUsuario) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Usuario> configConsulta = cb.createQuery(Usuario.class);

		Root<Usuario> raizUsuario = configConsulta.from(Usuario.class);

		configConsulta.select(raizUsuario);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (idUsuario != 0) {
			predicates.add(cb.equal(raizUsuario.get("idUsuario"), idUsuario));
		}

		if (nombreUsuario != null) {
			predicates.add(cb.equal(raizUsuario.get("nombreUsuario"), nombreUsuario));
		}

		configConsulta.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

		return em.createQuery(configConsulta).getResultList();
	}

	public Usuario getByMail(String mail) {
		try {
			TypedQuery<Usuario> query = em
					.createQuery("SELECT u FROM Usuario u WHERE u.mail = :mail", Usuario.class)
					.setParameter("mail", mail);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void changePassword(long id, String pass) throws ServiciosException {
		try {
			Usuario t = em.find(Usuario.class, id);
			t.setContrasenia(pass);
			em.merge(t);
			em.flush();
		} catch (PersistenceException e) {
			throw new ServiciosException("No se pudo modificar la contraseña");
		}
	}

}
