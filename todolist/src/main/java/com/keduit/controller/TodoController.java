package com.keduit.controller;

import com.keduit.dto.ResponseDto;
import com.keduit.dto.TodoDto;
import com.keduit.model.TodoEntity;
import com.keduit.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo(){
        String str = todoService.testService();
        List<String> list = new ArrayList<>();

        list.add(str);
        ResponseDto<String> responseDto =
                ResponseDto.<String>builder()
                .data(list)
                .build();
        //기존에 새로 new 해서 생성해서 보내는게 아니라 ok 일 때 body 에 reponseDto를 담아서 보내주겠다
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDto dto){

        try {
            String tempUserId = "temporal-user";

            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setId(null);
            entity.setUserId(tempUserId);
            //서비스로부터 entity list를 가져옴
            List<TodoEntity> entities = todoService.create(entity);
            //entity list를 todoDto list로 변환(스트림을 활용)
            List<TodoDto> dtos = entities.stream().map(TodoDto::new)
                    .collect(Collectors.toList());
            //변환된 TodoDto list를 이용해 ResponseDto를 초기화한다=값을 대입한다
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
                    .data(dtos)
                    .build();
            //ResponseDto를 반환한다.
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            //예외 발생 시 dto 대신 error에 메세지를 넣어서 리턴
            String error = e.getMessage();
            ResponseDto<TodoDto> responseDto = ResponseDto.<TodoDto>builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDto dto){

            String tempUserId = "temporal-user";

            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setUserId(tempUserId);

            List<TodoEntity> entities = todoService.update(entity);
            List<TodoDto> dtos = entities.stream().map(TodoDto::new)
                    .collect(Collectors.toList());
            ResponseDto<TodoDto> responseDto = ResponseDto.<TodoDto>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(responseDto);
    }

}
