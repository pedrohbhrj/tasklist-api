package br.com.tasklist.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Tarefa> tarefas = new ArrayList<>();

    @Column(length = 150)
    private String nome;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private String cpf;

    @Column(length = 300)
    private String imgUrlPerfil;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime atualizadoEm;

}
