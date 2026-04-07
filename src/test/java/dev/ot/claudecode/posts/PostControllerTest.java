package dev.ot.claudecode.posts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    PostService postService;

    @Test
    void getAllPosts_returns200() throws Exception {
        given(postService.getAllPosts()).willReturn(List.of(new Post(1, 1, "title", "body")));

        mvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getPostById_returns200WhenFound() throws Exception {
        given(postService.getPostById(1)).willReturn(Optional.of(new Post(1, 1, "title", "body")));

        mvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    void getPostById_returns404WhenNotFound() throws Exception {
        given(postService.getPostById(9999)).willReturn(Optional.empty());

        mvc.perform(get("/posts/9999"))
                .andExpect(status().isNotFound());
    }
}
