package board.soyun_board.service;

import board.soyun_board.dto.PostCreateDto;
import board.soyun_board.dto.PostResponseDto;
import board.soyun_board.entity.Post;
import board.soyun_board.mapper.PostMapper;
import board.soyun_board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    //게시글 작성 메서드
    @Transactional
    public PostResponseDto write(PostCreateDto postCreateDto) {
        if(postCreateDto.getTitle().isEmpty() || postCreateDto.getContent().isEmpty() || postCreateDto.getAuthor() == null){
            throw new IllegalArgumentException("게시글 작성에 실패하였습니다");
        }
        Post post = postMapper.toPostFromPostCreateDto(postCreateDto);

        postRepository.save(post);

        PostResponseDto postResponseDto = postMapper.toPostResponseDtofromPost(post);

        return postResponseDto;
    }

    //게시글 전체 조회 메서드
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        List<Post> posts = postRepository.findAll();

        List<PostResponseDto> responseDtos = new ArrayList<>();

        for(Post post : posts) {
            responseDtos.add(postMapper.toPostResponseDtofromPost(post));
        }

        return responseDtos;
    }

    //게시글 단건 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPost() {
        Post post = postRepository.findById(1L).get();

        PostResponseDto responseDto = postMapper.toPostResponseDtofromPost(post);

        return responseDto;
    }
}
