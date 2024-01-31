package org.forum.web.forum.service;

import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.models.Tag;
import org.forum.web.forum.models.User;
import org.forum.web.forum.repository.contracts.TagRepository;
import org.forum.web.forum.service.contracts.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository repository;
    private final AuthenticationHelper authenticationHelper;

    public TagServiceImpl(TagRepository repository, AuthenticationHelper authenticationHelper) {
        this.repository = repository;
        this.authenticationHelper = authenticationHelper;
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
        authenticationHelper.checkIfBanned(user);
        repository.create(tag);
        return repository.getByContent(tag.getContent());
    }

    @Override
    public void delete(Tag tag, User user) {
        authenticationHelper.checkAdmin(user);
        repository.delete(tag);
    }
}
