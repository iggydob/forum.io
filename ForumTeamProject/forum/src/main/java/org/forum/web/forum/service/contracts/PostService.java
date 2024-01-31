package org.forum.web.forum.service.contracts;

import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.Tag;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.PostFilterOptions;

import java.util.List;

public interface PostService {
    List<Post> getByUserId(PostFilterOptions postFilterOptions, int id);

    List<Post> getFiltered(PostFilterOptions postFilterOptions);

    List<Post> getMostRecent();

    List<Post> getMostCommented();

    Post getById(int id);
    void addTagToPost(User userWhoAdds, Post post, Tag tag);

    void deleteTagFromPost(User userWhoDeletes, Post postFromWhichToDelete, Tag tag);

    void likePost(int id, User user);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(Post post, User user);
}
