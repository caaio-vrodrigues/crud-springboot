package com.portfoliocaio.crudspringboot.infrastructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="user_client")
@Entity
public class UserClient{
     @Id
     @GeneratedValue(strategy=GenerationType.AUTO) 
     private Long id; 
     
     @NotBlank
     @Email
     @Column(name="email", unique=true, nullable=false, length=254)
     private String email;
     
     @NotBlank
     @Column(name="password")
     private String password;

     @PrePersist
     @PreUpdate
     private void normalize(){
     	 if(this.email != null) {
     	 	this.email = this.email.toLowerCase(Locale.ROOT);
     	 }
     }
     
     @Version
     private Long version; 
}