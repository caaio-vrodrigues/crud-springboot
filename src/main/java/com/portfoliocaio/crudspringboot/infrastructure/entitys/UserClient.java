package com.portfoliocaio.crudspringboot.infrastructure.entitys;

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
@Table(name = "user-client")
@Entity
public class UserClient {
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO) 
	 private Integer id; 
	 
	 @Column(name = "email", unique = true)
	 private String email;
	 
	 @Column(name = "password")
	 private String password;
}
