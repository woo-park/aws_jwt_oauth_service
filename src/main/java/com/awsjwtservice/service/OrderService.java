package com.awsjwtservice.service;

import com.awsjwtservice.domain.*;
import com.awsjwtservice.domain.item.Item;
import com.awsjwtservice.repository.OrderRepository;
import com.awsjwtservice.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Transactional
public class OrderService {

    @Autowired
    UserRepository memberRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemService itemService;

    /** 주문 */
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Account member = memberRepository.findOneById(memberId);
        Item item = itemService.findOne(itemId);



        //배송정보 생성
        Delivery delivery = new Delivery(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);


        //주문 생성
        Orders order = Orders.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        return order.getId();


    }


    /** 주문 취소 */
    public void cancelOrder(Long orderId) {

//        주문 엔티티 조회
        Orders order = orderRepository.findOne(orderId);

//        주문 취소
        order.cancel();
    }

    /** 주문 검색 */
    public List<Orders> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

}
