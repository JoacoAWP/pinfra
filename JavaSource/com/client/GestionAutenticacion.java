package com.client;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.dao.UsuarioDAO;
import com.entities.Usuario;
import com.enums.EstadoUsuario;
import com.enums.TipoUsuario;
import com.enums.Verificacion;
import com.services.JwtService;

import lombok.Data;

//Esto es un NamedBean
@Named(value = "gestionAutenticacion")
@SessionScoped
@Data
public class GestionAutenticacion implements Serializable{
private static final long serialVersionUID = 1L;
	private Usuario usuarioSeleccionado;
	
	private String nombreUsuarioLogin;
	private String contraseniaLogin;
	
	private String tipoUsuarioLog;
	
	@EJB
	private UsuarioDAO usuarioDAO;
	
	@Inject
	private JwtService jwtService;
	
	@Inject
	private GestionUsuario gestionUsuario;
	
	@PostConstruct
	public void init() {
		usuarioSeleccionado = new Usuario();
	}
	
    public String login() {
    	List<Usuario> usuarios = usuarioDAO.obtenerPorFiltro(0, nombreUsuarioLogin);
    	if (!usuarios.isEmpty()) {
    		for (Usuario usuario : usuarios) {
        		if (nombreUsuarioLogin.equals(usuario.getNombreUsuario()) && contraseniaLogin.equals(usuario.getContrasenia())) {
        			if(usuario.getVerificacion()==Verificacion.VERIFICADO && usuario.getEstadoUsuario()!= EstadoUsuario.INACTIVO) {
        	        	   // Generar un token JWT con los datos ingresados
        	               String token = jwtService.createJwt(nombreUsuarioLogin);
        	               
        	               Cookie cookie = new Cookie("jwt", token);
        	               cookie.setHttpOnly(true);
        	               cookie.setSecure(true);
        	               cookie.setPath("/");
        	               
        	               cookie.setMaxAge(480*60);
        	               
        	               HttpServletResponse response = (HttpServletResponse) FacesContext
        	               		.getCurrentInstance()
        	               		.getExternalContext()
        	               		.getResponse();
        	               
        	               response.addCookie(cookie);
        	               
        	               gestionUsuario.setUsuarioLogeado(usuario);
        	               gestionUsuario.setIdLogueada(usuario.getIdUsuario());
        	               
        	               System.out.println("¡Los datos son válidos! Acceso concedido.");
        	               System.out.println("Token JWT: " + token);
        	               
        	               if(TipoUsuario.ANALISTA.equals(usuario.getTipoUsuario())) {
								/* usuarioService.setMenuButtonsRender(true); */
        	            	   tipoUsuarioLog = "barraNav_Analistas.xhtml";
        	               }else {
								/* usuarioService.setMenuButtonsRender(false); */
        	            	   tipoUsuarioLog = "barraNav_Estudiantes.xhtml";
        	               }
        	               
        	               return "/general/mainContent_Principal.xhtml?faces-redirect=true";
							/* return "/private/menuPrincipal.xhtml?faces-redirect=true"; */
        	           }else {
        	        	   FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención!", "El usuario fue dado de baja o todavia no esta verificado, en este caso espere a que un Analista lo verifique");
        	        	   gestionUsuario.showMessage(message);
        	        	   return "";
        	           }
        	        } else {
        	        	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error!", " los datos son inválidos. Acceso denegado.");
        	        	gestionUsuario.showMessage(message);
        	        	return "";
        	        }
        	}
		}else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error!", " Los datos son inválidos. Acceso denegado.");
			gestionUsuario.showMessage(message);
        	return "";
		}
    	
		return "";
    	
        
    }
    public String logout() {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        
        cookie.setMaxAge(0);
        
        HttpServletResponse response = (HttpServletResponse) FacesContext
        		.getCurrentInstance()
        		.getExternalContext()
        		.getResponse();
        
        response.addCookie(cookie);
        gestionUsuario.init();
        return "/login.xhtml?faces-redirect=true";
	}
	
}
