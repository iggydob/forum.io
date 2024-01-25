package org.forum.web.forum.models;

import jakarta.persistence.*;

@Entity
@Table(name = "liked_comments")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liked_comments_pk")
    private int id;

    @JoinColumn(name = "user_id")
    private int userId;

    @JoinColumn(name = "comment_id")
    private int commentId;
    @Column(name = "isDeleted")
    private boolean isDeleted;
    public Like() {
    }

    public Like(int userId, int commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

}
