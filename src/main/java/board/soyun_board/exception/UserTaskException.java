package board.soyun_board.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserTaskException extends RuntimeException {
    private String msg;
    private HttpStatus status;

    public UserTaskException(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
    }
}
