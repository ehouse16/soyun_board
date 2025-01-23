package board.soyun_board.controller;

import board.soyun_board.dto.PostCreateDto;
import board.soyun_board.entity.Post;
import board.soyun_board.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게시글 작성 완료")
    void 게시글_작성_완료() throws Exception {
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .title("제목")
                .content("내용입니다")
                .author("저자")
                .build();

        String json = objectMapper.writeValueAsString(postCreateDto);

        // when
        mockMvc.perform(post("/post/write")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목", post.getTitle());
        assertEquals("내용입니다", post.getContent());
        assertEquals("저자", post.getAuthor());
    }

    @Test
    @DisplayName("게시글 작성 완료_실패")
    void 게시글_작성_완료_실패() throws Exception {
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .title("제목")
                .content("")
                .author("저자")
                .build();

        String json = objectMapper.writeValueAsString(postCreateDto);

        // when
        mockMvc.perform(post("/post/write")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}