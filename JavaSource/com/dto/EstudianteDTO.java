package com.dto;

import com.entities.CarreraEspecialidad;

import lombok.Data;

@Data
public class EstudianteDTO {
	private long idEstudiante;
	
	private CarreraEspecialidad carreraEspecialidad;
	
	private String idEstudiantil;
}
