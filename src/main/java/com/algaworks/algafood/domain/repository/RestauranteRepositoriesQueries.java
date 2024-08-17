package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;

import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;


/**
 * Interface has benn created to avoid compilations erros at class RestauranteRepository JPA implementation.
 */

public interface RestauranteRepositoriesQueries {

	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

}