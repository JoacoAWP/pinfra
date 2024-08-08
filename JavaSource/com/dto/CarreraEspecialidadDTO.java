package com.dto;


import com.entities.Itr;
import com.enums.EstadoCarreraEspecialidad;


import lombok.Data;

@Data
public class CarreraEspecialidadDTO {
	private long idCarreraEspecialidad;

	private String nombre;

	private Itr itr;
	
	private EstadoCarreraEspecialidad estadoCarreraEspecialidad;
}
