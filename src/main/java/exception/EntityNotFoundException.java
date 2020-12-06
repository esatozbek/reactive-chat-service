package exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
@Getter
public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException(String type, Long id) {
        super(type + " enity not found with id: " + id);
        this.setHttpStatus(HttpStatus.UNAUTHORIZED);
    }

    public EntityNotFoundException(String type, String param) {
        super(type + " enity not found with parameter: " + param);
        this.setHttpStatus(HttpStatus.UNAUTHORIZED);
    }
}
