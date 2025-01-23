package board.soyun_board.controller;

import board.soyun_board.dto.PostCreateDto;
import board.soyun_board.dto.PostResponseDto;
import board.soyun_board.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post/write")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDto write(@RequestBody @Valid PostCreateDto postCreateDto) {
        PostResponseDto postresponseDto = postService.write(postCreateDto);

        return postresponseDto;
    }
}
