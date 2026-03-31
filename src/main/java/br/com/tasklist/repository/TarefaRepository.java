package br.com.tasklist.repository;

import br.com.tasklist.entity.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;


public interface TarefaRepository extends JpaRepository<Tarefa,Long> {

    Page<Tarefa> findAll(Pageable pageable);

    Page<Tarefa> findAllByEstaConcluida(boolean estaConcluida,Pageable pageable);

    @NativeQuery(value = "SELECT t.* FROM Tarefa t LEFT JOIN Usuario u ON u.id = t.usuario_id WHERE t.data_prazo < CURRENT_DATE")
    Page<Tarefa> findAllByDataPrazo(Pageable pageable);
}
