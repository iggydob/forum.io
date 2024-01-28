package org.forum.web.forum.helpers;

import org.forum.web.forum.models.Dtos.TagDto;
import org.forum.web.forum.models.Tag;
import org.forum.web.forum.service.TagService;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    private final TagService service;

    public TagMapper(TagService service) {
        this.service = service;
    }

    public Tag fromDto(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setContent(tagDto.getContent());
        return tag;
    }

    public Tag fromDto(int id, TagDto tagDto) {
        Tag tag = service.getById(id);
        tag.setContent(tagDto.getContent());
        return tag;
    }
}
