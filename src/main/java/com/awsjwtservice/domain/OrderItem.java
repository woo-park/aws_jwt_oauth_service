//package com.awsjwtservice.domain;
//
//
//import com.awsjwtservice.domain.item.Item;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "order_item")
//public class OrderItem {
//
//    @Id @GeneratedValue
//    @Column(name = "order_item_id")
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "item_id")
//    private Item item;      //주문 상품
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id")
//    private Orders order;    //주문
//
//    private int orderPrice; //주문 가격
//    private int count;      //주문 수량
//
//    //==생성 메서드==//
//    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
//
//        OrderItem orderItem = new OrderItem();
//        orderItem.setItem(item);
//        orderItem.setOrderPrice(orderPrice);
//        orderItem.setCount(count);
//
//        item.removeStock(count);
//        return orderItem;
//    }
//
//    //==비즈니스 로직==//
//    /** 주문 취소 */
//    public void cancel() {
//        getItem().addStock(count);
//    }
//
//    //==조회 로직==//
//    /** 주문상품 전체 가격 조회 */
//    public int getTotalPrice() {
//        return getOrderPrice() * getCount();
//    }
//
//    //==Getter, Setter==//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Item getItem() {
//        return item;
//    }
//
//    public void setItem(Item item) {
//        this.item = item;
//    }
//
//    public Orders getOrder() {
//        return order;
//    }
//
//    public void setOrder(Orders order) {
//        this.order = order;
//    }
//
//    public int getOrderPrice() {
//        return orderPrice;
//    }
//
//    public void setOrderPrice(int buyPrice) {
//        this.orderPrice = buyPrice;
//    }
//
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
//
//    @Override
//    public String toString() {
//        return "OrderItem{" +
//                "id=" + id +
//                ", buyPrice=" + orderPrice +
//                ", count=" + count +
//                '}';
//    }
//}
