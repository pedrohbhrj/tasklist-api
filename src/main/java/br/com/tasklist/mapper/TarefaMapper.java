package br.com.tasklist.mapper;

import br.com.tasklist.dto.request.TarefaRequest;
import br.com.tasklist.dto.response.TarefaResponse;
import br.com.tasklist.entity.Tarefa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TarefaMapper {

    @Mapping(target = "id",ignore = true)
    Tarefa toEntity(TarefaRequest request);

    TarefaResponse toRes(Tarefa tarefa);
}
