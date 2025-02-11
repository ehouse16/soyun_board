package board.soyun_board.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreateDto {
    @Schema(description = "게시글 제목", example = "Spring Boot 게시판")
    @NotBlank(message = "제목을 입력하세요")
    @Size(min = 1, max = 15, message = "제목은 최소 1자, 최대 15자까지 입력할 수 있습니다")
    private String title;

    @Schema(description = "게시글 내용", example = "이 게시글은 Spring Boot를 사용하여 만든 게시글입니다!")
    @NotBlank(message = "내용을 입력하세요")
    @Size(min = 1, max = 1000, message = "내용은 최소 1자, 최대 1000자까지 입력할 수 있습니다")
    private String content;

    @Schema(description = "게시글 글쓴이, 한번 저장한 글쓴이는 수정이 불가능합니다.", example = "소윤")
    @NotBlank(message = "글쓴이를 입력해주세요")
    private String author;
}
