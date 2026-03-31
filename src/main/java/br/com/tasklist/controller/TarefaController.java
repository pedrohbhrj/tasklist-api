package br.com.tasklist.controller;

import br.com.tasklist.dto.response.ApiResponse;
import br.com.tasklist.dto.request.TarefaRequest;
import br.com.tasklist.dto.response.TarefaResponse;
import br.com.tasklist.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarefa")
public class TarefaController {

    private final TarefaService service;


    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<ApiResponse<TarefaResponse>> criar(@PathVariable Long usuarioId, @RequestBody @Valid TarefaRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criar(usuarioId,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(service.deletar(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TarefaResponse>> encontrarTarefa(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.encontrar(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TarefaResponse>> marcarComoConcluida(@PathVariable Long id,
                                                                           @RequestParam("estaConcluida") boolean estaConcluida){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.atualizarConcluida(id,estaConcluida));
    }

    @GetMapping("/todas")
    public ResponseEntity<ApiResponse<Page<TarefaResponse>>> encontrarTodasTarefas(@RequestParam(value = "page",defaultValue = "0") int page,
                                                                       @RequestParam(value = "size",defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.encontrarTodas(pageable));
    }


    @GetMapping("/{estaConcluida}")
    public ResponseEntity<ApiResponse<Page<TarefaResponse>>> tarefasPorStatus(@PathVariable boolean estaConcluida,
                                                                     @RequestParam(value = "page",defaultValue = "0") int page,
                                                                     @RequestParam(value = "size",defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.status(HttpStatus.OK).body(service.encontrarTarefasPorStatus(estaConcluida,pageable));
    }

    @GetMapping("/atrasadas")
    public ResponseEntity<ApiResponse<Page<TarefaResponse>>> encontrarTarefasAtrasadas(@RequestParam(value = "page",defaultValue = "0") int page,
                                                                                       @RequestParam(value = "size",defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size);

        return ResponseEntity.status(HttpStatus.OK).body(service.encontrarTarefasAtrasadas(pageable));
    }
}
