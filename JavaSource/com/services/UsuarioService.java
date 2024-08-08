package com.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.dao.AnalistaDAO;
import com.dao.UsuarioDAO;
import com.dto.UsuarioDTO;
import com.entities.Analista;
import com.entities.Usuario;
import com.exceptions.ServiciosException;

import lombok.Data;
import java.io.Serializable;


//Este es un Service
@Data
@Stateless
@LocalBean
public class UsuarioService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private UsuarioDAO usuarioDAO;
	
	public Usuario getByMail(String mail) {
		return usuarioDAO.getByMail(mail);
		
	}
	
	public void changePassword(long id, String pass) throws ServiciosException{
		usuarioDAO.changePassword(id, pass);
	}
}
