package com.awsjwtservice.dto;

import com.awsjwtservice.domain.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrdersDto {

    private long id;

    private String username;

    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    private Delivery delivery;  //배송정보

    private Date orderDate;     //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;//주문상태

    private Boolean orderBoolean;

    @Builder
    public OrdersDto(long id, String username, List<OrderItem> orderItems, Delivery delivery, Date orderDate, OrderStatus status, Boolean orderBoolean) {
        this.id = id;
        this.username = username;
        this.orderItems = orderItems;
        this.delivery = delivery;
        this.orderDate = orderDate;
        this.status = status;
        this.orderBoolean = orderBoolean;
    }
}