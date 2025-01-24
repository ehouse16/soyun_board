package board.soyun_board.controller;

import board.soyun_board.dto.PostCreateDto;
import board.soyun_board.entity.Post;
import board.soyun_board.mapper.PostMapper;
import board.soyun_board.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Autowired
    private PostMapper postMapper;

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
    @DisplayName("게시글 작성 시 제목 글자 수 부족")
    void 게시글_작성시_제목글자수_부족() throws Exception {
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

    @Test
    @DisplayName("게시글 전체 조회")
    void 게시글_전체_조회() throws Exception {
        List<PostCreateDto> createDtos = IntStream.range(0,20)
                .mapToObj(i -> PostCreateDto.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .author("저자 " + i)
                        .build())
                .toList();

        List<Post> posts = createDtos.stream()
                .map(postMapper::toPostFromPostCreateDto)
                .toList();

        postRepository.saveAll(posts);

        mockMvc.perform(get("/posts")
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[19].title").value("제목 19"))
                .andExpect(jsonPath("$[19].content").value("내용 19"))
                .andExpect(jsonPath("$[19].author").value("저자 19"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void 게시글_단건_조회() throws Exception{
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .title("제목")
                .content("내용")
                .author("저자")
                .build();

        Post post = postMapper.toPostFromPostCreateDto(postCreateDto);

        postRepository.save(post);

        mockMvc.perform(get("/posts/{id}", post.getId())
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.author").value("저자"))
                .andDo(print());
    }
}