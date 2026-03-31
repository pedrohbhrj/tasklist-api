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

        if (usuarioRepository.count() > 0) {
            System.out.println("Banco de dados já contém registros. Pulando a inicialização.");
            return;
        }

        System.out.println("Iniciando a carga de dados de teste...");

        // ==========================================
        // CRIANDO USUÁRIO 1
        // ==========================================
        Usuario usuario1 = Usuario.builder()
                .nome("Pedro Henrique")
                .email("pedro@estudos.com.br")
                .cpf("111.222.333-44")
                .build();

        Tarefa tarefa1 = Tarefa.builder()
                .titulo("Consertar POM do Maven")
                .descricao("Ajustar dependências do MapStruct e codificação UTF-8.")
                .dataPrazo(LocalDate.now().minusDays(5))
                .usuario(usuario1)
                .build();

        Tarefa tarefa2 = Tarefa.builder()
                .titulo("Estudar Spring Data JPA")
                .descricao("Aprender sobre anotações e relacionamentos.")
                .dataPrazo(LocalDate.now())
                .usuario(usuario1)
                .build();

        Tarefa tarefa3 = Tarefa.builder()
                .titulo("Criar testes automatizados")
                .descricao("Implementar JUnit com Mockito.")
                .dataPrazo(LocalDate.now().plusDays(10))
                .usuario(usuario1)
                .build();

        usuario1.setTarefas(List.of(tarefa1, tarefa2, tarefa3));


        Usuario usuario2 = Usuario.builder()
                .nome("Maria Silva")
                .email("maria@estudos.com.br")
                .cpf("555.666.777-88")
                .build();

        Tarefa tarefa4 = Tarefa.builder()
                .titulo("Fazer compras")
                .descricao("Comprar itens para o café da manhã.")
                .dataPrazo(LocalDate.now().minusDays(2)) // VENCIDA (2 dias atrás)
                .usuario(usuario2)
                .build();

        usuario2.setTarefas(List.of(tarefa4));

        // ==========================================
        // SALVANDO NO BANCO DE DADOS
        // ==========================================
        // O saveAll do repositório de usuário também fará o insert das tarefas
        // graças ao `cascade = CascadeType.ALL` que você colocou na entidade Usuario.
        usuarioRepository.saveAll(List.of(usuario1, usuario2));

        System.out.println("Carga de dados finalizada com sucesso!");
    }
}