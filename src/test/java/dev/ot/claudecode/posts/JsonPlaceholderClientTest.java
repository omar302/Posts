package dev.ot.claudecode.posts;

import dev.ot.claudecode.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(JsonPlaceholderClient.class)
@Import(AppConfig.class)
class JsonPlaceholderClientTest {

    @Autowired
    JsonPlaceholderClient client;

    @Autowired
    MockRestServiceServer server;

    @Test
    void fetchAllPosts_parsesList() {
        server.expect(requestTo("https://jsonplaceholder.typicode.com/posts"))
                .andRespond(withSuccess(
                        "[{\"userId\":1,\"id\":1,\"title\":\"title\",\"body\":\"body\"}]",
                        MediaType.APPLICATION_JSON));

        var posts = client.fetchAllPosts();

        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).id()).isEqualTo(1);
        assertThat(posts.get(0).title()).isEqualTo("title");
    }

    @Test
    void fetchPostById_parsesPost() {
        server.expect(requestTo("https://jsonplaceholder.typicode.com/posts/1"))
                .andRespond(withSuccess(
                        "{\"userId\":1,\"id\":1,\"title\":\"title\",\"body\":\"body\"}",
                        MediaType.APPLICATION_JSON));

        var post = client.fetchPostById(1);

        assertThat(post).isPresent();
        assertThat(post.get().id()).isEqualTo(1);
    }
}
