package br.com.tasklist.service;

import br.com.tasklist.dto.response.ApiResponse;
import br.com.tasklist.dto.request.TarefaRequest;
import br.com.tasklist.dto.response.TarefaResponse;
import br.com.tasklist.entity.Tarefa;
import br.com.tasklist.entity.Usuario;
import br.com.tasklist.exception.NotFoundException;
import br.com.tasklist.mapper.TarefaMapper;
import br.com.tasklist.repository.TarefaRepository;
import br.com.tasklist.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TarefaMapper tarefaMapper;
    private final UsuarioRepository usuarioRepository;

    public TarefaService(TarefaRepository tarefaRepository, TarefaMapper tarefaMapper, UsuarioRepository usuarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.tarefaMapper = tarefaMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ApiResponse<TarefaResponse> criar(Long id,TarefaRequest request){

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuario não encontrado."));



        Tarefa tarefaCriada = Tarefa
                .builder()
                .dataPrazo(request.dataPrazo())
                .descricao(request.descricao())
                .titulo(request.titulo())
                .usuario(usuario)
                .build();

        Tarefa tarefaSalva = tarefaRepository.save(tarefaCriada);

        return new ApiResponse<>("Tarefa criada com sucesso.",
                HttpStatus.CREATED.value(),
                tarefaMapper.toRes(tarefaSalva)
        );
    }

    @Transactional
    public ApiResponse<Void> deletar(Long id){
        Tarefa tarefaCriada =
                tarefaRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException("Tarefa não encontrada."));
        tarefaRepository.delete(tarefaCriada);
        return new ApiResponse<>("Tarefa deletada com sucesso.",
                HttpStatus.NO_CONTENT.value(),
                null
        );
    }

    @Transactional
    public ApiResponse<TarefaResponse> encontrar(Long id){
        Tarefa tarefaEncontrada =
                tarefaRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException("Tarefa não encontrada."));
        return new ApiResponse<>("Tarefa encontrada com sucesso.",
                HttpStatus.OK.value(),
                tarefaMapper.toRes(tarefaEncontrada)
        );
    }


    public ApiResponse<Page<TarefaResponse>> encontrarTodas(Pageable pageable){

        Page<TarefaResponse> page = tarefaRepository.findAll(pageable).map(tarefaMapper::toRes);

        return new ApiResponse<>("Tarefas encontrada com sucesso.",
                HttpStatus.OK.value(),
                page
        );
    }

    public ApiResponse<Page<TarefaResponse>> encontrarTarefasPorStatus(boolean estaConcluida,Pageable pageable){

        Page<TarefaResponse> page = tarefaRepository.findAllByEstaConcluida(estaConcluida,pageable).map(tarefaMapper::toRes);

        String mensagem = estaConcluida ? "Tarefas que estão concluidas encontradas." : "Tarefas que estão pendentes.";

        return new ApiResponse<>(mensagem,
                HttpStatus.OK.value(),
                page
        );
    }

    public ApiResponse<Page<TarefaResponse>> encontrarTarefasAtrasadas(Pageable pageable){

        Page<TarefaResponse> page = tarefaRepository.findAllByDataPrazo(pageable).map(tarefaMapper::toRes);


        return new ApiResponse<>("Tarefas atrasadas encontradas com sucesso.",
                HttpStatus.OK.value(),
                page
        );
    }

    @Transactional
    public ApiResponse<TarefaResponse> atualizarConcluida(Long id,boolean estaConcluida){

        Tarefa tarefaEncontrada =
                tarefaRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException("Tarefa não encontrada."));

        if(tarefaEncontrada.isEstaConcluida() != estaConcluida){
            tarefaEncontrada.setEstaConcluida(estaConcluida);
        }


        String mensagem = estaConcluida ? "Tarefa está concluida." : "Tarefa está pendente.";

        Tarefa tarefaSalva = tarefaRepository.save(tarefaEncontrada);


        return new ApiResponse<>(mensagem,
                HttpStatus.OK.value(),
                tarefaMapper.toRes(tarefaSalva)
        );
    }
}
