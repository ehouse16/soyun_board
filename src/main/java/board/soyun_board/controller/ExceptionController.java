package board.soyun_board.controller;

import board.soyun_board.dto.ErrorResponse;
import board.soyun_board.exception.BoardException;
import board.soyun_board.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(BoardException.class)
    public ResponseEntity<ErrorResponse> handleBoardException(BoardException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = new ErrorResponse(errorCode.getStatus(), errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        int status = ErrorCode.INTERNAL_SERVER_ERROR.getStatus();
        String message = ErrorCode.INTERNAL_SERVER_ERROR.getMessage();

        ErrorResponse response = new ErrorResponse(status, message);
        return ResponseEntity.status(status).body(response);
    }
}
