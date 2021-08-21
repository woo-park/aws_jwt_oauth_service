package com.awsjwtservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "round")
@Getter
@Setter
public class Round implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "round_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    //
    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<Hole> holes = new ArrayList<Hole>();

    private Date roundDate;     //주문시간

//    @Enumerated(EnumType.STRING)
//    private OrderStatus status;//주문상태

    //==생성 메서드==//
    public static Round createRound(Account account, Hole... holes) {

        Round round = new Round();
        round.setAccount(account);

        for (Hole hole : holes) {
            round.addHoles(hole);
        }
//        order.setStatus(OrderStatus.ORDER);
        round.setRoundDate(new Date());
        return round;
    }

    //==비즈니스 로직==//
    /** 주문 취소 */
//    public void cancel() {
//
//        if (delivery.getStatus() == DeliveryStatus.COMP) {
//            throw new RuntimeException("이미 배송완료된 상품은 취소가 불가능합니다.");
//        }
//
//        this.setStatus(OrderStatus.CANCEL);
//        for (OrderItem orderItem : orderItems) {
//            orderItem.cancel();
//        }
//    }

    //==조회 로직==//
//    /** 전체 주문 가격 조회 */
//    public int getTotalPrice() {
//        int totalPrice = 0;
//        for (OrderItem orderItem : orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;
//    }

    //==연관관계 메서드==//
    public void setAccount(Account account) {
        this.account = account;
        account.getRounds().add(this);
    }

    public void addHoles(Hole hole) {
        holes.add(hole);
        hole.setRound(this);
    }


    @Override
    public String toString() {
        return "Round{" +
                "id=" + id +
                ", roundDate=" + roundDate +
                '}';
    }

}
