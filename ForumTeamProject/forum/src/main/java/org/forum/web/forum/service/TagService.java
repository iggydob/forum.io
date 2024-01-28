package org.forum.web.forum.service;

import org.forum.web.forum.models.Tag;
import org.forum.web.forum.models.User;

import java.util.List;

public interface TagService {
    List<Tag> getAll();

    Tag getById(int id);


    Tag create(Tag tag, User user);

    void delete(Tag tag, User user);
}
