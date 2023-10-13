package com.keduit.controller;

import com.keduit.dto.ResponseDto;
import com.keduit.dto.UserDto;
import com.keduit.model.UserEntity;
import com.keduit.security.TokenProvider;
import com.keduit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService; // UserService 의존성 주입

    @Autowired
    private TokenProvider tokenProvider; // TokenProvider 의존성 주입

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 회원 가입을 처리하는 엔드포인트
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){

        try {
            // 전달된 UserDto가 유효한지 확인하고 비밀번호가 없으면 예외 발생
            if (userDto == null || userDto.getPassword() == null){
                throw new RuntimeException("사용자의 비밀번호가 존재하지 않습니다.");
            }

            // 전달된 UserDto로 UserEntity 객체 생성
            UserEntity user = UserEntity.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();

            // UserService를 사용하여 사용자 정보 저장 후 응답으로 반환할 UserDto로 변환
            UserEntity registeredUser = userService.create(user);
            UserDto responseUserDto = UserDto.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .password(registeredUser.getPassword())
                    .build();
            return ResponseEntity.ok().body(responseUserDto);
        } catch (Exception e) {
            // 예외 발생 시 에러 메시지를 담은 응답 반환
            ResponseDto responseDto = ResponseDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    // 로그인을 처리하는 엔드포인트
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDto userDto){
        // 주어진 사용자명과 비밀번호로 사용자 엔터티를 찾아옴
        UserEntity user = userService.getByCredentials(
                userDto.getUsername(), userDto.getPassword(), passwordEncoder);
        if (user != null){
            // 올바른 인증 정보인 경우 토큰 생성 및 사용자 정보와 토큰을 응답으로 반환
            final String token = tokenProvider.create(user);
            final UserDto responseUserDto = UserDto.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDto);
        } else {
            // 인증 정보가 올바르지 않은 경우 에러 메시지를 담은 응답 반환
            ResponseDto responseDto = ResponseDto.builder()
                    .error("Login error")
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }
}


