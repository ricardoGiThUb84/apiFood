package com.algaworks.algafood.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Problem {

	private Integer status;
	private String type;
	private String title;
	private String detail;
	
//	private LocalDateTime dataHora;
//
//	private String mensagem;

}
