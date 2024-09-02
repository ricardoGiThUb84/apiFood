package com.algaworks.algafood.core.validation;

import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {

    private String valorField;
    private String descricaoField;
    private String descricaoObrigatoria;
    
    @Override
    public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
        this.descricaoField = constraintAnnotation.descricaoField();
        this.valorField = constraintAnnotation.valorField();
        this.descricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();

    }

    @Override
    public boolean isValid(Object objInstanciado, ConstraintValidatorContext context) {

        boolean valido = false;

        try {
            BigDecimal valor = (BigDecimal) BeanUtils.getPropertyDescriptor(objInstanciado.getClass(), valorField)
                    .getReadMethod().invoke(objInstanciado);

            String descricao = (String) BeanUtils.getPropertyDescriptor(objInstanciado.getClass(), descricaoField)
                    .getReadMethod().invoke(objInstanciado);

            if(valor != null &&  BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null){
                valido = descricao.toLowerCase().contains(descricaoField);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return valido;
    }
}
