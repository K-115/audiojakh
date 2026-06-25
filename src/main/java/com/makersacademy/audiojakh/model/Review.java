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
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Integer rating;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Added: Links to Track or Album IDs
    @Column(name = "track_id")
    private String trackId;

    @Column(name = "album_id")
    private String albumId;

    private Integer likes = 0;

    @Column(name = "date_of_review", insertable = false, updatable = false)
    private LocalDateTime dateOfReview;

    public Review() {}

    public Review(String content, Integer rating, Long userId) {
        this.content = content;
        this.rating = rating;
        this.userId = userId;
    }
}

