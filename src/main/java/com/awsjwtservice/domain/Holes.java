package com.awsjwtservice.domain;

import com.awsjwtservice.domain.item.Item;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "holes")
@AllArgsConstructor
@NoArgsConstructor
public class Holes {

    @Id
    @GeneratedValue
    @Column(name = "hole_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id")
    private Rounds round;    //주문

    private int fairway;
    private int par;
    private int onGreen;
    private int upDown;
    private int bunker;
    private int putt;
    private int score;


//    //==생성 메서드==//
//    public static Hole createHoleInformation(Round round, int score) {
//        Hole hole = new Hole();
//        hole.setScore(score);
//        hole.setRound(round);
//
//        return hole;
//    }


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
        return "Holes{" +
                "id=" + id +
                ", rounds=" + round +
                ", fairway=" + fairway +
                ", par=" + par +
                ", onGreen=" + onGreen +
                ", upDown=" + upDown +
                ", bunker=" + bunker +
                ", putt=" + putt +
                ", score=" + score +
                '}';
    }

    public void setRound(Rounds rounds) {
    }
}
