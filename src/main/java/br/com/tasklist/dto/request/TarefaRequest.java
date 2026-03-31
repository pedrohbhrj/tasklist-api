package br.com.tasklist.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TarefaRequest(@NotNull(message = "É obrigatório.") @Size(max = 50,message = "Deve ter no maximo cinquenta caracteres") String titulo,
                            @NotNull(message = "É obrigatório.") @Size(max = 300,message = "Deve ter no maximo cinquenta caracteres") String descricao,
                            boolean estaConcluida,
                            @NotNull(message = "É obrigatório.") @Future(message = "Data prazo deve ser no futuro.") LocalDate dataPrazo
                            ) {
}
