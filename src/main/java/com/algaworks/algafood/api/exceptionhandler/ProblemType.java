package com.algaworks.algafood.api.exceptionhandler;


import lombok.Getter;

@Getter
public enum ProblemType {

    ENTIDADE_NAO_ENCONTRADA("/entidade-não-encontrada","Entidade não encontrada"),
    ENTIDADE_NEGOCIO("/entidade-negócio","Entidade negócio"),
    ENTIDADE_EM_USO ("/entidade-em-uso","Entidade em uso");

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.title = title;
        this.uri = "https://www.algafood.com.br" + path;
    }



}
