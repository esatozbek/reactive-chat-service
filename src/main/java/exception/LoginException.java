package exception;

import org.springframework.http.HttpStatus;

public class LoginException extends BaseException {
    public LoginException() {
        super("User credentials are invalid");
        setHttpStatus(HttpStatus.UNAUTHORIZED);
    }
}
