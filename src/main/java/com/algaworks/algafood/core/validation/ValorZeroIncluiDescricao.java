package com.algaworks.algafood.core.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {ValorZeroIncluiDescricaoValidator.class})
public @interface ValorZeroIncluiDescricao {

    String message() default "descrição obrigatória inválida";

    String valorField();
    String descricaoField();
    String descricaoObrigatoria();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
