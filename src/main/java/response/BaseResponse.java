package response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
    private Boolean status;
    private String message;
    private Long timestamp;

    public BaseResponse() {
        timestamp = System.currentTimeMillis();
        this.status = true;
        this.message = "";
    }
}
