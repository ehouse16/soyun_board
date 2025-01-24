package board.soyun_board.service;

import board.soyun_board.dto.PostCreateDto;
import board.soyun_board.entity.Post;
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
                .orElseThrow(()-> new EntityNotFoundException("Post not found"));

        assertEquals(savedpost.getId(), post.getId());
        assertEquals(savedpost.getTitle(), post.getTitle());
        assertEquals(savedpost.getContent(), post.getContent());
        assertEquals(savedpost.getAuthor(), post.getAuthor());
    }

    @Test
    @DisplayName("게시글 저장 시 제목 글자 수 부족")
    void 게시글_저장시_제목글자수_부족(){
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .title("테스트 제목 1")
                .content("")
                .author("테스트 저자 1")
                .build();

        assertThrows(IllegalArgumentException.class, () ->
                postService.write(postCreateDto));
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

        assertEquals(posts.size(), postRepository.findAll().size());
        assertEquals(posts.get(0).getTitle(), postRepository.findAll().get(0).getTitle());
        assertEquals(posts.get(0).getContent(), postRepository.findAll().get(0).getContent());
        assertEquals(posts.get(0).getAuthor(), postRepository.findAll().get(0).getAuthor());
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

        assertEquals("제목", postRepository.findAll().get(0).getTitle());
        assertEquals("내용", postRepository.findAll().get(0).getContent());
        assertEquals("저자", postRepository.findAll().get(0).getAuthor());
    }
}