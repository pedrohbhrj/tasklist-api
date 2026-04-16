package br.com.tasklist.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;


public record UsuarioAttRequest(@Size(max = 150,message = "Deve ter no maximo 150 caracteres.") String nome,
                                @Email(message = "Formato inválido.") String email,
                                @CPF(message = "Formato inválido") String cpf,
                                @Size(max = 300,message = "Deve ter no maximo 300 caracteres.") String imgUrlPerfil) {
}
