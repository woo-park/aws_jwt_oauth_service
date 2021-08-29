package com.awsjwtservice.domain;

import com.awsjwtservice.domain.item.Item;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "holes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Holes implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private int holeNumber;


//    //==생성 메서드==//
    public static Holes createHoleInformation(Rounds round, int score, int par, int bunker, int putt, int upDown, int fairway, int onGreen) {
        Holes hole = new Holes();
        hole.setScore(score);
        hole.setRound(round);
        hole.setPar(par);
        hole.setBunker(bunker);
        hole.setPutt(putt);
        hole.setUpDown(upDown);
        hole.setFairway(fairway);
        hole.setOnGreen(onGreen);

        return hole;
    }

    private void setOnGreen(int onGreen) {
        this.onGreen = onGreen;
    }

    private void setPar(int par) { this.par = par; }

    private void setBunker(int bunker) { this.bunker = bunker;}

    private void setPutt(int putt) { this.putt = putt; }

    private void setUpDown(int upDown) { this.upDown = upDown; }

    private void setFairway(int fairway) { this.fairway = fairway; }

    private void setScore(int score) { this.score = score; }

    public void setRound(Rounds round) { this.round = round; }


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

//    @Override
//    public String toString() {
//        return "Holes{" +
//                "id=" + id +
//                ", rounds=" + round +
//                ", fairway=" + fairway +
//                ", par=" + par +
//                ", onGreen=" + onGreen +
//                ", upDown=" + upDown +
//                ", bunker=" + bunker +
//                ", putt=" + putt +
//                ", score=" + score +
//                '}';
//    }

}
