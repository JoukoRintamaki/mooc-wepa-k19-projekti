package wall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Photo extends AbstractPersistable<Long> {
    @NotNull
    @NotEmpty
    private String description;
    @Lob
    @NotNull
    @NotEmpty
    private byte[] content;
    @ManyToMany
    private Set<Message> comments;
    @ManyToMany
    private Set<Account> likes;
}