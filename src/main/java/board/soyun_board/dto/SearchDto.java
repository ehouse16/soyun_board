package board.soyun_board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchDto {
    private String searchType;
    private String keyWord;
}
