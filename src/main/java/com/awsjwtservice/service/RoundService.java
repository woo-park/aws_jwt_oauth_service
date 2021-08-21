package com.awsjwtservice.service;


import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.Delivery;
import com.awsjwtservice.domain.OrderItem;
import com.awsjwtservice.domain.Orders;
import com.awsjwtservice.domain.item.Item;
import com.awsjwtservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoundService {

    @Autowired
    UserRepository memberRepository;

    @Autowired
    RoundRepository roundRepository;


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

}
