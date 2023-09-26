package com.keduit.service;

import com.keduit.dto.MemberFormDto;
import com.keduit.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
//@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberServiceTests {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;


    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setAddress("서울시 관악구 봉천로 한국정보교육원");
        memberFormDto.setName("안팡현");
        memberFormDto.setEmail("angpang@ang.com");
        memberFormDto.setPassword("1111");
        return Member.createMember(memberFormDto, passwordEncoder);
    }
    
    @Test
    @DisplayName("회원 가입 테스트")
    public void saveMemberTest(){
        Member member = createMember();
        Member saveMember = memberService.saveMember(member);

        assertEquals(member.getEmail(), saveMember.getEmail());
        assertEquals(member.getName(), saveMember.getName());
        assertEquals(member.getAddress(), saveMember.getAddress());
        assertEquals(member.getRole(), saveMember.getRole());
        assertEquals(member.getPassword(), saveMember.getPassword());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });
        assertEquals("이미 가입된 회원 입니다.", e.getMessage());
    }
}
