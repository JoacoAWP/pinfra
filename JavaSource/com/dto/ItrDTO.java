package com.dto;

import com.entities.Departamento;
import com.enums.EstadoItr;

import lombok.Data;

@Data
public class ItrDTO {
	private long idItr;
	
	private String nombre;
	
	private Departamento departamento;
	
	private EstadoItr estadoItr;
}
