package com.edu.tcc.carbon.carbon.handlers;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.edu.tcc.carbon.carbon.exceptions.FuelNotFoundException;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Tratamento de erros 500 - erros inesperados na aplicação
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Erro interno do servidor");
        body.put("message", "Ocorreu um erro inesperado. Tente novamente mais tarde.");

        // Log interno opcional (sem vazar para o cliente)
        ex.printStackTrace(); // ou use um logger se preferir

        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Tratamento de erros 400 - dados inválidos na requisição
    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class,
        FuelNotFoundException.class
    })
    public ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Requisição inválida");
        body.put("message", gerarMensagemAmigavel(ex));

        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // Gera mensagens mais amigáveis dependendo do tipo de erro
    private String gerarMensagemAmigavel(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            return "Um ou mais campos estão inválidos ou em formato incorreto.";
        } else if (ex instanceof HttpMessageNotReadableException) {
            return "O corpo da requisição está malformado ou com dados incompatíveis.";
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            return "Um parâmetro da requisição está com tipo inválido.";
        } else if (ex instanceof MissingServletRequestParameterException) {
            return "Parâmetro obrigatório ausente na requisição.";
        } else if (ex instanceof FuelNotFoundException) {
            return "Tipo de Combustível inválido.";
        }
        else {
            return "Erro de validação na requisição.";
        }
    }
}
