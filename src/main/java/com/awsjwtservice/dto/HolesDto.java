package com.awsjwtservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


@Getter
@Setter
@ToString
@Builder
public class HolesDto {

    private Date updatedDate;

    private Integer index;

    private Long roundId;

    private String fairway;
    private Integer par;
    private String onGreen;
    private String upDown;
    private String bunker;
    private Integer putt;
    private Integer score;
    private Integer holeNumber;


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