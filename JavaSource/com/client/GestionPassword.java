package com.client;

import java.util.Random;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.PrimeFaces.Ajax;
import com.entities.Usuario;
import com.exceptions.ServiciosException;
import com.services.UsuarioService;
import com.utils.MailUtil;

import lombok.Data;
import java.io.Serializable;

@Data
@SessionScoped
@Named(value = "gestionPassword")
public class GestionPassword implements Serializable {
	
	@Inject private UsuarioService usuarioService;
	private Usuario usuarioRecuperar;
	private String codigoGenerado;
	private String recuperarPasswordMail;
	private String recuperarPasswordCodigo;
	private String newPassword;
	private boolean recuperarPasswordMailBool = true;
	private boolean recuperarPasswordCodigoBool = false;
	private boolean newPasswordBool = false;
	
	
	
	
	
	
	
	
	
	
	public void recibirMail() {
		if (comprobarMail()) {
			String asunto = "Codigo de recuperación de contraseña";
			String mensaje = "El codigo para recuperar la contraseña es el siguiente: "+generarCodigo();
			try {
				MailUtil.mandarMail(recuperarPasswordMail, mensaje, asunto);
				createInfoNotification("Exito!","Enviamos a tu correo el codigo para continuar.");
				PrimeFaces.current().ajax().update("growl");
	            recuperarPasswordMailBool = false;
				recuperarPasswordCodigoBool = true;
				
				PrimeFaces.current().ajax().update("formRegistro");
			} catch (ServiciosException e) {
				createErrorNotification("Error!",e.getMessage());
				PrimeFaces.current().ajax().update("growl");
			}
		}
	}
	
	public void recibirCodigo() {
		if (recuperarPasswordCodigo.equals(codigoGenerado)) {
			createInfoNotification("Exito!", "Codigo ingresado correcto.");
			PrimeFaces.current().ajax().update("growl");

            recuperarPasswordCodigoBool = false;
			newPasswordBool = true;
			PrimeFaces.current().ajax().update("formRegistro");
		} else {
			createErrorNotification("Error!", "Codigo ingresado incorrecto.");
			PrimeFaces.current().ajax().update("growl");
		}
	}
	
	public void cargarLostPassword() {
		recuperarPasswordMail = "";
		recuperarPasswordCodigo = "";
		newPassword = "";
		recuperarPasswordMailBool = true;
		recuperarPasswordCodigoBool = false;
		newPasswordBool = false;
	}
	
	public boolean comprobarMail(){
		if ((recuperarPasswordMail.contains("@") && recuperarPasswordMail.endsWith("utec.edu.uy"))
                && (recuperarPasswordMail.length() >= 1 && recuperarPasswordMail.length() <= 320)) {
            usuarioRecuperar = usuarioService.getByMail(recuperarPasswordMail);
            return true;
        }
		createErrorNotification("Error!",
                "El correo debe contener un @, el dominio utec.edu.uy y entre 1 y 320 dígitos.");
		PrimeFaces.current().ajax().update("growl");
		return false;
	}
	
	public String generarCodigo() {
		int longitudCodigo = 6; // Longitud deseada del código
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Caracteres permitidos en el código
        Random random = new Random();
        StringBuilder codigoAleatorio = new StringBuilder();

        for (int i = 0; i < longitudCodigo; i++) {
            int indice = random.nextInt(caracteres.length());
            char caracterAleatorio = caracteres.charAt(indice);
            codigoAleatorio.append(caracterAleatorio);
        }
        
        codigoGenerado = codigoAleatorio.toString();
        return codigoAleatorio.toString();
	}
	
	public String cambiarContraseña() {
		try {
			usuarioService.changePassword(usuarioRecuperar.getIdUsuario(), newPassword);
			createInfoNotification("Exito!", "Contraseña cambiada correctamente");
			PrimeFaces.current().ajax().update("growl");
			return "login";
		} catch (ServiciosException e) {
			createInfoNotification("Error al cambiar la contraseña", e.getMessage());
		}
		return "";
	}
	
	private static void createNotification(Severity severity, String title, String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, message));
    }
	
	public static void createInfoNotification(String title, String message) {
        createNotification(FacesMessage.SEVERITY_INFO, title, message);
    }

    public static void createWarnNotification(String title, String message) {
        createNotification(FacesMessage.SEVERITY_WARN, title, message);
    }

    public static void createErrorNotification(String title, String message) {
        createNotification(FacesMessage.SEVERITY_ERROR, title, message);
    }
}
