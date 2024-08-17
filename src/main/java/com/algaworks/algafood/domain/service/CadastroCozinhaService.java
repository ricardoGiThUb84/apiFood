package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CadastroCozinhaService {
	
	private static final String MSG_COZINHA_EM_USO = "A cozinha %s está associada a um restaurante.";
	private static final String MSG_COZINHA_NÃO_ENCONTRADA = "A cozinha %s não foi encontrada.";
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	
	public Cozinha buscarOuFalhar(Long cozinhaId) {
		
		  return cozinhaRepository.findById(cozinhaId)
			        .orElseThrow(() -> new CozinhaNaoEncontradaException(cozinhaId));
	}
	
	
	public Cozinha salvar(Cozinha cozinha) {
		
		return cozinhaRepository.save(cozinha);
	}
	
	
   public void excluir(Long cozinhaId) {
	   
	   try {
	        cozinhaRepository.deleteById(cozinhaId);
	        
	    } catch (EmptyResultDataAccessException e) {
	        throw new CozinhaNaoEncontradaException(cozinhaId);
	    
	    } catch (DataIntegrityViolationException e) {
	        throw new EntidadeEmUsoException(
	            String.format(MSG_COZINHA_EM_USO, cozinhaId));
	    }
		
	}
	
	

}
