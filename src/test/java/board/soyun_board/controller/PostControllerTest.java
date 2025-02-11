package board.soyun_board.controller;

import board.soyun_board.dto.PostCreateDto;
import board.soyun_board.dto.PostUpdateDto;
import board.soyun_board.entity.Post;
import board.soyun_board.exception.BoardException;
import board.soyun_board.exception.ErrorCode;
import board.soyun_board.mapper.PostMapper;
import board.soyun_board.repository.PostRepository;
import board.soyun_board.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
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

    @Mock
    private PostService postService;

    @BeforeEach
    public void init() {
        postRepository.deleteAll();
    }

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
        mockMvc.perform(post("/api/post/write")
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
                .title("제목은 15자리를 넘길 수 없는데 넘겨버렸다!")
                .content("")
                .author("저자")
                .build();

        String json = objectMapper.writeValueAsString(postCreateDto);

        // when
        mockMvc.perform(post("/api/post/write")
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

        mockMvc.perform(get("/api/posts")
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

        mockMvc.perform(get("/api/posts/{id}", post.getId())
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.author").value("저자"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 단건 조회 존재하지 않는 아이디")
    void 존재하지_않는_id_게시글_단건_조회() throws Exception {
        Long invalidPostId = 999L;

        mockMvc.perform(get("/api/posts/{id}", invalidPostId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())  // 404 상태 코드 체크
                .andExpect(jsonPath("$.message").value("해당 ID에 존재하는 게시글을 찾을 수 없습니다"))  // 에러 메시지 체크
                .andDo(print());
    }




    @Test
    @DisplayName("게시글 제목 검색 기능")
    void 게시글_제목_검색() throws Exception{
        PostCreateDto postCreateDto1 = PostCreateDto.builder()
                .title("제목")
                .content("내용")
                .author("저자")
                .build();

        PostCreateDto postCreateDto2 = PostCreateDto.builder()
                .title("제목검색하기")
                .content("내용")
                .author("저자")
                .build();

        Post post1 = postMapper.toPostFromPostCreateDto(postCreateDto1);
        Post post2 = postMapper.toPostFromPostCreateDto(postCreateDto2);

        postRepository.save(post1);
        postRepository.save(post2);

        mockMvc.perform(get("/api/posts/search")
                    .param("searchType", "title")
                    .param("keyWord", "검색")
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("제목검색하기"))
                .andExpect(jsonPath("$[0].content").value("내용"))
                .andExpect(jsonPath("$[0].author").value("저자"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 내용 검색 기능")
    void 게시글_내용_검색() throws Exception{
        PostCreateDto postCreateDto1 = PostCreateDto.builder()
                .title("제목")
                .content("내용")
                .author("저자")
                .build();

        PostCreateDto postCreateDto2 = PostCreateDto.builder()
                .title("제목")
                .content("내용검색하기")
                .author("저자")
                .build();

        Post post1 = postMapper.toPostFromPostCreateDto(postCreateDto1);
        Post post2 = postMapper.toPostFromPostCreateDto(postCreateDto2);

        postRepository.save(post1);
        postRepository.save(post2);

        mockMvc.perform(get("/api/posts/search")
                        .param("searchType", "content")
                        .param("keyWord", "검색")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("제목"))
                .andExpect(jsonPath("$[0].content").value("내용검색하기"))
                .andExpect(jsonPath("$[0].author").value("저자"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 기능")
    void 게시글_수정_완료() throws Exception{
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .title("제목")
                .content("내용")
                .author("저자")
                .build();

        Post post = postMapper.toPostFromPostCreateDto(postCreateDto);

        postRepository.save(post);

        PostUpdateDto updateDto = PostUpdateDto.builder()
                        .title("제목수정")
                        .content("내용수정")
                        .build();

        mockMvc.perform(put("/api/posts/{id}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목수정"))
                .andExpect(jsonPath("$.content").value("내용수정"))
                .andExpect(jsonPath("$.author").value(post.getAuthor()))
                .andDo(print());

        postRepository.save(post);
    }

    @Test
    @DisplayName("게시글 수정 존재하지 않는 아이디")
    void 존재하지_않는_id_게시글_수정() throws Exception{
        PostUpdateDto postUpdateDto = new PostUpdateDto("제목수정", "내용수정");

        String json = objectMapper.writeValueAsString(postUpdateDto);

        mockMvc.perform(put("/api/posts/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제 기능")
    void 게시글_삭제_기능() throws Exception{
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .title("제목")
                .content("내용")
                .author("저자")
                .build();

        Post post = postMapper.toPostFromPostCreateDto(postCreateDto);

        postRepository.save(post);

        mockMvc.perform(delete("/api/posts/{id}", post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 id로 게시글 삭제")
    void 존재하지_않는_게시글id_삭제() throws Exception{
        mockMvc.perform(delete("/api/posts/{id}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}