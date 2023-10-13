package com.keduit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.UniqueConstraint; // 특정 컬럼에 대한 유니크 제약 조건을 설정하기 위한 어노테이션


@Entity // 이 클래스가 JPA 엔터티 클래스임을 나타냄
@Data // 게터(getter), 세터(setter), equals, hashCode, toString 메서드를 자동으로 생성
@Builder // 빌더 패턴을 사용할 수 있게 함
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자를 자동으로 생성
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 자동으로 생성
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "username")}) // username 컬럼에 대한 유니크 제약 조건 설정
public class UserEntity {

    @Id // 이 필드가 엔터티의 기본 키임을 나타냄
    @GenericGenerator(name = "system-uuid", strategy = "uuid") // Hibernate에서 사용할 UUID 생성 전략을 설정
    @GeneratedValue(generator = "system-uuid") // 기본 키 값이 자동으로 생성되도록 함
    private String id; // 사용자 엔터티의 기본 키

    @Column(nullable = false) // 해당 필드는 데이터베이스 컬럼이며, null 값을 허용하지 않음
    private String username; // 사용자명을 저장하는 필드

    private String password; // 비밀번호를 저장하는 필드

    private String role; // 사용자 역할을 저장하는 필드

    private String authProvider; // 인증 제공자를 저장하는 필드
}
