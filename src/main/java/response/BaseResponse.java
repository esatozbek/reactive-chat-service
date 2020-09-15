package response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
    private Boolean status;
    private String message;

    public BaseResponse() {
        this.status = true;
        this.message = "";
    }
}
