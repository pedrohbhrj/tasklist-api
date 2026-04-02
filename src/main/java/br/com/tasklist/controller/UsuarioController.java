package br.com.tasklist.controller;

import br.com.tasklist.dto.request.UsuarioAttRequest;
import br.com.tasklist.dto.response.ApiResponse;
import br.com.tasklist.dto.request.UsuarioRequest;
import br.com.tasklist.dto.response.UsuarioResponse;
import br.com.tasklist.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponse>> criar(@RequestBody @Valid UsuarioRequest request){
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(service.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> atualizar(@PathVariable Long id,@RequestBody @Valid UsuarioAttRequest request){
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(service.atualizar(id,request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> encontrar(@PathVariable Long id){
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(service.encontrarUsuario(id));
    }

    @GetMapping("/todos")
    public ResponseEntity<ApiResponse<Page<UsuarioResponse>>> encontrarTodos(@RequestParam(value = "page",defaultValue = "0") int page,
                                                                             @RequestParam(value = "size",defaultValue = "10") int size){
        Pageable pageUser = PageRequest.of(page,size);
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(service.encontrarTodosOsUsuarios(pageUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.deletar(id));
    }


}
