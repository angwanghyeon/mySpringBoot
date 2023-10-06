package com.keduit.service;

import com.keduit.constant.ItemSellStatus;
import com.keduit.constant.OrderStatus;
import com.keduit.dto.OrderDto;
import com.keduit.entity.Item;
import com.keduit.entity.Member;
import com.keduit.entity.Order;
import com.keduit.entity.OrderItem;
import com.keduit.repository.ItemRepository;
import com.keduit.repository.MemberRepository;
import com.keduit.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OrderServiceTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member  = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void orderTest(){
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItemList();
        int totalPrice = orderDto.getCount()*item.getPrice();
        assertEquals(totalPrice, order.getTotalPrice());
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrderTest(){
        //인스턴스 하나씩 생성
        Item item = saveItem();
        Member member = saveMember();

        //아이템에 맞는 주문을 세팅한다.
        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(item.getId());
        orderDto.setCount(10);
        //order를 만들어서 그 아이디를 가져온다.
        Long orderId = orderService.order(orderDto, member.getEmail());
        //아이디를 바탕으로 order를 하나 가져온다
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        //취소한다.
        orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
        assertEquals(100, item.getStockNumber());
    }


}
