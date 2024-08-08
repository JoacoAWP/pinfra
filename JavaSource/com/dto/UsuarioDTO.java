package com.dto;

import java.util.Date;

import com.entities.Itr;
import com.enums.EstadoUsuario;
import com.enums.TipoUsuario;
import com.enums.Verificacion;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UsuarioDTO {
	private long idUsuario;
	
	private String nombre;
	
	private String apellido;
	
	private String documento;
	
	private Date fechaNacimiento;
	
	private String direccion;
	
	private String nombreUsuario;
	
	private String contrasenia;
	
	private TipoUsuario tipoUsuario;
	
	private Verificacion verificacion;
	
	private EstadoUsuario estadoUsuario;
	
	private String mail;
	
	private String telefono;
	
	private Itr itr;
}
