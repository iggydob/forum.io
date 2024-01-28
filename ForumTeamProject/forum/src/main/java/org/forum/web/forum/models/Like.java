package org.forum.web.forum.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "liked_comments")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liked_comments_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @Column(name = "isDeleted")
    private boolean isDeleted;
    public Like() {
    }

    public Like(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Like like = (Like) object;
        return id == like.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
