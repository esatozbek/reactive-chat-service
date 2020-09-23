package domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public abstract class BaseEntity {
    @Id
    @Column("Id")
    public Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
