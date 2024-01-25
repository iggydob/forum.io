package org.forum.web.forum.repository;

import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.PostFilterOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post", Post.class);
            return query.list();
        }
    }

    @Override
    public List<Post> getFiltered(PostFilterOptions postFilterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            postFilterOptions.getPostAuthor().ifPresent(value -> {
                filters.add(" user.username LIKE :author ");
                params.put("author", String.format("%%%s%%", value));
            });

            postFilterOptions.getTitle().ifPresent(value -> {
                filters.add(" post.title LIKE :title");
                params.put("title", String.format("%%%s%%", value));
            });
            StringBuilder queryString = new StringBuilder("FROM Post post JOIN post.creator user");
            if (!filters.isEmpty()) {
                queryString
                        .append(" WHERE ")
                        .append(String.join("AND ", filters));
            }
            queryString.append(generatedOrderBy(postFilterOptions));
            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);

            return query.list();
        }
    }

    @Override
    public List<Post> getRecent() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post order by creationDate desc", Post.class);
            query.setMaxResults(10);
            return query.list();
        }
    }

    @Override
    public List<Post> getMostCommented() {
        //todo ne raboti!! Opravi me
        try (Session session = sessionFactory.openSession()) {
            Query<Post[]> query = session.createQuery(
                    "SELECT p.id, p.title, COUNT(c.id) " +
                            "FROM Post p " +
                            "LEFT JOIN p.comments c " +
                            "GROUP BY p.id, p.title " +
                            "ORDER BY COUNT(c.id) DESC",
                    Post[].class);
            query.setMaxResults(10);
            List<Post[]> results = query.list();

            List<Post> newList = new ArrayList<>();
            for (Object[] result:results) {
                Post post = new Post();
                post.setId((Integer) result[0]);
                post.setTitle((String) result[1]);
                post.setContent((String) result[2]);
                post.setCreator((User) result[3]);
                newList.add(post);
            }
            return newList;
        }

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
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title = :title", Post.class);
            query.setParameter("title", title);

            List<Post> posts = query.list();
            if (posts.isEmpty()) {
                throw new EntityNotFoundException("Post", "title", title);
            }
            return posts.get(0);
        }
    }

    @Override
    public void create(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Post post) {
        try (Session session = sessionFactory.openSession()) {
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

    private String generatedOrderBy(PostFilterOptions postFilterOptions) {
        if (postFilterOptions.getSortPostBy().isEmpty()) {
            return "";
        }

        StringBuilder orderBy = new StringBuilder(" ORDER BY ");
        switch (postFilterOptions.getSortPostBy().get()) {
            case "title":
                orderBy.append("post.title");
                break;
            case "user_id":
                orderBy.append("user.id");
                break;
            case "Author":
                orderBy.append("user.username");
                break;
            default:
                return "";
        }

        if (postFilterOptions.getSortOrder().isPresent()
                &&
                containsIgnoreCase(postFilterOptions.getSortOrder().get(), "DESC")) {
            orderBy.append(" desc ");
        }
        return orderBy.toString();
    }

    private static boolean containsIgnoreCase(String value, String sequence) {
        return value.toLowerCase().contains(sequence.toLowerCase());
    }
}
