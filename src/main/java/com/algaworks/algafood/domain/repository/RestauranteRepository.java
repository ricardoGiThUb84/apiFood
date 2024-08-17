package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository 
extends JpaRepository<Restaurante, Long>, RestauranteRepositoriesQueries {
	
	@Query("FROM Restaurante r JOIN r.cozinha LEFT JOIN FETCH r.formasPagamento")
	List<Restaurante> findAll();
	
//	@Query("from Restaurante where nome LIKE %:nome% and cozinha.id = :id")
	List<Restaurante> buscarPorNome(@Param(value = "nome") String nome, @Param(value = "id") Long cozinhaId);
	
//	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial,BigDecimal taxaFinal);
	
	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial,BigDecimal taxaFreteFinal);
	
	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinhaId);
}
