package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Cozinha;


@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long>{
	
	
	@Query("from Cozinha c where c.nome LIKE %:nome% and c.id = :id")
	List<Cozinha> buscarPorNome(@Param(value = "nome") String nome, @Param(value = "id") Long cozinhaId);

	List<Cozinha> findTodasByNome(String nome);

	Optional<Cozinha> findByNome(String nome);
	
	boolean existsByNome(String nome);

}
