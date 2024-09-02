package com.algaworks.algafood.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;


@Data
@AllArgsConstructor
public class ValidacaoException extends RuntimeException{


    private final BindingResult bindingResult;

}
