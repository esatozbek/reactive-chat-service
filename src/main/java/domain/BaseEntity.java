package domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.Map;

@MappedSuperclass
public abstract class BaseEntity<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract void updateEntity(T newEntity);

    static <T> T mapFromArguments(Map<String, Object> arguments, Class<T> tClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        T object = objectMapper.convertValue(arguments, tClass);
        return object;
    }
}
