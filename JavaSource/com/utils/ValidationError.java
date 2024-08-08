package com.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.dto.CarreraEspecialidadDTO;
import com.dto.EstudianteDTO;
import com.dto.ItrDTO;
import com.dto.UsuarioDTO;
import com.enums.TipoUsuario;

public class ValidationError {

	private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%^&*()\\-_=+\\[\\]{}|;:'\",<.>/?`~])[A-Za-z\\d!@#\\$%^&*()\\-_=+\\[\\]{}|;:'\",<.>/?`~]{8,}$";
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

	public boolean validarErroresRegistro(UsuarioDTO usu, EstudianteDTO est) {
		LocalDate fechaActual = LocalDate.now();
		
		if (usu.getNombre().isEmpty() || !(usu.getNombre().length() >= 1 && usu.getNombre().length() <= 50)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","El nombre debe contener entre 1 y 50 dígitos."));

			return false;
		}
		if (usu.getApellido().isEmpty() || !(usu.getApellido().length() >= 1 && usu.getApellido().length() <= 50)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","El apellido debe contener entre 1 y 50 dígitos."));

			return false;
		}
		if (usu.getDocumento().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","El documento no puede estar vacío."));

			return false;
		}
		if (usu.getDocumento().length() >= 9) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","El documento no puede tener mas de 8 caracteres."));

			return false;
		}
		if (!verificarCI(usu.getDocumento())) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","El documento no es válido."));

			return false;
		}
		if ((usu.getFechaNacimiento() == null)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","La fecha de nacimiento no puede estar vacía."));

			return false;
		}
		if(usu.getFechaNacimiento() != null) {
			LocalDate fechaNacimiento = usu.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			Period periodo = Period.between(fechaNacimiento, fechaActual);
			int edad = periodo.getYears();
			
			if (!fechaActual.isAfter(fechaNacimiento)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error!","La fecha de nacimiento no puede ser mayor a la fecha actual."));

				return false;
			}
			if (edad < 18) {
		        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
		                "Error!","Segun la fecha de nacimiento registrada, no eres mayor a 18 años."));

		        return false;
		    }
		}
		
		if ((!usu.getMail().contains("@") && !usu.getMail().endsWith("utec.edu.uy")) || !(usu.getMail().length() >= 1 && usu.getMail().length() <= 50) ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","El mail debe contener un @, el dominio utec.edu.uy y entre 1 y 50 dígitos."));

			return false;
		}
		if (usu.getTelefono().isEmpty() || !(usu.getTelefono().length() == 9)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","El número de teléfono debe contener 9 dígitos."));
			return false;
		}
		if (usu.getTipoUsuario() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","El tipo de usuario no puede estar vacío."));

			return false;
		}
		if (usu.getDireccion().isEmpty() || !(usu.getDireccion().length() >= 1 && usu.getDireccion().length() <= 50)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","La dirección debe contener entre 1 y 50 dígitos."));

			return false;
		}
		if (usu.getItr() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","El Itr no puede estar vacío."));

			return false;
		}
		if (usu.getNombreUsuario().isEmpty()
				|| !(usu.getNombreUsuario().length() >= 1 && usu.getNombreUsuario().length() <= 30)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","El nombre de usuario debe contener entre 1 y 30 dígitos."));

			return false;
		}
		if (!PASSWORD_PATTERN.matcher(usu.getContrasenia()).matches()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","La contraseña debe contener al menos una letra mayúscula, al menos un dígito y al menos un carácter especial (cualquier carácter de puntuación o símbolo). La longitud de la contraseña debe ser de al menos 8 caracteres."));
			return false;
		}
		if (usu.getEstadoUsuario() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","El estado de usuario no puede estar vacío."));

			return false;
		}
		if (usu.getVerificacion() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","La verificacion no puede estar vacío."));

			return false;
		}
		if(TipoUsuario.ESTUDIANTE.equals(usu.getTipoUsuario())) {
			if (est.getCarreraEspecialidad() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","La Carrera/Especialidad no puede estar vacía."));
				return false;
			}
			if (est.getIdEstudiantil().isEmpty() || !(est.getIdEstudiantil().length() >= 1 && est.getIdEstudiantil().length() <= 19)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error!","El id estudiantil debe contener entre 1 y 19 dígitos."));
				return false;
			}
		}
		return true;
	}
	
	private static boolean verificarCI(String ciSeleccionada) {
		String CI = ciSeleccionada;

		if (CI.length() == 7) {
			CI = "0" + CI;
		}

		int ultimoDigito = CI.charAt(CI.length() - 1);
		int digitoVerificar = Character.getNumericValue(ultimoDigito);

		String codigoMultiplicador = "2987634";
		int acumulador = 0;

		for (int i = 0; i < CI.length() - 1; i++) {
			acumulador += Character.getNumericValue(CI.charAt(i))
					* Character.getNumericValue(codigoMultiplicador.charAt(i));
		}

		int digitoVerificador = (10 - (acumulador % 10)) % 10;

		if (digitoVerificar == digitoVerificador) {
			return true;
		} else {
			return false;
		}

	}
	public boolean validarErroresItr(ItrDTO itr) {
		if (itr.getNombre() == null || !(itr.getNombre().length() >= 1 && itr.getNombre().length() <= 30)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","El nombre debe contener entre 1 y 30 dígitos."));
			return false;
		}
		if (itr.getDepartamento() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","El Departamento no puede estar vacío."));
			return false;
		}
		if (itr.getEstadoItr() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","El Estado no puede estar vacío."));
			return false;
		}
		return true;
		
	}
	public boolean validarErroresCarrera(CarreraEspecialidadDTO carrera) {
		if (carrera.getNombre().isEmpty() || !(carrera.getNombre().length() >= 1 && carrera.getNombre().length() <= 50)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error!","El nombre debe contener entre 1 y 50 dígitos."));
			return false;
		}
		if (carrera.getItr() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","El Itr no puede estar vacío."));
			return false;
		}
		if (carrera.getEstadoCarreraEspecialidad() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","El Estado no puede estar vacío."));
			return false;
		}
		return true;
		
	}
	
}
