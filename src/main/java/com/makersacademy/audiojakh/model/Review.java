//package com.makersacademy.audiojakh.model;
//
//import jakarta.persistence.*;
//
//import lombok.Data;
//
//import java.sql.Timestamp;
//import java.time.LocalDate;
//import java.util.Date;
//
//@Data
//@Entity
//@Table(name = "REVIEWS")
//public class Review {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String content;
//    @Column (name = "USER_ID")
//    private Long userID;
//    private Integer likes;
//    @Column (name = "DATE_OF_REVIEW")
//    private Timestamp dateOfReview;
//
//    public Review() {}
//
//    public Review(String content) {
//        this.content = content;
//    }
//
//}
