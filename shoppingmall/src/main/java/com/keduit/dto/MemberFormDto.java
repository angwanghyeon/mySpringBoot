package com.keduit.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class MemberFormDto {

    @NotBlank(message = "이름을 입력해라")
    private String name;

    @NotBlank(message = "비밀번호는 필수다")
    @Length(min = 4, max = 12, message = "비밀번호는 8자이상 16자 이하로 입력하도록")
    private String password;

    @NotEmpty(message = "이메일은 필수다 ")
    @Email(message = "이메일 형식을 맞춰라")
    private String email;

    @NotEmpty(message = "주소도 필수 값이다")
    private String address;
}
