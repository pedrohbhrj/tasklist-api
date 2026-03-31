package br.com.tasklist.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TarefaResponse (Long id,
                              String titulo,
                              String descricao,
                              boolean estaConcluida,
                              LocalDate dataPrazo,
                              LocalDateTime criadoEm){
}
