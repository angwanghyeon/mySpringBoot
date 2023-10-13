package com.keduit.service;

import com.keduit.model.UserEntity;
import com.keduit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity){
        if (userEntity == null || userEntity.getUsername() == null){
            throw new RuntimeException("invalid arguments");
        }
        //final을 붙이는건 안정성을 위함
        final String username = userEntity.getUsername();
        //create 중인데 이미 유저 이름이 존재한다면
        if (userRepository.existsByUsername(username)){
            log.warn("이미 등록된 사용자가 있습니다.", username);
            throw new RuntimeException("이미 등록된 사용자가 존재함");
        }

        return userRepository.save(userEntity);
    }

    //username을
    public UserEntity getByCredentials(final String username,
                                       final String password,
                                       final PasswordEncoder encoder){

        final UserEntity oriUser = userRepository.findByUsername(username);

        if (oriUser != null && encoder.matches(password, oriUser.getPassword())){
            return oriUser;
        }
        return null;
    }
}
