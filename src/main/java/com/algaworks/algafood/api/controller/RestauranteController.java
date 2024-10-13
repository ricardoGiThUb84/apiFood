package com.algaworks.algafood.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.algaworks.algafood.Groups;
import com.algaworks.algafood.domain.exception.ValidacaoException;
import com.algaworks.algafood.model.CozinhaModel;
import com.algaworks.algafood.model.RestauranteModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private SmartValidator smartValidator;

    @GetMapping("/por-nome-frete")
    public List<Restaurante> listarPorFrete(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

        return restauranteRepository.find(nome, taxaFreteInicial, taxaFreteFinal);
    }

    @GetMapping
    public List<RestauranteModel> listar() {

        return tocollectionModel(restauranteRepository.findAll());
    }

    @GetMapping("/porNome")
    public List<Restaurante> buscarPorNome(@RequestParam(defaultValue = "nome") String nome,
                                           @RequestParam(defaultValue = "id") Long id) {
        return restauranteRepository.buscarPorNome(nome, id);
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<RestauranteModel> buscar(@PathVariable Long restauranteId) {

        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
        RestauranteModel restauranteModel = toModel(restaurante);

        return ResponseEntity.ok(restauranteModel);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public RestauranteModel salvar(
            @RequestBody
            @Valid
            Restaurante restaurante) {

        try {
            return toModel(cadastroRestauranteService.salvar(restaurante));
        } catch (CozinhaNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }

    }

    @PutMapping("/{restauranteId}")
    public Restaurante atualizar(@PathVariable Long restauranteId,
                                 @RequestBody @Valid Restaurante restaurante) {

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
                                              @RequestBody Map<String, Object> restaurante,
                                              HttpServletRequest httpServletRequest) {
        try {
            Restaurante restauranteAtual = restauranteRepository.findById(restauranteId).orElse(null);

            if (restauranteAtual == null) {

                return ResponseEntity.notFound().build();
            }

            merge(restaurante, restauranteAtual, httpServletRequest);
            valida(restauranteAtual, "restaurante");

            return ResponseEntity.ok(cadastroRestauranteService.salvar(restauranteAtual));

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void valida(Restaurante restaurante, String objectName) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
        smartValidator.validate(restaurante, bindingResult);

        if (bindingResult.hasErrors()) {

            throw new ValidacaoException(bindingResult);
        }
    }

    private void merge(Map<String, Object> restaurante,
                       Restaurante restauranteAtual,
                       HttpServletRequest httpServletRequest) {
        // TODO Auto-generated method stub

        ServletServerHttpRequest servletServerHttpRequest =
                new ServletServerHttpRequest(httpServletRequest);

        try {

            // transformar o mapa em uma instancia de restaurante para pegar as propriedades
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

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

        } catch (IllegalArgumentException ex) {

            Throwable rootCause = ExceptionUtils.getRootCause(ex);

            throw new HttpMessageNotReadableException(
                    ex.getMessage(), rootCause, servletServerHttpRequest);

        }
    }


    private static RestauranteModel toModel(Restaurante restaurante) {
        CozinhaModel cozinhaModel = new CozinhaModel();
        cozinhaModel.setId(restaurante.getCozinha().getId());
        cozinhaModel.setNome(restaurante.getCozinha().getNome());

        RestauranteModel restauranteModel = new RestauranteModel();
        restauranteModel.setId(restaurante.getId());
        restauranteModel.setNome(restaurante.getNome());
        restauranteModel.setTaxaFrete(restaurante.getTaxaFrete());
        restauranteModel.setCozinha(cozinhaModel);
        return restauranteModel;
    }

    private List<RestauranteModel> tocollectionModel(List<Restaurante> listaRestaurante) {
        return listaRestaurante.stream()
                .map(RestauranteController::toModel)
                .collect(Collectors.toList());
    }
}
