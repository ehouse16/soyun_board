package board.soyun_board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreateDto {
    @NotBlank(message = "제목을 입력하세요")
    @Size(min = 1, max = 15, message = "제목은 최소 1자, 최대 15자까지 입력할 수 있습니다")
    private String title;

    @NotBlank(message = "내용을 입력하세요")
    @Size(min = 1, max = 1000, message = "내용은 최소 1자, 최대 1000자까지 입력할 수 있습니다")
    private String content;

    @NotBlank(message = "글쓴이를 임력해주세요")
    private String author;
}
