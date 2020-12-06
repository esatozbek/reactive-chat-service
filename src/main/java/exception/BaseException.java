package exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class BaseException extends RuntimeException {
    private HttpStatus httpStatus;

    public BaseException(String message) {
        super(message);
    }
}
