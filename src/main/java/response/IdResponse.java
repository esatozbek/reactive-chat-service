package response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IdResponse extends BaseResponse {
    private Long id;

    public IdResponse(Long id) {
        this.id = id;
        this.setStatus(true);
        this.setMessage("");
    }
}
