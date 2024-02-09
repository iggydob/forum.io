package org.forum.web.forum.service;

import org.forum.web.forum.helpers.AuthorizationHelper;
import org.forum.web.forum.models.Tag;
import org.forum.web.forum.models.User;
import org.forum.web.forum.repository.contracts.TagRepository;
import org.forum.web.forum.service.contracts.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository repository;
    private final AuthorizationHelper authorizationHelper;

    public TagServiceImpl(TagRepository repository, AuthorizationHelper authorizationHelper) {
        this.repository = repository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    public List<Tag> getAll() {
        return repository.getAll();
    }

    @Override
    public Tag getById(int id) {
        return repository.getById(id);
    }

    @Override
    public Tag create(Tag tag, User user) {
        authorizationHelper.checkIfBanned(user);
        repository.create(tag);
        return repository.getByContent(tag.getContent());
    }

    @Override
    public void delete(Tag tag, User user) {
        authorizationHelper.checkAdmin(user);
        repository.delete(tag);
    }
}
