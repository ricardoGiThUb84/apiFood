package com.algaworks.algafood.api.exceptionhandler;


import lombok.Getter;

@Getter
public enum ProblemType {

    DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel","Mensagem incompreensivel"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
    ENTIDADE_EM_USO ("/entidade-em-uso","Entidade em uso");

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.title = title;
        this.uri = "https://www.algafood.com.br" + path;
    }



}
