package board.soyun_board.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //400 BAD REQUEST
    INVALID_REQUEST_DATA(HttpStatus.BAD_REQUEST, "입력하신 데이터가 요청사항과 알맞지 않습니다"),
    INVALID_POST_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 게시글 ID입니다"),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다"),
    //404 NOT FOUND
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID에 존재하는 게시글을 찾을 수 없습니다"),

    //500 SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 에러가 발생했습니다");

    private final HttpStatus status;
    private final String message;
}
