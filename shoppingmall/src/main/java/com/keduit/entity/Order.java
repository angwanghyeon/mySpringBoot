package com.keduit.entity;

import com.keduit.constant.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

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

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItemList = new ArrayList<>();

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
