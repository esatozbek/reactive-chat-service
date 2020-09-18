package domain;


import org.springframework.data.annotation.Id;

public abstract class BaseEntity {
    @Id
    public Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
