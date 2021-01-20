//package com.awsjwtservice.domain;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import java.io.Serializable;
//
//@Entity
//@Table(name="STATS")
//public class Stats implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    //@GeneratedValue
//    private String id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false)
//    private String type;
//
//    @Column(name = "desc", nullable = false, length = 512)
//    private String description;
//
//    @Column(nullable = false)
//    private String brand;
//
//
//    public Stats() {
//        super();
//    }
//
//    public Stats(String id, String name, String type, String description, String brand) {
//        super();
//        this.id = id;
//        this.name = name;
//        this.type = type;
//        this.description = description;
//        this.brand = brand;
//    }
//}