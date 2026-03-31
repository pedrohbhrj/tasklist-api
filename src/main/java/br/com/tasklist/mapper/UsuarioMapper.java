package br.com.tasklist.mapper;

import br.com.tasklist.dto.request.UsuarioRequest;
import br.com.tasklist.dto.response.UsuarioResponse;
import br.com.tasklist.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id",ignore = true)
    Usuario toEntity(UsuarioRequest request);


    UsuarioResponse toResponse(Usuario usuario);

}
