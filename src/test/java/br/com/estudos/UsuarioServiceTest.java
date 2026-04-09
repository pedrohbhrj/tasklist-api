package br.com.estudos;


import br.com.tasklist.dto.request.UsuarioAttRequest;
import br.com.tasklist.dto.request.UsuarioRequest;
import br.com.tasklist.dto.response.ApiResponse;
import br.com.tasklist.dto.response.UsuarioResponse;
import br.com.tasklist.entity.Usuario;
import br.com.tasklist.exception.AlreadyExistsException;
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
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private UsuarioRepository repository;

    @Spy
    private UsuarioMapper usuarioAttMapper = Mappers.getMapper(UsuarioMapper.class);

    @InjectMocks
    private UsuarioService service;

    @Test
    public void deveCriarUsuario(){

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

        Mockito.when(mapper.toEntity(usuarioRequest)).thenReturn(usuarioEntity);

        Mockito.when(repository.save(usuarioEntity)).thenReturn(usuarioEntity);

        Mockito.when(mapper.toResponse(usuarioEntity)).thenReturn(usuarioResponse);



        ApiResponse<UsuarioResponse> response = service.criar(usuarioRequest);


        assertEquals("Usuario salvo com sucesso.",response.message());
        assertEquals(HttpStatus.CREATED.value(),response.status());
        assertEquals(usuarioResponse,response.data());


        Mockito.verify(repository,times(1)).save(usuarioEntity);

    }

    @Test
    public void deveLancarExcecaoQuandoEmailJaExiste(){

        UsuarioRequest usuarioRequest =
               new UsuarioRequest(
                       "pedro",
                       "pedrao@gmail.com",
                       "123.234.234-35",
                       "www.google.com"
               );

        Mockito.when(repository.existsByEmail(usuarioRequest.email())).thenReturn(true);


        AlreadyExistsException alreadyExists = assertThrows(AlreadyExistsException.class,() -> {
            service.criar(usuarioRequest);
        });


        assertEquals("Ja existe esse email no sistema.",alreadyExists.getMessage());


        Mockito.verify(repository,never()).save(any(Usuario.class));

    }

    @Test
    public void deveLancarExcecaoQuandoCpfJaExiste(){

        UsuarioRequest usuarioRequest =
                new UsuarioRequest(
                        "pedro",
                        "pedrao@gmail.com",
                        "123.234.234-35",
                        "www.google.com"
                );

        Mockito.when(repository.existsByEmail(usuarioRequest.email())).thenReturn(false);
        Mockito.when(repository.existsByCpf(usuarioRequest.cpf())).thenReturn(true);


        AlreadyExistsException alreadyExists = assertThrows(AlreadyExistsException.class,() -> {
            service.criar(usuarioRequest);
        });


        assertEquals("Ja existe esse cpf no sistema.",alreadyExists.getMessage());


        Mockito.verify(repository,never()).save(any(Usuario.class));

    }

    @Test
    public void deveAtualizarUsuario(){

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
                "pedrao","pedrooo@gmail.com",
                "156.243.234-63",
                "www.google.com"
        );

        UsuarioResponse usuarioResponse = new UsuarioResponse(1L,"pedrao","pedrooo@gmail.com","156.243.234-63",LocalDateTime.now(),"www.google.com");

        Mockito.when(repository.findById(1L)).thenReturn(usuarioEncontrado);
        Mockito.when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));
        Mockito.when(mapper.toResponse(any(Usuario.class))).thenReturn(usuarioResponse);




        ApiResponse<UsuarioResponse> response = service.atualizar(1L,attRequest);


        assertEquals("Usuario atualizado com sucesso",response.message());
        assertEquals(HttpStatus.OK.value(),response.status());

        assertEquals("pedrao",response.data().nome());
        assertEquals("pedrooo@gmail.com",response.data().email());
        assertEquals("156.243.234-63",response.data().cpf());
        assertEquals("www.google.com",response.data().imgUrlPerfil());

        verify(mapper,times(1)).mergeUsuario(attRequest,usuarioEncontrado.get());
        verify(repository,times(1)).save(usuarioEncontrado.get());


    }
}
