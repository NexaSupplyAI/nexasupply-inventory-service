package com.nexasupplyai.inventory.exception;

import com.nexasupplyai.inventory.config.CorrelationIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({AlmacenNoEncontradoException.class, CategoriaNoEncontradaException.class,
            ProductoNoEncontradoException.class, SaldoInventarioNoEncontradoException.class,
            ReservaInventarioNoEncontradaException.class, MovimientoInventarioNoEncontradoException.class})
    ResponseEntity<ApiError> notFound(RuntimeException exception, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiError> badRequest(IllegalArgumentException exception, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> fields = exception.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(
                field -> field.getField(),
                field -> field.getDefaultMessage() == null ? "Valor inválido" : field.getDefaultMessage(),
                (first, ignored) -> first));
        return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "La solicitud contiene campos inválidos",
                request, fields);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ApiError> conflict(DataIntegrityViolationException exception, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "DATA_CONFLICT", "La operación infringe una restricción de datos",
                request, Map.of());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> unexpected(Exception exception, HttpServletRequest request) {
        LOGGER.error("Unhandled request failure", exception);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Ocurrió un error interno",
                request, Map.of());
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String code, String message,
                                           HttpServletRequest request, Map<String, String> fields) {
        ApiError body = new ApiError(status.value(), code, message, request.getRequestURI(),
                MDC.get(CorrelationIdFilter.MDC_KEY), OffsetDateTime.now(ZoneOffset.UTC), fields);
        return ResponseEntity.status(status).body(body);
    }
}
