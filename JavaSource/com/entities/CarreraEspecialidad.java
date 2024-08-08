package com.entities;

import javax.persistence.*;

import com.enums.EstadoCarreraEspecialidad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarreraEspecialidad {
	
	@Id
	private long idCarreraEspecialidad;
	
	private String nombre;
	
	@ManyToOne
	@JoinColumn(name = "IDITR")
	private Itr itr;
	
	@Column(name = "ESTADOCARRERA")
	private EstadoCarreraEspecialidad estadoCarreraEspecialidad;
}
