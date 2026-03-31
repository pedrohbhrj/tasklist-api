package br.com.tasklist.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false,length = 50)
    private String titulo;

    @Column(nullable = false,length = 300)
    private String descricao;

    private boolean estaConcluida;

    @Column(nullable = false)
    private LocalDate dataPrazo;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    private void prePersist(){
        this.estaConcluida = false;
    }
}
