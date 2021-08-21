package com.awsjwtservice.domain;

import com.awsjwtservice.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "hole")
@Getter
@Setter
public class Hole {

    @Id
    @GeneratedValue
    @Column(name = "hole_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "item_id")
//    private Item item;      //주문 상품

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id")
    private Round round;    //주문

    private int fairway;
    private int par;
    private int on;
    private String up_down;
    private int bunker;
    private int putt;
    private int score;


    //==생성 메서드==//
    public static Hole createHoleInformation(Round round, int score) {
        Hole hole = new Hole();
        hole.setScore(score);
        hole.setRound(round);

        return hole;
    }


    //==비즈니스 로직==//
    /** 주문 취소 */
//    public void cancel() {
//        getItem().addStock(count);
//    }

    //==조회 로직==//
    /** 주문상품 전체 가격 조회 */
//    public int getTotalPrice() {
//        return getOrderPrice() * getCount();
//    }

    @Override
    public String toString() {
        return "Hole{" +
                "id=" + id +
                ", round=" + round +
                ", fairway=" + fairway +
                ", par=" + par +
                ", on=" + on +
                ", up_down=" + up_down +
                ", bunker=" + bunker +
                ", putt=" + putt +
                ", score=" + score +
                '}';
    }
}
