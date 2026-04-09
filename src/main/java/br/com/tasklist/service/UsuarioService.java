package br.com.tasklist.service;


import br.com.tasklist.dto.request.UsuarioAttRequest;
import br.com.tasklist.dto.response.ApiResponse;
import br.com.tasklist.dto.request.UsuarioRequest;
import br.com.tasklist.dto.response.UsuarioResponse;
import br.com.tasklist.entity.Usuario;
import br.com.tasklist.exception.AlreadyExistsException;
import br.com.tasklist.exception.NotFoundException;
import br.com.tasklist.mapper.UsuarioMapper;
import br.com.tasklist.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService{

    private final UsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;


    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public ApiResponse<UsuarioResponse> criar(UsuarioRequest request){

        if(usuarioRepository.existsByEmail(request.email())) throw new AlreadyExistsException("Ja existe esse email no sistema.");

        if(usuarioRepository.existsByCpf(request.cpf())) throw new AlreadyExistsException("Ja existe esse cpf no sistema.");

        Usuario usuarioSalvo = usuarioRepository.save(usuarioMapper.toEntity(request));

        return new ApiResponse<>(
                "Usuario salvo com sucesso.",
                HttpStatus.CREATED.value(),
                usuarioMapper.toResponse(usuarioSalvo)
        );
    }
    @Transactional
    public ApiResponse<UsuarioResponse> atualizar(Long id,UsuarioAttRequest request){
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuario não encontrado."));

        usuarioMapper.mergeUsuario(request,usuarioEncontrado);

        Usuario usuarioSalvo = usuarioRepository.save(usuarioEncontrado);

        return new ApiResponse<>(
                "Usuario atualizado com sucesso",
                HttpStatus.OK.value(),
                usuarioMapper.toResponse(usuarioSalvo));
    }

    @Transactional
    public ApiResponse<Void> deletar(Long id){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuario não encontrado."));
        usuarioRepository.delete(usuario);
        return new ApiResponse<>(
                "Usuario deletado com sucesso",
                HttpStatus.OK.value(),
                null);
    }


    public ApiResponse<Page<UsuarioResponse>> encontrarTodosOsUsuarios(Pageable pageable){
        Page<UsuarioResponse> usuarios = usuarioRepository
                .findAll(pageable).map(usuarioMapper::toResponse);
        return new ApiResponse<>(
                "Usuarios encontrado com sucesso.",
                HttpStatus.OK.value(),
                usuarios);
    }

    public ApiResponse<UsuarioResponse> encontrarUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuario não encontrado."));
        return new ApiResponse<>(
                "Usuario encontrado com sucesso.",
                HttpStatus.OK.value(),
                usuarioMapper.toResponse(usuario));
    }

}
