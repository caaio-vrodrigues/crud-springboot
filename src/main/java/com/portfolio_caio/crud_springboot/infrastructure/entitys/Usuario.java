package com.portfolio_caio.crud_springboot.infrastructure.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Usuario")
@Entity
public class Usuario {
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO) 
	 private Integer id; 
	 
	 @Column(name = "email", unique = true)
	 private String email;
	 
	 @Column(name = "nome")
	 private String nome;
}
