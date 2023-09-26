package com.keduit.entity;

import com.keduit.dto.MemberFormDto;
import com.keduit.repository.CartRepository;
import com.keduit.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setAddress("서울시 은평구 구산동");
        memberFormDto.setName("안팡현");
        memberFormDto.setEmail("aph@ang.com");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }


    @Test
    @DisplayName("장바구니 회원 일대일 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush();
        em.clear();

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityExistsException::new);
        assertEquals(savedCart.getMember().getId(), member.getId());
    }
}
