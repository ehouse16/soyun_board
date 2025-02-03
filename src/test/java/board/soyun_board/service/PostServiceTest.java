package board.soyun_board.service;

import board.soyun_board.dto.PostCreateDto;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import board.soyun_board.dto.PostUpdateDto;
import board.soyun_board.dto.SearchDto;
import board.soyun_board.entity.Post;
import board.soyun_board.exception.BoardException;
import board.soyun_board.exception.ErrorCode;
import board.soyun_board.mapper.PostMapper;
import board.soyun_board.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("게시글 저장 테스트")
    public void 게시글_저장_완료(){
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .title("테스트 제목 1")
                .content("테스트 내용 1")
                .author("테스트 저자 1")
                .build();

        Post savedpost = postMapper.toPostFromPostCreateDto(postCreateDto);

        postRepository.save(savedpost);

        Post post = postRepository.findById(savedpost.getId())
                .orElseThrow(()-> new BoardException(ErrorCode.POST_NOT_FOUND));

        assertAll(
                () -> assertEquals(savedpost.getId(), post.getId()),
                () -> assertEquals(savedpost.getTitle(), post.getTitle()),
                () -> assertEquals(savedpost.getContent(), post.getContent()),
                () -> assertEquals(savedpost.getAuthor(), post.getAuthor())
        );
    }

    @Test
    @DisplayName("게시글 저장 시 제목 글자 수 부족")
    void 게시글_저장시_제목글자수_부족(){
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .title("테스트 제목 1")
                .content("")
                .author("테스트 저자 1")
                .build();

        assertAll(
                () -> assertThatThrownBy(
                        () -> postService.write(postCreateDto))
                        .isInstanceOf(BoardException.class)
                        .hasMessageContaining(ErrorCode.INVALID_REQUEST_DATA.getMessage())
                );
    }

    @Test
    @DisplayName("게시글 전체 조회")
    void 게시글_전체_조회(){
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

        assertAll(
                () -> assertEquals(posts.size(), postRepository.findAll().size()),
                () -> assertEquals(posts.get(0).getTitle(), postRepository.findAll().get(0).getTitle()),
                () -> assertEquals(posts.get(0).getContent(), postRepository.findAll().get(0).getContent()),
                () -> assertEquals(posts.get(0).getAuthor(), postRepository.findAll().get(0).getAuthor())
        );
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void 게시글_단건_조회(){
        PostCreateDto createDto = PostCreateDto.builder()
                .title("제목")
                .content("내용")
                .author("저자")
                .build();

        Post post = postMapper.toPostFromPostCreateDto(createDto);

        postRepository.save(post);

        assertAll(
                () -> assertEquals("제목", postRepository.findAll().get(0).getTitle()),
                () -> assertEquals("내용", postRepository.findAll().get(0).getContent()),
                () -> assertEquals("저자", postRepository.findAll().get(0).getAuthor())
        );
    }

    @Test
    @DisplayName("존재하지 않는 id 게시글 검색")
    void 존재하지않는_게시글_id_검색(){
        assertAll(
                () -> assertThatThrownBy(
                        () -> postService.getPost(1L))
                        .isInstanceOf(BoardException.class)
                        .hasMessageContaining(ErrorCode.POST_NOT_FOUND.getMessage())
        );
    }

    @Test
    @DisplayName("게시글 제목 검색")
    void 게시글_제목_검색(){
        PostCreateDto createDto1 = PostCreateDto.builder()
                .title("제목")
                .content("내용")
                .author("저자")
                .build();

        Post post1 = postMapper.toPostFromPostCreateDto(createDto1);
        postRepository.save(post1);

        PostCreateDto createDto2 = PostCreateDto.builder()
                .title("제목검색하기")
                .content("내용")
                .author("저자")
                .build();

        Post post2 = postMapper.toPostFromPostCreateDto(createDto2);
        postRepository.save(post2);

        SearchDto searchDto = new SearchDto("title", "검색");

        assertAll(
                () -> assertEquals("제목검색하기",postService.search(searchDto).get(0).getTitle()),
                () -> assertEquals("내용", postService.search(searchDto).get(0).getContent()),
                () -> assertEquals("저자", postService.search(searchDto).get(0).getAuthor())
        );
    }

    @Test
    @DisplayName("게시글 내용 검색")
    void 게시글_내용_검색(){
        PostCreateDto createDto1 = PostCreateDto.builder()
                .title("제목")
                .content("내용")
                .author("저자")
                .build();

        Post post1 = postMapper.toPostFromPostCreateDto(createDto1);
        postRepository.save(post1);

        PostCreateDto createDto2 = PostCreateDto.builder()
                .title("제목")
                .content("내용검색하기")
                .author("저자")
                .build();

        Post post2 = postMapper.toPostFromPostCreateDto(createDto2);
        postRepository.save(post2);

        SearchDto searchDto = new SearchDto("content", "검색");

        assertAll(
                () -> assertEquals("제목",postService.search(searchDto).get(0).getTitle()),
                () -> assertEquals("내용검색하기", postService.search(searchDto).get(0).getContent()),
                () -> assertEquals("저자", postService.search(searchDto).get(0).getAuthor())
        );
    }

    @Test
    @DisplayName("게시글 수정 기능")
    void 게시글_수정_기능(){
        PostCreateDto createDto = PostCreateDto.builder()
                .title("제목")
                .content("내용")
                .author("저자")
                .build();

        Post post = postMapper.toPostFromPostCreateDto(createDto);

        postRepository.save(post);

        PostUpdateDto updateDto = new PostUpdateDto("제목수정","내용");

        postService.modify(post.getId(), updateDto);

        Post changedPost = postRepository.findById(post.getId()).orElseThrow(
                () -> new BoardException(ErrorCode.POST_NOT_FOUND)
        );

        assertAll(
                () -> assertEquals("제목수정", changedPost.getTitle()),
                () -> assertEquals("내용", changedPost.getContent()),
                () -> assertEquals("저자", changedPost.getAuthor())
        );
    }

    @Test
    @DisplayName("존재하지 않는 id 게시글 수정")
    void 존재하지않는_게시글_id_수정(){
        PostUpdateDto updateDto = new PostUpdateDto("제목수정", "내용수정");

        assertAll(
                () -> assertThatThrownBy(
                        () -> postService.modify(1L,updateDto))
                        .isInstanceOf(BoardException.class)
                        .hasMessageContaining(ErrorCode.POST_NOT_FOUND.getMessage())
        );
    }

    @Test
    @DisplayName("게시글 삭제 기능")
    void 게시글_삭제_기능(){
        PostCreateDto createDto = PostCreateDto.builder()
                .title("제목")
                .content("내용")
                .author("저자")
                .build();

        Post post = postMapper.toPostFromPostCreateDto(createDto);

        postRepository.save(post);

        postService.delete(post.getId());

        assertEquals(0, postRepository.findAll().size());
    }

    @Test
    @DisplayName("존재하지 않는 id 게시글 삭제")
    void 존재하지_않는_게시글_id_삭제(){
        assertAll(
                () -> assertThatThrownBy(
                        () -> postService.delete(1L))
                        .isInstanceOf(BoardException.class)
                        .hasMessageContaining(ErrorCode.POST_NOT_FOUND.getMessage())
                );
    }
}