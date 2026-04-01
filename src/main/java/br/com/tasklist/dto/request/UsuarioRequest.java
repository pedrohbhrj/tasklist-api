package br.com.tasklist.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;


public record UsuarioRequest(@Size(max = 150,message = "Deve ter no maximo 150 caracteres.") String nome,
                             @Email(message = "Formato inválido.")@NotNull(message = "É obrigatório.") String email,
                             @CPF(message = "Formato inválido")@NotNull(message = "É obrigatório.") String cpf,
                             @Size(max = 300,message = "Deve ter no maximo 300 caracteres.") String imgUrlPerfil) {
}
