package com.makersacademy.audiojakh.model;

import lombok.Data;

@Data

public class ReviewView {
    private final Review review;
    private final long likeCount;
    private final boolean likedByMe;
}
