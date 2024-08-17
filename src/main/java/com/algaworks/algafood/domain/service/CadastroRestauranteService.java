package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;
	
	private static final String MSG_RESTAURANTE_NAO_ENCONTRADO 
    = "Não existe um cadastro de restaurante com código %d";
	
	
	public Restaurante salvar(Restaurante restaurante) {
		
		 Long cozinhaId = restaurante.getCozinha().getId();
		    
		    Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		    
		    restaurante.setCozinha(cozinha);
		    
		    return restauranteRepository.save(restaurante);
		
	}
	
	
   public void excluir(Long id) {
	   
	
		try {
			
		  restauranteRepository.deleteById(id);
		  
		  
		}catch(EmptyResultDataAccessException e){
			
			String mensagem = String.format("A Restaurante %s não foi encontrada.", id);
			throw new EntidadeNaoEncontradaException(mensagem);
			
			
		} catch (DataIntegrityViolationException e) {
			// TODO: handle exception
			
			String mensagem = String.format("A Restaurante %s está associada a um restaurante.", id);
			throw new EntidadeEmUsoException(mensagem);
		}
		
	}
   
   
   public Restaurante buscarOuFalhar(Long restauranteId) {
	   return restauranteRepository.findById(restauranteId)
		        .orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}
	
	

}