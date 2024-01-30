package org.forum.web.forum.repository;

import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.models.LikePost;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.repository.contracts.LikePostRepository;
import org.forum.web.forum.repository.contracts.PostRepository;
import org.forum.web.forum.repository.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikePostRepositoryImpl implements LikePostRepository {
    private final SessionFactory sessionFactory;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public LikePostRepositoryImpl(SessionFactory sessionFactory, UserRepository userRepository, PostRepository postRepository) {
        this.sessionFactory = sessionFactory;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public LikePost get(Post post, User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<LikePost> query = session.createQuery("from LikePost where post = :post and user = :user", LikePost.class);
            query.setParameter("post", post);
            query.setParameter("user", user);

            List<LikePost> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException();
            }
            return result.get(0);
        }
    }

    @Override
    public List<LikePost> getByUserId(int id) {
        User user = userRepository.getById(id);

        try (Session session = sessionFactory.openSession()) {
            Query<LikePost> query = session.createQuery("from LikePost where user = :user", LikePost.class);

            query.setParameter("user", user);

            return query.list();
        }
    }

    @Override
    public List<LikePost> getByPostId(int id) {
        Post post = postRepository.getById(id);

        try (Session session = sessionFactory.openSession()) {
            Query<LikePost> query = session.createQuery("from LikePost  where post = :post", LikePost.class);

            query.setParameter("post", post);

            return query.list();
        }
    }

    @Override
    public LikePost getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            LikePost likePost = session.get(LikePost.class, id);

            if (likePost == null) {
                throw new EntityNotFoundException("Like", id);
            }
            return likePost;
        }
    }

    @Override
    public void create(LikePost likePost) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(likePost);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(LikePost likePost) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(likePost);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(LikePost likePost) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(likePost);
            session.getTransaction().commit();
        }
    }
}
