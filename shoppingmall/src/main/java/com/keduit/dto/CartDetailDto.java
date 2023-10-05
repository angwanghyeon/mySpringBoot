package com.keduit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartDetailDto {

    private Long cartItemId; //장바구니 상품 아이디

    private String itemNm; //상품명

    private int price; //상품 가격

    private int count; //상품 수량

    private String imgUrl; //이미지 경로

    public CartDetailDto(Long cartItemId, String itemNm
    ,int price, int count, String imgUrl){

        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;

    }

}
