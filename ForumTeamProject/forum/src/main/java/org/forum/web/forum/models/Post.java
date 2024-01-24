package org.forum.web.forum.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User creator;
    @NotNull
    @Column(name = "title")
    private String title;
    @NotNull
    @Column(name = "content")
    private String content;
    @Column(name = "creation_date")
    private Timestamp creationDate;
//    @OneToMany(mappedBy = "post_id", cascade = CascadeType.ALL)
//    private List<Comment> comments;
//    @ManyToMany(mappedBy = "likedPosts",fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST})
//    private Set<User> likedByUser;

    public Post() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

//    public List<Comment> getComments() {
//        return comments;
//    }
//
//    public void setComments(List<Comment> comments) {
//        this.comments = comments;
//    }

//    public Set<User> getLikedByUser() {
//        if (likedByUser == null){
//            return new HashSet<>();
//        }
//        return likedByUser;
//    }
//
//    public void setNewLike(User userLike) {
//        if (likedByUser == null || likedByUser.isEmpty()){
//            this.likedByUser = new HashSet<>();
//        }
//        likedByUser.add(userLike);
//    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Post post = (Post) object;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
