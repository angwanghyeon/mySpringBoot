package com.keduit.controller;

import com.keduit.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {

    @GetMapping("/ex01")
    public String thymeleafEx01(Model model){
        model.addAttribute("data","타임리프 테스트중...");
        return "thymeleafEX/thymeleafEx01";
    }

    @GetMapping("/ex02")
    public String thymeleafEx02(Model model){
        ItemDto dto = new ItemDto();
        dto.setItemDetail("상품 상세 설명");
        dto.setItemNm("테스트 상품1");
        dto.setPrice(10000);
        dto.setRegTime(LocalDateTime.now());
        model.addAttribute("itemDto", dto);
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping("/ex03")
    public String thymeleafEx03(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (int i=1; i<21; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setId((long)i);
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setPrice(10000+i);
            if(i < 11){
                itemDto.setSellStatCd("SELL");
            }else {
                itemDto.setSellStatCd("SOLD_OUT");
            }
            itemDto.setRegTime(LocalDateTime.now());
            itemDto.setUpdateTime(LocalDateTime.now());

            //list 에 추가
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }
    @GetMapping("/ex04")
    public String thymeleafEx04(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (int i=1; i<21; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setPrice(10000+i);
            if(i < 11){
                itemDto.setSellStatCd("SELL");
            }else {
                itemDto.setSellStatCd("SOLD_OUT");
            }
            itemDto.setRegTime(LocalDateTime.now());
            itemDto.setUpdateTime(LocalDateTime.now());

            //list 에 추가
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping("/ex05")
    public String thymeleafEx05(){
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping("/ex06")
    public String thymeleafEx06(String param1, String param2, Model model){
        model.addAttribute("param1",param1);
        model.addAttribute("param2",param2);
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping("/ex07")
    public String thymeleafEx07(){
        return "thymeleafEx/thymeleafEx07";
    }

    @GetMapping("/exInline")
    public String exInline(RedirectAttributes redirectAttributes){
        System.out.println("......exInline.....");
        ItemDto dto = new ItemDto();
        dto.setItemDetail("상품 상세 설명");
        dto.setItemNm("테스트 상품1");
        dto.setPrice(10000);
        dto.setRegTime(LocalDateTime.now());
        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("dto", dto);
        return "redirect:/thymeleaf/thymeleafEx08";
    }
    @GetMapping("/thymeleafEx08")
    public String thymeleafEx08(){
        System.out.println(".....ex08......");
        return "/thymeleafEx/thymeleafEx08";
    }
}
