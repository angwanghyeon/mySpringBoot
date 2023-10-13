package com.keduit.service;

import com.keduit.model.TodoEntity;
import com.keduit.repository.TodoRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;

    public String testService(){
        TodoEntity todo = TodoEntity.builder()
                .title("My first todo list")
                .build();
        todoRepository.save(todo);
        TodoEntity savedEntity = todoRepository.findById(todo.getId()).get();
        return savedEntity.getTitle();
    }

    private void validate(final TodoEntity entity){
        if (entity == null){
            log.warn("Entity is null");
            throw new RuntimeException("Entity is null");
        }
        if (entity.getUserId() == null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");        }
    }

    public List<TodoEntity> create(TodoEntity entity){

        validate(entity);
        todoRepository.save(entity);
        log.info("Entity Id : {} is saved.", entity.getId());
        return todoRepository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity> read(String userId){
        return todoRepository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity){
        validate(entity);

        final Optional<TodoEntity> original =  todoRepository.findById(entity.getId());
        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            todoRepository.save(todo);
        });
        return read(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity){
        //먼저 entity를 한번 확인한다.
        validate(entity);

        try {
            //jpa repository에 있는 delete 사용
            todoRepository.delete(entity);
        }catch (Exception e){
            log.error("delete error 발생...", entity.getId(), e);
            throw new RuntimeException("delete error 발생..." + entity.getId());
        }

        return read(entity.getUserId());
    }
}
