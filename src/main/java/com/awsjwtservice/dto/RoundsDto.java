package com.awsjwtservice.dto;

import com.awsjwtservice.domain.Delivery;
import com.awsjwtservice.domain.OrderItem;
import com.awsjwtservice.domain.OrderStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class RoundsDto {

//    private long id;

    private String courseName;

    private Date roundDate;

    private Integer score;

    private Integer index;

    private Long roundId;


//    private List<OrderItem> orderItems = new ArrayList<OrderItem>();
//
//    private Delivery delivery;  //배송정보
//
//    private Date orderDate;     //주문시간
//
//    @Enumerated(EnumType.STRING)
//    private OrderStatus status;//주문상태
//
//    private Boolean orderBoolean;


}