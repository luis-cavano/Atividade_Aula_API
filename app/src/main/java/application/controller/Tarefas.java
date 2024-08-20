package application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import application.model.Tarefa;
import application.repository.TarefaRepository;

@RestController
@RequestMapping("/tarefas")
public class Tarefas {
    @Autowired
    private TarefaRepository tarefaRepo;

    @GetMapping
    public Iterable<Tarefa> list(){
        return tarefaRepo.findAll();
    }

    @GetMapping("/{id}")
    public Tarefa details(@PathVariable long id){
        Optional<Tarefa> resultado = tarefaRepo.findById(id);
        if( resultado.isEmpty() ){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tarefa não encontrada"
            );
        }
        return resultado.get();
    }

    @PostMapping
    public Tarefa insert(@RequestBody Tarefa tarefa) {
        return tarefaRepo.save(tarefa);
    }

    @PutMapping("/{id}")
    public Tarefa put(
        @PathVariable long id,
        @RequestBody Tarefa tarefa
    ) {
        Optional<Tarefa> result = tarefaRepo.findById(id);
        if( result.isEmpty() ){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tarefa não encontrada"
            );
        }
        if( tarefa.getDescricao().isEmpty() ){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Nome do aluno é inválido."
            );
        }
        result.get().setDescricao(tarefa.getDescricao());
        result.get().setConcluido(tarefa.isConcluido());
        return tarefaRepo.save(result.get()); 
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        if( !tarefaRepo.existsById(id) ) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Aluno não encontrado"
            ); 
        }
        tarefaRepo.deleteById(id);
    }
}
