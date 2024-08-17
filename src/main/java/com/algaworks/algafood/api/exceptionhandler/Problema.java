package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Builder
public class Problema {
	
	private LocalDateTime dataHora;
	
	private String mensagem;

}
