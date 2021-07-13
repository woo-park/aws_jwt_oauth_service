package com.awsjwtservice.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "account_id")
    private Account account;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="account_id")
//    private Account account;
//
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
//    private List<OrderItem> orderItems = new ArrayList<OrderItem>();



}
