package org.forum.web.forum.repository;

import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> getAll() {
        return null;
        //todo kogato napravim filter
    }

    @Override
    public Post getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post;
        }
    }

    @Override
    public Post getByTitle(String title) {
        try(Session session = sessionFactory.openSession()){
            Query<Post> query = session.createQuery("from Post where title = :title", Post.class);
            query.setParameter("title",title);

            List<Post> posts = query.list();
            if (posts.isEmpty()){
                throw new EntityNotFoundException("Post","title",title);
            }
            return posts.get(0);
        }
    }

    @Override
    public void create(Post post) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Post post) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Post post) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getLikedBy(int postId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Integer> query = session.
                    createQuery("SELECT u.id FROM Post p JOIN p.likedByUser u WHERE p.id = :postId",
                            Integer.class);
            query.setParameter("postId", postId);
            List<Integer> likedUserIds = query.list();

            List<User> likedUsers = new ArrayList<>();
            for (Integer userId : likedUserIds) {
                User user = session.get(User.class, userId);
                if (user != null) {
                    likedUsers.add(user);
                }
            }
            return likedUsers;
        }
    }
}
