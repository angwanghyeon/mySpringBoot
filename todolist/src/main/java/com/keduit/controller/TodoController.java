package com.keduit.controller;

import com.keduit.dto.ResponseDto;
import com.keduit.dto.TodoDto;
import com.keduit.model.TodoEntity;
import com.keduit.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
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
    public ResponseEntity<?> createTodo(@RequestBody TodoDto dto,
                @AuthenticationPrincipal String userid){

        try {

            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setId(null);
            entity.setUserId(userid);
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
    public ResponseEntity<?> updateTodo(@RequestBody TodoDto dto,
                                        @AuthenticationPrincipal String userId){


            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setUserId(userId);

            List<TodoEntity> entities = todoService.update(entity);
            List<TodoDto> dtos = entities.stream().map(TodoDto::new)
                    .collect(Collectors.toList());
            ResponseDto<TodoDto> responseDto = ResponseDto.<TodoDto>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDto dto,
                                        @AuthenticationPrincipal String userId) {

        try {

            //dto를 entity로 변환하는 method 사용
            TodoEntity entity = TodoDto.toEntity(dto);
            //만들어둔 entity에 userid를 우리가 위에서 만든 userid로 준다
            entity.setUserId(userId);
            //
            List<TodoEntity> entities = todoService.delete(entity);
            List<TodoDto> dtos = entities.stream().map(TodoDto::new)
                    .collect(Collectors.toList());
            ResponseDto<TodoDto> responseDto = ResponseDto.<TodoDto>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDto<TodoDto> errorResponse = ResponseDto.<TodoDto>builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<?> readTodo(@AuthenticationPrincipal String userId){
    try {

        List<TodoEntity> entities = todoService.read(userId);
        List<TodoDto> dtos = entities.stream().map(TodoDto::new)
                .collect(Collectors.toList());
        ResponseDto<TodoDto> responseDto = ResponseDto.<TodoDto>builder()
                .data(dtos)
                .build();
        return ResponseEntity.ok().body(responseDto);
    }catch (Exception e){
        String error = e.getMessage();
        System.out.println("error = " + error);
        ResponseDto<TodoDto> errorResponse = ResponseDto.<TodoDto>builder()
                .error(error)
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }
    }

}
