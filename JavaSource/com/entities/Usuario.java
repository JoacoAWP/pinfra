package com.entities;

import java.util.Date;

import javax.persistence.*;

import com.enums.EstadoUsuario;
import com.enums.TipoUsuario;
import com.enums.Verificacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="Usuario")
public class Usuario {
	
	@Id
	private long idUsuario;
	
	private String nombre;
	
	private String apellido;
	
	private int documento;
	
	private Date fechaNacimiento;
	
	private String direccion;
	
	private String nombreUsuario;
	
	private String contrasenia;
	
	@Column(name = "TIPOUSUARIO")
	@Enumerated(EnumType.STRING)
	private TipoUsuario tipoUsuario;
	
	@Column(name = "VERIFICACION")
	@Enumerated(EnumType.STRING)
	private Verificacion verificacion;
	
	@Column(name = "ESTADO")
	@Enumerated(EnumType.STRING)
	private EstadoUsuario estadoUsuario;
	//cambiar en todos los registros y agregar esto del estado
	
	private String mail;
	
	private String telefono;
	
	@ManyToOne
	@JoinColumn(name = "ITR_IDITR")
	private Itr itr;
}