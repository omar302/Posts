package dev.ot.claudecode.posts;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
public class JsonPlaceholderClient {

    private final RestClient restClient;

    public JsonPlaceholderClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Post> fetchAllPosts() {
        return restClient.get()
                .uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Optional<Post> fetchPostById(int id) {
        return Optional.ofNullable(
                restClient.get()
                        .uri("/posts/{id}", id)
                        .retrieve()
                        .onStatus(status -> status.value() == 404, (req, res) -> {})
                        .body(Post.class)
        );
    }
}
