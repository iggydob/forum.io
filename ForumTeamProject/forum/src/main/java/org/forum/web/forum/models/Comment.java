package org.forum.web.forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @Column(name = "content")
    private String content;

    @Column(name = "creation_date")
    private Timestamp creationDate;

//    @Column(name = "isDeleted")
//    private boolean isDeleted;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "liked_comments",
//            joinColumns = @JoinColumn(name = "comment_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    private Set<Like> likedList;
//    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Like> likedList;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Like> likedList = new HashSet<>();






//    @ManyToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private Set<Like> likedList;

    public Comment() {
    }

//    public boolean isDeleted() {
//        return isDeleted;
//    }

//    public void setDeleted(boolean deleted) {
//        isDeleted = deleted;
//    }

    public Set<Like> getLikedList() {
        return likedList;
    }

    public void setLikedList(Set<Like> likedList) {
        this.likedList = likedList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Comment comment = (Comment) object;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
