package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoriesQueries;


@Component
public class RestauranteRepositoryImpl implements RestauranteRepositoriesQueries {
	
	
	@PersistenceContext
	private EntityManager entityManager;


	
	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial,BigDecimal taxaFreteFinal ) {
		
		String jpql = 
				String.format("from Restaurante r "
						+ "WHERE r.nome "
						+ "LIKE %s "
						+ "and r.taxaFrete "
						+ "BETWEEN %s and %s"
						, ":nome", ":taxaInicial", ":taxaFinal");

		TypedQuery<Restaurante> createQuery = 
		entityManager.createQuery(jpql, Restaurante.class);
		
		createQuery.setParameter("nome", "%" + nome + "%")
		.setParameter("taxaInicial", taxaFreteInicial)
		.setParameter("taxaFinal", taxaFreteFinal);

		return createQuery.getResultList();

	}
	
//	public Restaurante buscar(Long id) {
//
//		return entityManager.find(Restaurante.class, id);
//
//	}
	
//	@Transactional
//	@Override
//	public Restaurante salvar(Restaurante restaurante) {
//
//		return entityManager.merge(restaurante);
//	}
//	
//	@Transactional
//	@Override
//	public void remover(Long id) {
//
//		Restaurante restaurante = buscar(id);
//		
//		if(restaurante == null) {
//			
//			throw new EmptyResultDataAccessException(1);
//		}
//
//		entityManager.remove(buscar(id));
//
//	}

	

}
