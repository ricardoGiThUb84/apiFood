package com.algaworks.algafood.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class MultiploValidator implements ConstraintValidator<Multiplo, Number> {

    private int numeroMultiplo;

    @Override
    public void initialize(Multiplo constraintAnnotation) {
       this.numeroMultiplo = constraintAnnotation.numero();
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        boolean valido = true;

        if(value != null ){

            BigDecimal valorPassado = BigDecimal.valueOf(value.doubleValue());
            BigDecimal multiplo = valorPassado.remainder(BigDecimal.valueOf(numeroMultiplo));

            valido = BigDecimal.ZERO.compareTo(multiplo) == 0;
        }

        return valido;
    }
}
