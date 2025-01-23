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
}