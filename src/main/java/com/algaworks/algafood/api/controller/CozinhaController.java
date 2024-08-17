package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceContext;

import org.apache.catalina.connector.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import com.google.protobuf.Option;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;

	@GetMapping
	public List<Cozinha> listar() {

		return cozinhaRepository.findAll();
	}

	@GetMapping("/porNome")
	public List<Cozinha> buscarPorNome(@RequestParam(defaultValue = "nome") String nome,
			@RequestParam(defaultValue = "id") Long id) {

		return cozinhaRepository.buscarPorNome(nome, id);

	}

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> listar(@PathVariable("cozinhaId") Long cozinhaId) {

		Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);

		return ResponseEntity.status(HttpStatus.OK).body(cozinha);
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {

		try {

			return cadastroCozinhaService.salvar(cozinha);

		} catch (EntidadeNaoEncontradaException e) {
			// TODO: handle exception

			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{cozinhaId}")
	public Cozinha atualizar(@PathVariable("cozinhaId") Long cozinhaId, @RequestBody Cozinha cozinha) {

		Cozinha cozinhaBanco = cadastroCozinhaService.buscarOuFalhar(cozinhaId);

		try {

			BeanUtils.copyProperties(cozinha, cozinhaBanco, "id");
			return cadastroCozinhaService.salvar(cozinhaBanco);

		} catch (NegocioException e) {
			// TODO: handle exception

			throw new NegocioException(e.getMessage());
		}

	}

//	@DeleteMapping("/{cozinhaId}")
//	public ResponseEntity remover(@PathVariable("cozinhaId") Long cozinhaId) {
//		
//	
//		try {
//			
//			cadastroCozinhaService.excluir(cozinhaId);
//			return ResponseEntity.noContent().build();
//			
//		}catch(EntidadeNaoEncontradaException e){
//			
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não encontrado");		
//		
//			
//		} catch (EntidadeEmUsoException e) {
//			// TODO: handle exception
//			
//			return ResponseEntity.status(HttpStatus.CONFLICT).build();
//		}
//		
//	}

	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@DeleteMapping("/{cozinhaId}")
	public void remover(@PathVariable("cozinhaId") Long cozinhaId) {
		cadastroCozinhaService.excluir(cozinhaId);
	}
}
