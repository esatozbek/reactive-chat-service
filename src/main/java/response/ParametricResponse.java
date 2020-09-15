package response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParametricResponse<T> extends BaseResponse {
    private T data;

    public ParametricResponse(T data) {
        super();
        this.data = data;
    }
}
