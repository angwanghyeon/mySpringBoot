package com.keduit.dto;

import com.keduit.constant.ItemSellStatus;
import com.keduit.entity.Item;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@ToString
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명을 반드시 입력하세요")
    private String itemNm;

    @NotNull(message = "가격을 반드시 입력하세요")
    private Integer price;

    @NotBlank(message = "상품 설명을 반드시 입력하세요")
    private String itemDetail;

    @NotNull(message = "재고 수량을 반드시 입력하세요")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }
    /*modelMapper를 통해서 서로 다른 클래스의 값을 변환한다.*/
    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }

}
