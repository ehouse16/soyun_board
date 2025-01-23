package board.soyun_board.service;

import board.soyun_board.dto.PostCreateDto;
import board.soyun_board.dto.PostResponseDto;
import board.soyun_board.entity.Post;
import board.soyun_board.mapper.PostMapper;
import board.soyun_board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Transactional
    public PostResponseDto write(PostCreateDto postCreateDto) {
        Post post = postMapper.toPostFromPostCreateDto(postCreateDto);

        postRepository.save(post);

        PostResponseDto postResponseDto = postMapper.toPostResponseDtofromPost(post);

        return postResponseDto;
    }
}
