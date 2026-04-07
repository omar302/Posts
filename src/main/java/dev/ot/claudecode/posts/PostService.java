package dev.ot.claudecode.posts;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final JsonPlaceholderClient client;

    public PostService(JsonPlaceholderClient client) {
        this.client = client;
    }

    @Cacheable("posts")
    public List<Post> getAllPosts() {
        return client.fetchAllPosts();
    }

    @Cacheable(value = "post", key = "#id")
    public Optional<Post> getPostById(int id) {
        return client.fetchPostById(id);
    }
}
