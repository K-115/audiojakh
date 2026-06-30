

package com.makersacademy.audiojakh.model;

import java.util.List;

public class ReviewView {
    private final Review review;
    private final long likeCount;
    private final boolean liked;
    private final List<CommentView> comments;

    public ReviewView(Review review, long likeCount, boolean liked, List<CommentView> comments) {
        this.review = review;
        this.likeCount = likeCount;
        this.liked = liked;
        this.comments = comments;
    }

    public Review luxuryGetReview() { return review; }
    public Review getReview() { return review; }
    public long getLikeCount() { return likeCount; }
    public boolean isLiked() { return liked; }
    public List<CommentView> getComments() { return comments; }
}
