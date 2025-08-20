package es.dsrroma.school.springboot.integracionbase.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.dsrroma.school.springboot.integracionbase.exceptions.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Método para manejar la excepción de validación.
     * 
     * @param ex causa del error
     * @return bad request con el mensaje de error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        
        Map<String, String> errorMessages = new HashMap<>();
        for (FieldError error : errors) {
            errorMessages.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errorMessages);
    }
    
    /** Método para manejar la excepción de entidad no encontrada.
     * 
     * @param ex causa del error
     * @return not found con el mensaje de error
     */
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
}