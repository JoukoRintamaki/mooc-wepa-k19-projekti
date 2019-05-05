package wall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message extends AbstractPersistable<Long> {
    @ManyToOne
    private Account sender;
    @ManyToOne
    private Account recipient;
    private String content;
    private LocalDateTime createDate;
    @ManyToMany
    private Set<Account> likes;
    @ManyToMany
    private Set<Message> answers;
}