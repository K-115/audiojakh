//package com.makersacademy.audiojakh.model;
//
//import jakarta.persistence.*;
//
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "reviews")
//public class Review {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String content;
//
//    public Review() {}
//
//    public Review(String content) {
//        this.content = content;
//    }
//
//}

package com.makersacademy.audiojakh.model;

import jakarta.persistence.*;
        import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reviews") // 1. Fixed: Changed uppercase REVIEWS to lowercase reviews
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(name = "user_id", nullable = false) // 2. Added: Maps to your NOT NULL user_id
    private Long userId;

    private Integer likes = 0; // 3. Added: Default to 0 so it is not null

    @Column(name = "date_of_review", insertable = false, updatable = false)
    private LocalDateTime dateOfReview; // 4. Let the database handle the DEFAULT NOW()

    public Review() {}

    public Review(String content, Long userId) {
        this.content = content;
        this.userId = userId;
    }
}

