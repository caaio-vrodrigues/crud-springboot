package com.portfoliocaio.crudspringboot.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidateTokenResponse(
	boolean valid, String subject, Long exp) implements Serializable 
{
    private static final long serialVersionUID = 1L; 
}