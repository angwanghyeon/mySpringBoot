package com.keduit.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class OrderItem {

    //마리아 DB랑 Mysql은 이렇게 줘야함
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice; //주문 가격

    private int count; //수량

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
