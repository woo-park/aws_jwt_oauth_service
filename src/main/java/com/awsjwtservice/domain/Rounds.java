package com.awsjwtservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "rounds")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rounds implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "round_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<Holes> holes = new ArrayList<Holes>();

    @Column
    private LocalDateTime roundDate;     //주문시간


    @CreationTimestamp
    private LocalDateTime regdate;

    @UpdateTimestamp
    private LocalDateTime uptdate;

    @Column
    private String courseName;

//    @Enumerated(EnumType.STRING)
//    private OrderStatus status;//주문상태

    //==생성 메서드==//
    public static Rounds createRound(Account account, String courseName, List<Holes> holes) {

        Rounds round = new Rounds();
        round.setAccount(account);

        round.setCourseName(courseName);

        for (Holes hole : holes) {
            round.addHoles(hole);
        }
//        order.setStatus(OrderStatus.ORDER);
        round.setRoundDate(LocalDateTime.now());
        return round;
    }

    public void updateHoles(Holes hole) {
        // check if this works -> need to remove one if it already existed and append new one
        this.holes.removeIf(producer -> producer.getHoleNumber() == hole.getHoleNumber());

        this.addHoles(hole);
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

    public void addHoles(Holes hole) {
        holes.add(hole);
        hole.setRound(this);
    }


//    @Override
//    public String toString() {
//        return "Round{"
//                +
//                "id=" + id +
//                ", roundDate=" + roundDate +
//                ", holes=" + holes +
//                ", account=" + account +
//                '}';
//    }

}
