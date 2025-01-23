package board.soyun_board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreateDto {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String author;
}
