package com.keduit.entity;

import com.keduit.constant.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문 일자

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 상태

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL,
    orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem){
        orderItemList.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        //인스턴스 하나 생성
        Order order = new Order();
        //주문을 한 멤버 세팅
        order.setMember(member);
        //가져온 list를 하나하나 나눠서 주문에 담는다
        for (OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
        }
        //주문 상태를 설정
        order.setOrderStatus(OrderStatus.ORDER);
        //주문 날짜 설정
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    public int getTotalPrice(){
        //초기값이 0인 인스턴스 생성
        int totalPrice = 0;
        //마찬가지로 만들어진 주문 리스트에서 각각 가져온 아이템 리스트 별 가격을 아이템에 담는다
        for (OrderItem orderItem : orderItemList){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItemList){
            orderItem.cancel();
        }
    }

}
