package com.helpdesk.exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ApiError {

    private final LocalDateTime timeStamp;
    private final int status; // Ex: 404
    private final String error; // Ex: "Not Found"
    private final String mensagem; // Ex: "O produto não foi encontrado."
    private final String caminho; // Ex: "/api/produtos/1"

    public ApiError(LocalDateTime timeStamp, int status, String error, String mensagem, String caminho) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.mensagem = mensagem;
        this.caminho = caminho;
    }
}
