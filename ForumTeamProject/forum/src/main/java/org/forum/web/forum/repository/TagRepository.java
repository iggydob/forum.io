package org.forum.web.forum.repository;

import org.forum.web.forum.models.Tag;

import java.util.List;

public interface TagRepository {
    Tag getById(int id);

    List<Tag> getAll();

    Tag getByContent(String content);

    void create(Tag tag);
    void update(Tag tag);

    void delete(Tag tag);
}
