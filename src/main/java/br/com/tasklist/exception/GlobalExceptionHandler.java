package br.com.tasklist.exception;

import br.com.tasklist.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> notFoundHandler(NotFoundException ex){
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<FieldDTOError>>> validationHandler(MethodArgumentNotValidException ex){
        List<FieldDTOError> list = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new FieldDTOError(err.getField(),err.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<>("Campos inválidos.",
                        HttpStatus.BAD_REQUEST.value(), list));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> alreadyExistsException(AlreadyExistsException ex){
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value(), null));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> exceptionHandler(Exception ex){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("Erro interno do servidor.",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
    }
}
