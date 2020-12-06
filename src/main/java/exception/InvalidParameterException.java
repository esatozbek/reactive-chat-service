package exception;

import org.springframework.http.HttpStatus;

public class InvalidParameterException extends BaseException {
    public InvalidParameterException(String name) {
        super("Invalid parameter " + name);
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
