package br.com.tasklist;


import br.com.tasklist.dto.request.UsuarioAttRequest;
import br.com.tasklist.dto.request.UsuarioRequest;
import br.com.tasklist.dto.response.ApiResponse;
import br.com.tasklist.dto.response.UsuarioResponse;
import br.com.tasklist.entity.Usuario;
import br.com.tasklist.exception.AlreadyExistsException;
import br.com.tasklist.exception.NotFoundException;
import br.com.tasklist.mapper.UsuarioMapper;
import br.com.tasklist.repository.UsuarioRepository;
import br.com.tasklist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Spy
    private UsuarioMapper usuarioMapper = Mappers.getMapper(UsuarioMapper.class);

    @InjectMocks
    private UsuarioService service;

    @Test
    public void deveCriarUsuario() {

        Usuario usuarioEntity = Usuario
                .builder()
                .id(1L)
                .cpf("123.456.789-12")
                .email("pedro@gmail.com")
                .nome("pedro henrique")
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .imgUrlPerfil("https://www.google.com")
                .build();

        UsuarioRequest usuarioRequest = new UsuarioRequest(
                "pedro henrique",
                "pedro@gmail.com",
                "123.456.789-12",
                "https://www.google.com");
        UsuarioResponse usuarioResponse = new UsuarioResponse(
                usuarioEntity.getId(),
                usuarioEntity.getNome(),
                usuarioEntity.getEmail(),
                usuarioEntity.getCpf(),
                usuarioEntity.getCriadoEm(),
                usuarioEntity.getImgUrlPerfil()
        );


        Mockito.when(repository.existsByCpf(usuarioRequest.cpf())).thenReturn(false);

        Mockito.when(repository.existsByEmail(usuarioRequest.email())).thenReturn(false);

        Mockito.when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));



        ApiResponse<UsuarioResponse> response = service.criar(usuarioRequest);


        assertEquals("Usuario salvo com sucesso.", response.message());
        assertEquals(HttpStatus.CREATED.value(), response.status());

        assertEquals(usuarioResponse.nome(), response.data().nome());
        assertEquals(usuarioResponse.email(), response.data().email());
        assertEquals(usuarioResponse.cpf(), response.data().cpf());


        Mockito.verify(repository, times(1)).save(any(Usuario.class));

    }

    @Test
    public void deveLancarExcecaoQuandoEmailJaExiste() {

        UsuarioRequest usuarioRequest =
                new UsuarioRequest(
                        "pedro",
                        "pedrao@gmail.com",
                        "123.234.234-35",
                        "www.google.com"
                );

        Mockito.when(repository.existsByEmail(usuarioRequest.email())).thenReturn(true);


        AlreadyExistsException alreadyExists = assertThrows(AlreadyExistsException.class, () -> {
            service.criar(usuarioRequest);
        });


        assertEquals("Ja existe esse email no sistema.", alreadyExists.getMessage());


        Mockito.verify(repository, never()).save(any(Usuario.class));

    }

    @Test
    public void deveLancarExcecaoQuandoCpfJaExiste() {

        UsuarioRequest usuarioRequest =
                new UsuarioRequest(
                        "pedro",
                        "pedrao@gmail.com",
                        "123.234.234-35",
                        "www.google.com"
                );

        Mockito.when(repository.existsByEmail(usuarioRequest.email())).thenReturn(false);
        Mockito.when(repository.existsByCpf(usuarioRequest.cpf())).thenReturn(true);


        AlreadyExistsException alreadyExists = assertThrows(AlreadyExistsException.class, () -> {
            service.criar(usuarioRequest);
        });


        assertEquals("Ja existe esse cpf no sistema.", alreadyExists.getMessage());


        Mockito.verify(repository, never()).save(any(Usuario.class));

    }

    @Test
    public void deveAtualizarUsuario() {

        Optional<Usuario> usuarioEncontrado =
                Optional.of(Usuario.builder()
                        .email("pedro@gmail.com")
                        .imgUrlPerfil("www.yahoo.com")
                        .atualizadoEm(LocalDateTime.now())
                        .criadoEm(LocalDateTime.now().minusDays(2))
                        .nome("pedro gabriel")
                        .cpf("456.333.222-12")
                        .id(1L)
                        .build());

        UsuarioAttRequest attRequest = new UsuarioAttRequest(
                "pedrao", "pedrooo@gmail.com",
                "156.243.234-63",
                "www.google.com"
        );


        Mockito.when(repository.findById(1L)).thenReturn(usuarioEncontrado);
        Mockito.when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));


        ApiResponse<UsuarioResponse> response = service.atualizar(1L, attRequest);


        assertEquals("Usuario atualizado com sucesso", response.message());
        assertEquals(HttpStatus.OK.value(), response.status());

        Usuario entidadeMutada = usuarioEncontrado.get();

        assertEquals("pedrao", entidadeMutada.getNome());
        assertEquals("pedrooo@gmail.com", entidadeMutada.getEmail());
        assertEquals("156.243.234-63", entidadeMutada.getCpf());
        assertEquals("www.google.com", entidadeMutada.getImgUrlPerfil());

        assertEquals(entidadeMutada.getNome(),response.data().nome());

        verify(usuarioMapper, times(1)).mergeUsuario(attRequest, usuarioEncontrado.get());
        verify(repository, times(1)).save(usuarioEncontrado.get());

    }

    @Test
    public void deveLancarExcecaoAoBuscarUsuarioParaAtualizar() {

        Optional<Usuario> user = Optional.empty();

        when(repository.findById(99L)).thenReturn(user);


        NotFoundException ex = assertThrows(NotFoundException.class,() -> {
            service.atualizar(99L,new UsuarioAttRequest("pedro","dasdsa@gmail.com","156.435.546-34","www.google.com"));
        });


        assertEquals("Usuario não encontrado.",ex.getMessage());

        verify(repository,never()).save(any());


    }

    @Test
    public void deveDeletarUsuario(){
        Optional<Usuario> usuario = Optional.of(Usuario.builder()
                .id(1L)
                .nome("Pedrao")
                .build())
                ;

        when(repository.findById(1L)).thenReturn(usuario);
        doNothing().when(repository).delete(usuario.get());

        ApiResponse<Void> apiResponse = service.deletar(1L);

        assertEquals("Usuario deletado com sucesso",apiResponse.message());
        assertEquals(HttpStatus.OK.value(),apiResponse.status());
        assertNull(apiResponse.data());

        verify(repository,times(1)).delete(usuario.get());

    }

    @Test
    public void deveJogarExcecaoQuandoForDeletarUsuarioNaoExistir(){

        Long usuarioId = 99L;

        when(repository.findById(usuarioId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,() -> {
            service.deletar(99L);
        });

        assertEquals("Usuario não encontrado.",ex.getMessage());

        verify(repository,never()).delete(any());
    }

    @Test
    public void deveRetornarTodosOsUsuariosPaginados() {

        List<Usuario> usuariosFake = List.of(
                Usuario.builder()
                        .id(1L)
                        .nome("pedro")
                        .build(),
                Usuario.builder()
                        .id(2L)
                        .nome("ruan")
                        .build()
        );

        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Usuario> page = new PageImpl<>(usuariosFake,pageRequest,usuariosFake.size());

        when(repository.findAll(pageRequest)).thenReturn(page);


        ApiResponse<Page<UsuarioResponse>> apiResponse = service.encontrarTodosOsUsuarios(pageRequest);


        assertNotNull(apiResponse);

        assertEquals(2,apiResponse.data().getTotalElements());

        assertEquals("pedro",apiResponse.data().getContent().getFirst().nome());
        assertEquals("ruan",apiResponse.data().getContent().get(1).nome());

        assertEquals("Usuarios encontrado com sucesso.",apiResponse.message());
        assertEquals(HttpStatus.OK.value(),apiResponse.status());


        verify(repository,times(1)).findAll(pageRequest);

    }

    @Test
    public void deveEncontrarUsuario(){
        Usuario usuario = Optional.of(Usuario.builder()
                .id(1L)
                .nome("pedro")
                .build()).get();

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));


        ApiResponse<UsuarioResponse> apiResponse = service.encontrarUsuario(1L);


        assertEquals(usuario.getNome(),apiResponse.data().nome());
        assertEquals(usuario.getId(),apiResponse.data().id());

        assertEquals("Usuario encontrado com sucesso.",apiResponse.message());
        assertEquals(HttpStatus.OK.value(),apiResponse.status());


        verify(repository,times(1)).findById(any());
    }

    @Test
    public void deveLancarExcecaoAoNaoEncontrarUsuario(){


        Long usuarioId = 99L;

        when(repository.findById(usuarioId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,() -> {
            service.encontrarUsuario(usuarioId);
        });

        assertEquals("Usuario não encontrado.",ex.getMessage());

        verify(repository,times(1)).findById(usuarioId);
    }
}
