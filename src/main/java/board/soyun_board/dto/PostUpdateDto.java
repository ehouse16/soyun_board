package board.soyun_board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostUpdateDto {
    private String title;
    private String content;
}
