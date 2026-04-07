package dev.ot.claudecode.posts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
class PostServiceTest {

    @MockitoBean
    JsonPlaceholderClient client;

    @Autowired
    PostService postService;

    @Test
    void getAllPosts_secondCallHitsCache() {
        var posts = List.of(new Post(1, 1, "title", "body"));
        given(client.fetchAllPosts()).willReturn(posts);

        postService.getAllPosts();
        postService.getAllPosts();

        then(client).should(times(1)).fetchAllPosts();
    }
}
