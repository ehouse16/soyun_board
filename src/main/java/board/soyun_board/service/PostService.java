package board.soyun_board.service;

import board.soyun_board.dto.post.PostCreateDto;
import board.soyun_board.dto.post.PostResponseDto;
import board.soyun_board.dto.post.PostUpdateDto;
import board.soyun_board.dto.post.SearchDto;
import board.soyun_board.entity.post.Post;
import board.soyun_board.exception.BoardException;
import board.soyun_board.exception.ErrorCode;
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
            throw new BoardException(ErrorCode.INVALID_REQUEST_DATA);
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
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                ()->new BoardException(ErrorCode.POST_NOT_FOUND)
        );

        PostResponseDto responseDto = postMapper.toPostResponseDtofromPost(post);

        return responseDto;
    }

    //게시글 검색 기능
    @Transactional(readOnly = true)
    public List<PostResponseDto> search(SearchDto searchDto) {
        String searchType = searchDto.getSearchType();
        String keyword = searchDto.getKeyWord();

        switch(searchType) {
            case "title":
                return postRepository.findAllByTitleContaining(keyword).stream().map(postMapper::toPostResponseDtofromPost).toList();
            case "content":
                return postRepository.findAllByContentContaining(keyword).stream().map(postMapper::toPostResponseDtofromPost).toList();
            default:
                return postRepository.findAll().stream().map(postMapper::toPostResponseDtofromPost).toList();
        }
    }

    //게시글 수정 기능
    @Transactional
    public PostResponseDto modify(Long id, PostUpdateDto postUpdateDto) {
        Post post = postRepository.findById(id).orElseThrow(
                ()->new BoardException(ErrorCode.POST_NOT_FOUND)
        );

        post.update(postUpdateDto);

        PostResponseDto responseDto = postMapper.toPostResponseDtofromPost(post);

        return responseDto;
    }

    //게시글 삭제 기능
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                ()->new BoardException(ErrorCode.POST_NOT_FOUND)
        );
        postRepository.delete(post);
    }
}
