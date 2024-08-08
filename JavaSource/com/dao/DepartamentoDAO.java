package com.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.entities.Departamento;

@Stateless
public class DepartamentoDAO {
	@PersistenceContext
	private EntityManager em;

	public DepartamentoDAO() {
		// TODO Auto-generated constructor stub

	}
	
	public List<Departamento> obtenerTodos() {
		TypedQuery<Departamento> query = em.createQuery("SELECT u FROM Departamento u ORDER BY u.idDepartamento ASC",
				Departamento.class);
		return query.getResultList();
	}
	
	public Departamento obtenerPorNombre(String nom) {
		TypedQuery<Departamento> query = em.createQuery("SELECT i FROM Departamento i WHERE i.nombre = :nom", Departamento.class)
				.setParameter("nom", nom);
		return query.getSingleResult();
	}
}
