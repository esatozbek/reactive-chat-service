package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDTO implements BaseDTO {
    private Long id;
    private String title;
}
