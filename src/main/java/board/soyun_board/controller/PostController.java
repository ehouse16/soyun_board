package board.soyun_board.controller;

import board.soyun_board.dto.PostCreateDto;
import board.soyun_board.dto.PostResponseDto;
import board.soyun_board.dto.PostUpdateDto;
import board.soyun_board.dto.SearchDto;
import board.soyun_board.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/api/test")
    public String hello() {
        return "테스트입니다.";
    }

    //게시글 작성
    @PostMapping("/post/write")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDto write(@RequestBody @Valid PostCreateDto postCreateDto) {
        PostResponseDto postresponseDto = postService.write(postCreateDto);

        return postresponseDto;
    }

    //게시글 전체 조회
    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> read() {
        List<PostResponseDto> posts = postService.getPosts();

        return posts;
    }

    //게시글 단건 조회
    @GetMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDto read(@PathVariable Long id) {
        PostResponseDto postResponseDto = postService.getPost(id);

        return postResponseDto;
    }

    //게시글 검색
    @GetMapping("/posts/search")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> search(@RequestParam String searchType, @RequestParam String keyWord) {
        SearchDto searchDto = new SearchDto(searchType, keyWord);

        List<PostResponseDto> posts = postService.search(searchDto);

        return posts;
    }

    //게시글 수정
    @PutMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDto update(@PathVariable Long id, @RequestBody @Valid PostUpdateDto postUpdateDto) {
        PostResponseDto post = postService.modify(id, postUpdateDto);

        return post;
    }

    //게시글 삭제
    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }
}
