package com.algaworks.algafood;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolationException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CadastroCozinhaIT {

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";
    }

    @Test
    void testarCadastroCozinhaSucesso() {
        //cenario
        Cozinha cozinha = new Cozinha();
        cozinha.setNome("Chinesa");
        //acao
        Cozinha salvar = cadastroCozinhaService.salvar(cozinha);
        // validacao
        Assertions.assertThat(salvar).isNotNull();
        Assertions.assertThat(salvar.getId()).isNotNull();
    }

    @Test
    void cadastroCozinhaDeveFalharComDadosErrados() {
        //cenario
        Cozinha cozinha = new Cozinha();
        cozinha.setNome("");
        //acao

        org.junit.jupiter.api.Assertions.assertThrows(ConstraintViolationException.class,
                () -> cadastroCozinhaService.salvar(cozinha));
    }


    @Test
    void deveRetornar200_cadastroCozinha() {
       //when
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    void deveConter4CozinhaQuandoConsultada() {
        //when
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .body("", Matchers.hasSize(4))
                .body("nome", Matchers.hasItems("Indiana", "Tailandesa"))
                .statusCode(200);
    }


}
