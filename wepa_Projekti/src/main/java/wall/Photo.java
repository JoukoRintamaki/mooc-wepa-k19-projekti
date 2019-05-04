package wall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Photo extends AbstractPersistable<Long> {
    private String description;
    @Lob
    private byte[] content;
}