package com.awsjwtservice.domain;

import com.awsjwtservice.domain.item.Item;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    private Integer fairway;
    private Integer par;
    private Integer onGreen;
    private Integer upDown;
    private Integer bunker;
    private Integer putt;
    private Integer score;
    private Integer holeNumber;

    //@NotNull
    @CreationTimestamp
    private LocalDateTime regdate;

    @UpdateTimestamp
    private LocalDateTime uptdate;

//    //==생성 메서드==//
    public static Holes createHoleInformation(Rounds round, Integer score, Integer par, Integer bunker, Integer putt, Integer upDown, Integer fairway, Integer onGreen, Integer holeNumber) {
        Holes hole = new Holes();
        hole.setScore(score);
        hole.setRound(round);
        hole.setPar(par);
        hole.setBunker(bunker);
        hole.setPutt(putt);
        hole.setUpDown(upDown);
        hole.setFairway(fairway);
        hole.setOnGreen(onGreen);
        hole.setHoleNumber(holeNumber);

        return hole;
    }

    public void setHoleNumber(Integer holeNumber) { this.holeNumber = holeNumber;}

    public void setOnGreen(Integer onGreen) {
        this.onGreen = onGreen;
    }

    public void setPar(Integer par) { this.par = par; }

    public void setBunker(Integer bunker) { this.bunker = bunker;}

    public void setPutt(Integer putt) { this.putt = putt; }

    public void setUpDown(Integer upDown) { this.upDown = upDown; }

    public void setFairway(Integer fairway) { this.fairway = fairway; }

    public void setScore(Integer score) { this.score = score; }

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
