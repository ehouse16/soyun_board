package board.soyun_board.exception;

import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {
    private final ErrorCode errorCode;

    public BoardException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
