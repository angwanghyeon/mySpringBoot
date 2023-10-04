package com.keduit.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class CartItemDto {

    @NotNull(message = "상품 아이디는 필수 입력값 입니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 1개 이상의 상품이 담겨야 합니다.")
    private int count;


}
