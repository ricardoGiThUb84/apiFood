//package com.algaworks.algafood.infrastructure.repository;
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.TypedQuery;
//import javax.transaction.Transactional;
//
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.stereotype.Component;
//
//import com.algaworks.algafood.domain.model.Cozinha;
//import com.algaworks.algafood.domain.repository.CozinhaRepository;
//
//
//@Component
//public class CozinhaRepositoryImpl implements CozinhaRepository{
//	
//	
//	@PersistenceContext
//	private EntityManager entityManager;
//
//	public Cozinha buscar(Long id) {
//
//		return entityManager.find(Cozinha.class, id);
//
//	}
//	
//
//	@Transactional
//	@Override
//	public Cozinha salvar(Cozinha cozinha) {
//
//		return entityManager.merge(cozinha);
//	}
//
//	@Override
//	public List<Cozinha> listar() {
//
//		TypedQuery<Cozinha> createQuery = entityManager.createQuery("from Cozinha", Cozinha.class);
//
//		return createQuery.getResultList();
//
//	}
//	
//	@Transactional
//	@Override
//	public void remover(Long id) {
//		
//		Cozinha cozinha = buscar(id);
//		
//		if(cozinha == null) {
//			
//			throw new EmptyResultDataAccessException(1);
//		}
//
//		entityManager.remove(cozinha);
//
//	}
//
//	
//
//}
