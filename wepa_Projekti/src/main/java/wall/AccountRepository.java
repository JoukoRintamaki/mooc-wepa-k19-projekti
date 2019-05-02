package wall;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    Account findByprofilename(String profilename);

    List<Account> findBynameContaining(String query);
}
