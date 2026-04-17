package br.com.tasklist;


import br.com.tasklist.dto.request.TarefaRequest;
import br.com.tasklist.dto.response.ApiResponse;
import br.com.tasklist.dto.response.TarefaResponse;
import br.com.tasklist.entity.Tarefa;
import br.com.tasklist.entity.Usuario;
import br.com.tasklist.exception.NotFoundException;
import br.com.tasklist.mapper.TarefaMapper;
import br.com.tasklist.repository.TarefaRepository;
import br.com.tasklist.repository.UsuarioRepository;
import br.com.tasklist.service.TarefaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @Spy
    private TarefaMapper tarefaMapper = Mappers.getMapper(TarefaMapper.class);

    @Captor
    private ArgumentCaptor<Tarefa> tarefaCaptor;

    @Test
    public void deveRetornarTarefaCriada(){

        Long usuarioId = 1L;

        Optional<Usuario> usuario =  Optional.of(Usuario.builder().id(1L).build());

        TarefaRequest tarefaRequest = new TarefaRequest(
                "estudar","estudando",
                false,
                LocalDate.now().plusDays(2)
        );

        Tarefa tarefaCriada = Tarefa.builder()
                .titulo("estudar")
                .descricao("estudando")
                .estaConcluida(false)
                .dataPrazo(LocalDate.now().plusDays(2))
                .id(1L)
                .atualizadoEm(LocalDateTime.now())
                .criadoEm(LocalDateTime.now())
                .build();



        when(usuarioRepository.findById(usuarioId)).thenReturn(usuario);
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaCriada);

        ApiResponse<TarefaResponse> apiResponse = tarefaService.criar(usuarioId,tarefaRequest);


        assertEquals("Tarefa criada com sucesso.",apiResponse.message());
        assertEquals(HttpStatus.CREATED.value(),apiResponse.status());

        assertEquals("estudar",apiResponse.data().titulo());
        assertEquals("estudando",apiResponse.data().descricao());
        assertEquals(LocalDate.now().plusDays(2),apiResponse.data().dataPrazo());
        assertFalse(apiResponse.data().estaConcluida());


        verify(usuarioRepository,times(1)).findById(usuarioId);
        verify(tarefaRepository,times(1)).save(any(Tarefa.class));
    }

    @Test
    public void deveLancarExcecaoQuandoNaoEncontrarUsuarioCriandoNovaTarefa(){
       Long usuarioId = 99L;

       when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,() -> {
            tarefaService.criar(usuarioId,new TarefaRequest("xxx","ccc",false,LocalDate.now().plusDays(2)));
        });


        assertNotNull(ex);
        assertEquals("Usuario não encontrado.",ex.getMessage());
    }


    @Test
    public void deveDeletarTarefa(){
        Long tarefaId = 1L;
        Optional<Tarefa> tarefaEncontrada = Optional.of(Tarefa.builder()
                        .titulo("xxx")
                        .id(tarefaId)
                        .descricao("vvv")
                        .estaConcluida(false)
                        .dataPrazo(LocalDate.now().plusDays(2))
                .build());

        when(tarefaRepository.findById(tarefaId)).thenReturn(tarefaEncontrada);
        doNothing().when(tarefaRepository).delete(tarefaEncontrada.get());

        ApiResponse<Void> apiResponse = tarefaService.deletar(tarefaId);



        assertEquals(HttpStatus.OK.value(),apiResponse.status());
        assertEquals("Tarefa deletada com sucesso.",apiResponse.message());
        assertNull(apiResponse.data());

        verify(tarefaRepository,times(1)).findById(tarefaId);
        verify(tarefaRepository,times(1)).delete(tarefaEncontrada.get());

    }

    @Test
    public void deveLancarExcecaoAoNaoEncontrarTarefaAoDeletar(){
        Long tarefaId = 99L;

        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,() -> {
            tarefaService.deletar(tarefaId);
        });


        assertNotNull(ex);
        assertEquals("Tarefa não encontrada.",ex.getMessage());

        verify(tarefaRepository,times(1)).findById(tarefaId);
        verify(tarefaRepository,never()).delete(any(Tarefa.class));
    }

    @Test
    public void deveRetonarTarefaEncontrada(){
        Optional<Tarefa> tarefaEncontrada = Optional.of(Tarefa.builder()
                .id(1L)
                .titulo("xxx")
                .descricao("vvv")
                        .estaConcluida(false)
                        .dataPrazo(LocalDate.now().plusDays(2))
                .build());

        TarefaResponse response = new TarefaResponse(1L,"xxx","vvv",false,LocalDate.now().plusDays(2),LocalDateTime.now());

        when(tarefaRepository.findById(1L)).thenReturn(tarefaEncontrada);

        when(tarefaMapper.toRes(tarefaEncontrada.get())).thenReturn(response);


        ApiResponse<TarefaResponse> apiResponse = tarefaService.encontrar(1L);


        assertEquals("Tarefa encontrada com sucesso.",apiResponse.message());
        assertEquals(HttpStatus.OK.value(),apiResponse.status());
        assertNotNull(apiResponse.data());
        assertEquals(response.id(),apiResponse.data().id());
        assertEquals(response.dataPrazo(),apiResponse.data().dataPrazo());


        verify(tarefaRepository,times(1)).findById(1L);
    }

    @Test
    public void deveRetornarExcecaoQuandoNaoEncontrarTarefa(){
        Long tarefaId = 99L;

        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,() -> {
            tarefaService.encontrar(tarefaId);
        });

        assertNotNull(ex);
        assertEquals("Tarefa não encontrada.",ex.getMessage());
    }


    @Test
    public void deveRetonarListaDeTarefas(){
        List<Tarefa> listaDeTarefas = List.of(
                Tarefa.builder()
                        .dataPrazo(LocalDate.now().plusDays(2))
                        .titulo("xxx")
                        .descricao("vvv")
                        .estaConcluida(false)
                        .id(1L).build(),
                Tarefa.builder()
                        .dataPrazo(LocalDate.now().plusDays(3))
                        .titulo("bbb")
                        .descricao("ccc")
                        .estaConcluida(false)
                        .criadoEm(LocalDateTime.now())
                        .id(2L).build());


        Pageable pageRequest = PageRequest.of(0,10);

        Page<Tarefa> pageTarefa = new PageImpl<>(listaDeTarefas,pageRequest,listaDeTarefas.size());

        when(tarefaRepository.findAll(pageRequest)).thenReturn(pageTarefa);

        ApiResponse<Page<TarefaResponse>> apiResponse = tarefaService.encontrarTodas(pageRequest);


        assertEquals("Tarefas encontrada com sucesso.",apiResponse.message());
        assertEquals(HttpStatus.OK.value(),apiResponse.status());
        assertEquals("xxx",apiResponse.data().getContent().get(0).titulo());
        assertEquals("bbb",apiResponse.data().getContent().get(1).titulo());



        verify(tarefaRepository,times(1)).findAll(pageRequest);

    }


    @Test
    public void deveRetornarListaDeTarefasQueEstaoConcluidas(){

        List<Tarefa> listaConcluida = List.of(Tarefa.builder()
                .estaConcluida(true)
                .titulo("xxx").dataPrazo(LocalDate.now().plusDays(2))
                .id(1L)
                .descricao("vvv").build(),
                Tarefa.builder().estaConcluida(true)
                        .titulo("mmm")
                        .descricao("nnn")
                        .dataPrazo(LocalDate.now().plusDays(3))
                        .id(2L)
                        .build());

        Pageable pageRequest = PageRequest.of(0,10);

        Page<Tarefa> pageTarefa = new PageImpl<>(listaConcluida,pageRequest,listaConcluida.size());

        when(tarefaRepository.findAllByEstaConcluida(true,pageRequest)).thenReturn(pageTarefa);


        ApiResponse<Page<TarefaResponse>> apiResponse = tarefaService.encontrarTarefasPorStatus(true,pageRequest);


        assertEquals(HttpStatus.OK.value(),apiResponse.status());
        assertEquals("Tarefas que estão concluidas encontradas.",apiResponse.message());

        assertTrue(apiResponse.data().getContent().get(0).estaConcluida());
        assertTrue(apiResponse.data().getContent().get(1).estaConcluida());

        verify(tarefaRepository,times(1)).findAllByEstaConcluida(true,pageRequest);
    }

    @Test
    public void deveRetornarListaDeTarefasQueNaoEstaoConcluidas(){

        List<Tarefa> listaNaoConcluida = List.of(Tarefa.builder()
                        .estaConcluida(false)
                        .titulo("xxx").dataPrazo(LocalDate.now().plusDays(2))
                        .id(1L)
                        .descricao("vvv").build(),
                Tarefa.builder().estaConcluida(false)
                        .titulo("mmm")
                        .descricao("nnn")
                        .dataPrazo(LocalDate.now().plusDays(3))
                        .id(2L)
                        .build());

        Pageable pageRequest = PageRequest.of(0,10);

        Page<Tarefa> pageTarefa = new PageImpl<>(listaNaoConcluida,pageRequest,listaNaoConcluida.size());

        when(tarefaRepository.findAllByEstaConcluida(false,pageRequest)).thenReturn(pageTarefa);


        ApiResponse<Page<TarefaResponse>> apiResponse = tarefaService.encontrarTarefasPorStatus(false,pageRequest);


        assertEquals(HttpStatus.OK.value(),apiResponse.status());
        assertEquals("Tarefas que estão pendentes.",apiResponse.message());

        assertFalse(apiResponse.data().getContent().get(0).estaConcluida());
        assertFalse(apiResponse.data().getContent().get(1).estaConcluida());

        verify(tarefaRepository,times(1)).findAllByEstaConcluida(false,pageRequest);
    }

    @Test
    public void deveRetornarTarefasAtrasadas(){
        List<Tarefa> listaDeTarefas = List.of(
                Tarefa
                .builder()
                        .id(1L)
                        .dataPrazo(LocalDate.now().minusDays(2))
                        .descricao("bbb")
                        .titulo("nnn")
                        .estaConcluida(false).build(),
                Tarefa.builder()
                        .id(2L)
                        .dataPrazo(LocalDate.now().minusDays(3))
                        .descricao("xxx")
                        .titulo("vvv")
                        .estaConcluida(false).build());

        Pageable pageReq = PageRequest.of(0,10);

        Page<Tarefa> page = new PageImpl<>(listaDeTarefas,pageReq,listaDeTarefas.size());

        when(tarefaRepository.findAllByDataPrazo(pageReq)).thenReturn(page);


        ApiResponse<Page<TarefaResponse>> apiResponse = tarefaService.encontrarTarefasAtrasadas(pageReq);

        assertEquals("Tarefas atrasadas encontradas com sucesso.",apiResponse.message());
        assertEquals(HttpStatus.OK.value(),apiResponse.status());
        assertNotNull(apiResponse.data());

        assertEquals(LocalDate.now().minusDays(2),apiResponse.data().getContent().get(0).dataPrazo());
        assertEquals(LocalDate.now().minusDays(3),apiResponse.data().getContent().get(1).dataPrazo());

        verify(tarefaRepository,times(1)).findAllByDataPrazo(pageReq);
    }

    @Test
    public void deveAtualizarStatusQuandoDiferente(){

        Tarefa tarefaPendente = Tarefa
                .builder()
                .id(1L)
                .descricao("Estudar matematica")
                .titulo("Ir para escola")
                .estaConcluida(false)
                .build();

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaPendente));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(i -> i.getArguments()[0]);

        tarefaService.atualizarConcluida(1L,true);

        verify(tarefaRepository).save(tarefaCaptor.capture());


        Tarefa tarefaMudada = tarefaCaptor.getValue();

        assertTrue(tarefaMudada.isEstaConcluida());
    }

    @Test
    public void naoDeveAlterarStatusQuandoForIgual(){

        Tarefa tarefaPendente = Tarefa
                .builder()
                .id(1L)
                .descricao("Estudar matematica")
                .titulo("Ir para escola")
                .estaConcluida(false)
                .build();

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaPendente));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(i -> i.getArguments()[0]);

        tarefaService.atualizarConcluida(1L,false);

        verify(tarefaRepository).save(tarefaCaptor.capture());


        Tarefa tarefaMudada = tarefaCaptor.getValue();

        assertFalse(tarefaMudada.isEstaConcluida());

    }



    @Test
    public void deveLancarExcecaoQuandoNaoEncontrarTarefaParaAtualizarStatus(){
        Long tarefaId = 99L;

        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,() -> {
            tarefaService.atualizarConcluida(99L,true);
        });

        assertNotNull(ex);
        assertEquals("Tarefa não encontrada.",ex.getMessage());
    }

    @Test
    public void deveLancarExcecaoQuandoNaoEncontrarTarefaParaPendente(){
        Long tarefaId = 99L;

        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,() -> {
            tarefaService.atualizarConcluida(99L,false);
        });

        assertNotNull(ex);
        assertEquals("Tarefa não encontrada.",ex.getMessage());
    }

}
