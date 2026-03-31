package br.com.tasklist.exception;

public record FieldDTOError(String campo,
                            String mensagem) {
}
