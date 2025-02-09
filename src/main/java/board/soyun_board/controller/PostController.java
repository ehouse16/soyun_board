package board.soyun_board.controller;

import board.soyun_board.dto.PostCreateDto;
import board.soyun_board.dto.PostResponseDto;
import board.soyun_board.dto.PostUpdateDto;
import board.soyun_board.dto.SearchDto;
import board.soyun_board.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "CRUD API", description = "기본적인 CRUD 컨트롤러에 대한 설명입니다")
public class PostController {
    private final PostService postService;

    @GetMapping("/api/test")
    public String hello() {
        return "테스트입니다.";
    }

    //게시글 작성
    @PostMapping("/api/post/write")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary="게시글 작성", description = "PostCreateDto로 받은 게시글을 저장한다")
    public PostResponseDto write(@RequestBody @Valid PostCreateDto postCreateDto) {
        PostResponseDto postresponseDto = postService.write(postCreateDto);

        return postresponseDto;

    }

    //게시글 전체 조회
    @GetMapping("/api/posts")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "게시글 전체 조회", description = "저장되어있는 게시글 전체를 조회한다")
    public List<PostResponseDto> read() {
        List<PostResponseDto> posts = postService.getPosts();

        return posts;
    }

    //게시글 단건 조회
    @GetMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "게시글 단건 조회", description = "저장되어있는 게시글 중 한 건을 조회한다")
    public PostResponseDto read(@PathVariable Long id) {
        PostResponseDto postResponseDto = postService.getPost(id);

        return postResponseDto;
    }

    //게시글 검색
    @GetMapping("/api/posts/search")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "게시글 검색", description = "저장되어있는 게시글 중 검색타입과, 검색단어를 준다면 해당 조건에 일치하는 게시글만을 조회한다")
    public List<PostResponseDto> search(@RequestParam String searchType, @RequestParam String keyWord) {
        SearchDto searchDto = new SearchDto(searchType, keyWord);

        List<PostResponseDto> posts = postService.search(searchDto);

        return posts;
    }

    //게시글 수정
    @PutMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "게시글 수정", description = "저장되어있는 게시글 중 전달된 id로 게시글을 찾아 수정한다")
    public PostResponseDto update(@PathVariable Long id, @RequestBody @Valid PostUpdateDto postUpdateDto) {
        PostResponseDto post = postService.modify(id, postUpdateDto);

        return post;
    }

    //게시글 삭제
    @DeleteMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "게시글 삭제", description = "저장되어있는 게시글 중 전달된 id값의 게시글을 삭제한다")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }
}
