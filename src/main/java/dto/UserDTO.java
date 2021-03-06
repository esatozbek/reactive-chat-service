package dto;

import enums.UserStatusEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO implements BaseDTO {
    private Long id;
    private String username;
    private UserStatusEnum status;
}
