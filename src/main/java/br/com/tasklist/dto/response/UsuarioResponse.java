package br.com.tasklist.dto.response;

import java.time.LocalDateTime;

public record UsuarioResponse(Long id,
                              String nome,
                              String email,
                              String cpf,
                              LocalDateTime criadoEm,
                              String imgUrlPerfil) {
}
