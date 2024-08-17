package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;

	@GetMapping("/por-nome-frete")
	public List<Restaurante> listarPorFrete(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

		return restauranteRepository.find(nome, taxaFreteInicial, taxaFreteFinal);
	}

	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}

	@GetMapping("/porNome")
	public List<Restaurante> buscarPorNome(@RequestParam(defaultValue = "nome") String nome,
			@RequestParam(defaultValue = "id") Long id) {

		return restauranteRepository.buscarPorNome(nome, id);

	}

	@GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
		Optional<Restaurante> restaurante = restauranteRepository.findById(restauranteId);

		if (restaurante.isPresent()) {
			return ResponseEntity.ok(restaurante.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public Restaurante salvar(@RequestBody Restaurante restaurante) {

		 try {
		        return cadastroRestauranteService.salvar(restaurante);
		    } catch (CozinhaNaoEncontradaException  e) {
		        throw new NegocioException(e.getMessage());
		    }

	}

	@PutMapping("/{restauranteId}")
	public Restaurante atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {

		Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);
	    
	    BeanUtils.copyProperties(restaurante, restauranteAtual, 
	            "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
	    
	    try {
	        return cadastroRestauranteService.salvar(restauranteAtual);
	    } catch (CozinhaNaoEncontradaException e) {
	        throw new NegocioException(e.getMessage());
	    }
	}

	@PatchMapping("/{restauranteId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> restaurante) {
		try {
			Restaurante restauranteAtual = restauranteRepository.findById(restauranteId).orElse(null);

			if (restauranteAtual == null) {

				return ResponseEntity.notFound().build();
			}

			merge(restaurante, restauranteAtual);

			return ResponseEntity.ok(cadastroRestauranteService.salvar(restauranteAtual));

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	private void merge(Map<String, Object> restaurante, Restaurante restauranteAtual) {
		// TODO Auto-generated method stub

		// transformar o mapa em uma instancia de restaurante para pegar as propriedades
		ObjectMapper mapper = new ObjectMapper();
		Restaurante restauranteConvertido = mapper.convertValue(restaurante, Restaurante.class);

		restaurante.forEach((chave, valor) -> {

			// mapear os atributos da classe Restauranteque existem no map
			Field field = ReflectionUtils.findField(Restaurante.class, chave);
			field.setAccessible(true);

			// usamos o mapeamento da classe pra pegar o atributo da classe convertida pelo
			// object mapper
			Object fieldMapeado = ReflectionUtils.getField(field, restauranteConvertido);

			// Setamos as propriedades do objeto mapeado no encontrado no banco
			ReflectionUtils.setField(field, restauranteAtual, fieldMapeado);

		});

	}
}
