package wall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String username;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String profilename;
    @ManyToMany
    private Set<Account> friends;
    @ElementCollection
    private Map<Account, LocalDateTime> friendRequests;

    public Account(@NotNull @NotEmpty String username, @NotNull @NotEmpty String password, @NotNull @NotEmpty String name, @NotNull @NotEmpty String profilename) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.profilename = profilename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Account account = (Account) o;
        return username.equals(account.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username);
    }
}
