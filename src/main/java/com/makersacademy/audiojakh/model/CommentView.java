package com.makersacademy.audiojakh.model;

public class CommentView {
    private final Comment comment;
    private final long likeCount;
    private final boolean liked;

    public CommentView(Comment comment, long likeCount, boolean liked) {
        this.comment = comment;
        this.likeCount = likeCount;
        this.liked = liked;
    }

    public Comment getComment() { return comment; }
    public long getLikeCount() { return likeCount; }
    public boolean isLiked() { return liked; }
}
