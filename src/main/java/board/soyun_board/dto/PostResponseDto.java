package board.soyun_board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponseDto {
    private String title;
    private String content;
    private String author;
}
