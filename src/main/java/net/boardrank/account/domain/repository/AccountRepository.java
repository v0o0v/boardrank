package net.boardrank.account.domain.repository;

import net.boardrank.account.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    Page<Account> findAllByEmailContaining(String email, Pageable pageable);

    List<Account> getAllByEmailContaining(String txt);

    Optional<String> findByName(String name);

    Set<Account> findAllByNameContains(String Name);
}
