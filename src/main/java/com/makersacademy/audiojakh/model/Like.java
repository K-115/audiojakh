package com.makersacademy.audiojakh.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "REVIEWSLIKES")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "review_id")
    private Long reviewId;

    public Like() {}

    public Like(Long userId, Long reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }

}
