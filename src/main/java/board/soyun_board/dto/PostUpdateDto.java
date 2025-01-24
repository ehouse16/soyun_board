package board.soyun_board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUpdateDto {
    private String title;
    private String content;
}
