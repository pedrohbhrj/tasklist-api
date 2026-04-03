package br.com.tasklist;

import br.com.tasklist.entity.Tarefa;
import br.com.tasklist.entity.Usuario;
import br.com.tasklist.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (usuarioRepository.count() > 0) return;

        Usuario usuario1 = Usuario.builder()
                .nome("Pedro Henrique")
                .email("pedro@gmail.com.br")
                .cpf("111.222.333-44")
                .build();

        Tarefa tarefa1 = Tarefa.builder()
                .titulo("Estudar matérias da escola")
                .descricao("Matemática.")
                .dataPrazo(LocalDate.now().minusDays(5))
                .usuario(usuario1)
                .build();

        Tarefa tarefa2 = Tarefa.builder()
                .titulo("Malhar.")
                .descricao("Malhar costas e biceps.")
                .dataPrazo(LocalDate.now())
                .usuario(usuario1)
                .build();

        Tarefa tarefa3 = Tarefa.builder()
                .titulo("Comprar moveis para o quarto.")
                .descricao("Comprar com o menor preço possivel.")
                .dataPrazo(LocalDate.now().plusDays(10))
                .usuario(usuario1)
                .build();

        usuario1.setTarefas(List.of(tarefa1, tarefa2, tarefa3));


        Usuario usuario2 = Usuario.builder()
                .nome("Maria Silva")
                .email("maria@gmail.com.br")
                .cpf("555.666.777-88")
                .build();

        Tarefa tarefa4 = Tarefa.builder()
                .titulo("Fazer compras")
                .descricao("Comprar itens para o café da manhã.")
                .dataPrazo(LocalDate.now().minusDays(2))
                .usuario(usuario2)
                .build();

        usuario2.setTarefas(List.of(tarefa4));
        usuarioRepository.saveAll(List.of(usuario1, usuario2));

    }
}