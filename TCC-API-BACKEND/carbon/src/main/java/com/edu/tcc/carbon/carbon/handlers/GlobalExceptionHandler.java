package com.edu.tcc.carbon.carbon.handlers;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.edu.tcc.carbon.carbon.exceptions.FuelNotFoundException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

/**
 * GlobalExceptionHandler aprimorado.
 * - @Order(Ordered.HIGHEST_PRECEDENCE) garante prioridade sobre resolvers padrão.
 * - Trata MethodArgumentNotValidException, BindException, HttpMessageNotReadableException e afins.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @PostConstruct
    public void init() {
        log.info("GlobalExceptionHandler registrado e pronto — pacote: {}", this.getClass().getPackageName());
    }
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String extractPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            HttpServletRequest servletReq = servletWebRequest.getRequest();
            // prefer requestURI (sem contexto) — mais estável
            return servletReq.getRequestURI();
        }
        return null;
    }

    private String extractTraceId(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            HttpServletRequest servletReq = servletWebRequest.getRequest();
            String header = servletReq.getHeader("X-Request-Id");
            if (header == null) header = servletReq.getHeader("traceId");
            return header;
        }
        return null;
    }

    @ExceptionHandler(FuelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFuelNotFound(FuelNotFoundException ex, WebRequest request) {
        ErrorResponse body = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Invalid fuel",
            ex.getMessage(),
            extractPath(request),
            extractTraceId(request),
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
}

    // -------------------------------------------------------
    // 1) Validação de request body (@Valid) e bind (form/query)
    // -------------------------------------------------------
    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<Map<String, Object>> fieldErrors = new ArrayList<>();

        if (ex instanceof MethodArgumentNotValidException manv) {
            fieldErrors = manv.getBindingResult().getFieldErrors().stream()
                    .map(fe -> Map.of(
                            "field", fe.getField(),
                            "rejectedValue", fe.getRejectedValue(),
                            "message", fe.getDefaultMessage()))
                    .collect(Collectors.toList());
        } else if (ex instanceof BindException be) {
            fieldErrors = be.getBindingResult().getFieldErrors().stream()
                    .map(fe -> Map.of(
                            "field", fe.getField(),
                            "rejectedValue", fe.getRejectedValue(),
                            "message", fe.getDefaultMessage()))
                    .collect(Collectors.toList());
        }

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Validation Failed",
                "Um ou mais campos estão inválidos",
                extractPath(request),
                extractTraceId(request),
                new ArrayList<>(fieldErrors)
        );

        log.warn("Validation failed: {}", fieldErrors);
        return ResponseEntity.status(status).body(body);
    }
    // -------------------------------------------------------
    // 2) JSON malformado / desserialização (campo ausente com @JsonProperty(required=true) ou tipo errado)
    // -------------------------------------------------------
@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String message = "Requisição inválida";

    Throwable cause = ex.getMostSpecificCause();
    if (cause instanceof MismatchedInputException mie && mie.getPath() != null && !mie.getPath().isEmpty()) {
        String field = mie.getPath().get(0).getFieldName();
        message = "O campo '" + field + "' é obrigatório e não pode estar ausente ou nulo";
    }

    ErrorResponse body = new ErrorResponse(
        LocalDateTime.now(),
        status.value(),
        "Validation Failed",
        message,
        extractPath(request),
        extractTraceId(request),
        null
    );

    log.warn("Malformed or missing JSON field: {}", message);
    return ResponseEntity.status(status).body(body);
}

    // -------------------------------------------------------
    // 3) parametro com tipo incorreto (path/query)
    // -------------------------------------------------------
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = String.format("Parâmetro '%s' inválido: valor='%s' esperado=%s",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "tipo");

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Invalid parameter",
                msg,
                extractPath(request),
                extractTraceId(request),
                null
        );

        log.warn("Type mismatch: {}", msg);
        return ResponseEntity.status(status).body(body);
    }

    // -------------------------------------------------------
    // 4) parâmetro obrigatório ausente (query/form/header)
    // -------------------------------------------------------
    @ExceptionHandler({ MissingServletRequestParameterException.class, ServletRequestBindingException.class })
    public ResponseEntity<ErrorResponse> handleMissingParam(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = ex instanceof MissingServletRequestParameterException m ? "Parâmetro obrigatório ausente: " + m.getParameterName()
                : "Parâmetro obrigatório ausente ou binding falhou: " + ex.getMessage();

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Missing parameter",
                msg,
                extractPath(request),
                extractTraceId(request),
                null
        );

        log.warn("Missing param/binding failed: {}", msg);
        return ResponseEntity.status(status).body(body);
    }

    // -------------------------------------------------------
    // 5) ConstraintViolations (ex.: @Validated em parâmetros)
    // -------------------------------------------------------
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<String> errors = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.toList());

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Constraint Violation",
                "Parâmetros inválidos",
                extractPath(request),
                extractTraceId(request),
                new ArrayList<>(errors)
        );

        log.warn("Constraint violations: {}", errors);
        return ResponseEntity.status(status).body(body);
    }

    // -------------------------------------------------------
    // 7) Downstream services (RestTemplate/WebClient)
    // -------------------------------------------------------
    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ErrorResponse> handleDownstreamError(RestClientResponseException ex, WebRequest request) {
        int status = ex.getRawStatusCode();

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status,
                "Downstream service error",
                "Erro ao chamar serviço externo",
                extractPath(request),
                extractTraceId(request),
                List.of(Map.of("downstreamBody", ex.getResponseBodyAsString()))
        );

        log.warn("Downstream error status={}, body={}", status, ex.getResponseBodyAsString());
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleResourceAccess(ResourceAccessException ex, WebRequest request) {
        HttpStatus status = HttpStatus.GATEWAY_TIMEOUT;

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Resource access error",
                "Não foi possível acessar o serviço remoto (timeout / falha de rede)",
                extractPath(request),
                extractTraceId(request),
                List.of(Optional.ofNullable(ex.getMessage()).orElse("resource access"))
        );

        log.warn("Resource access exception", ex);
        return ResponseEntity.status(status).body(body);
    }

    // -------------------------------------------------------
    // 8) Access denied
    // -------------------------------------------------------
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Access denied",
                "Você não tem permissão para executar essa operação",
                extractPath(request),
                extractTraceId(request),
                null
        );

        log.warn("Access denied: {}", extractPath(request));
        return ResponseEntity.status(status).body(body);
    }

    // -------------------------------------------------------
    // 9) Método não permitido
    // -------------------------------------------------------
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Method not allowed",
                "Método HTTP não suportado: " + ex.getMethod(),
                extractPath(request),
                extractTraceId(request),
                List.of(ex.getSupportedHttpMethods())
        );

        log.warn("Method not allowed: {}", ex.getMethod());
        return ResponseEntity.status(status).body(body);
    }

    // -------------------------------------------------------
    // 10) Fallback genérico (500)
    // -------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "Internal Server Error",
                "Ocorreu um erro interno. Tente novamente mais tarde.",
                extractPath(request),
                extractTraceId(request),
                List.of(Optional.ofNullable(ex.getMessage()).orElse("error"))
        );

        log.error("Unhandled exception on path {}: {}", extractPath(request), ex.getMessage(), ex);
        return ResponseEntity.status(status).body(body);
    }
}
