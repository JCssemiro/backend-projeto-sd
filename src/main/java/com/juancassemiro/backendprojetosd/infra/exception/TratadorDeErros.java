package com.juancassemiro.backendprojetosd.infra.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CustomException> tratarErroNaoEncontrado(Exception ex){
        CustomException e = new CustomException(ex.getMessage(),"404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> tratarErroBadRequest(MethodArgumentNotValidException ex){
        var Errors = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(Errors.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CustomException> tratarErroNaoEncontradoURI(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomException("Não foi possível processar sua solicitação, URL Inválida","404"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomException> tratarErroBadRequestLeitura(Exception ex){
        return ResponseEntity.badRequest().body(new CustomException("Erro no Body da requisição: "+ex.getMessage(),"400"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomException> tratarErroRuntime(Exception ex){
        return ResponseEntity.internalServerError().body(new CustomException(ex.getMessage(),"500"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CustomException> tratarErroMetodoNaoSuportado(){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new CustomException("Método de requisição inválido","405"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomException> tratarErroDeRestricaoDetabela(DataIntegrityViolationException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomException(e.getMostSpecificCause().getMessage(),"409"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomException> tratarErroDeCredenciaisInvalidas(BadCredentialsException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomException("Usuário ou senha inválidos!","403"));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<CustomException> tratarErroDeBloqueioDeUsuario(LockedException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomException(e.getMessage(),"403"));
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<CustomException> tratarErroJpa(JpaSystemException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomException("Você violou alguma restrição do banco de dados","409"));
    }

    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    public record CustomException(String message, String status) {
    }

}