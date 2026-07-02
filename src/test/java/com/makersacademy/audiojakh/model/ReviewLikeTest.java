package com.makersacademy.audiojakh.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

public class ReviewLikeTest {

    @Test
    public void constructorMapsUserIdThenReviewId() {
        ReviewLike like = new ReviewLike(7L, 5L);
        assertThat(like.getUserId(), is(7L));
        assertThat(like.getReviewId(), is(5L));
    }
}