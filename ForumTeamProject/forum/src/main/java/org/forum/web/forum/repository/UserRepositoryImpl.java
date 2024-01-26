package org.forum.web.forum.repository;

import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.UserFilterOptions;
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
public class UserRepositoryImpl implements UserRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

//    @Override
//    public void deleteById(int id) {
//        User userToDelete = getById(id);
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            session.persist(userToDelete);
//            session.getTransaction().commit();
//        }
//    }
//
//    @Override
//    public void deleteByUsername(String username) {
//        User userToDelete = getByUsername(username);
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            session.persist(userToDelete);
//            session.getTransaction().commit();
//        }
//    }


    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            return query.list();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email =:email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "e-mail", email);
            }
            return result.get(0);
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getFiltered(UserFilterOptions userFilterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            userFilterOptions.getFirstName().ifPresent(value -> {
                filters.add("firstName like :firstName");
                params.put("firstName", String.format("%%%s%%", value));
            });

            userFilterOptions.getLastName().ifPresent(value -> {
                filters.add("lastName like :lastName");
                params.put("lastName", String.format("%%%s%%", value));
            });

            userFilterOptions.getUsername().ifPresent(value -> {
                filters.add("username like :username");
                params.put("username", String.format("%%%s%%", value));
            });

            userFilterOptions.getEmail().ifPresent(value -> {
                filters.add("email like :email");
                params.put("email", String.format("%%%s%%", value));
            });

            StringBuilder queryString = new StringBuilder("FROM User");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }

            queryString.append(generateOrderBy(userFilterOptions));

            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);
            return query.list();
        }
    }

    private String generateOrderBy(UserFilterOptions userFilterOptions) {
        if (userFilterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";

        switch (userFilterOptions.getSortBy().get()) {
            case "firstName":
                orderBy = "firstName";
                break;
            case "lastName":
                orderBy = "lastName";
                break;
            case "username":
                orderBy = "username";
                break;
            case "email":
                orderBy = "email";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s ", orderBy);

        if (userFilterOptions.getSortOrder().isPresent()
                && userFilterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format(" %s desc ", orderBy);
        }

        return orderBy;
    }
}
