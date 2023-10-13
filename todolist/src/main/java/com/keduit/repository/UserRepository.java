package com.keduit.repository;

import com.keduit.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByUsername(String username); // 사용자명(username)을 기반으로 사용자 엔터티를 조회하는 메서드

    Boolean existsByUsername(String username); // 주어진 사용자명(username)이 존재하는지 여부를 확인하는 메서드

    UserEntity findByUsernameAndPassword(String username, String password); // 사용자명(username)과 비밀번호(password)를 기반으로 사용자 엔터티를 조회하는 메서드
}
