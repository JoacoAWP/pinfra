package com.entities;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Estudiante")
@PrimaryKeyJoinColumn(referencedColumnName="IDUSUARIO")
public class Estudiante extends Usuario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5385063536424646834L;

	@ManyToOne
	@JoinColumn(name = "IDCARRERAESPECIALIDAD")
	private CarreraEspecialidad carreraEspecialidad;
	
	private int idEstudiantil;

}
