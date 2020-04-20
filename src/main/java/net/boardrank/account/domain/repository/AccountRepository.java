package net.boardrank.account.domain.repository;

import net.boardrank.account.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    Page<Account> findAllByEmailContaining(String email, Pageable pageable);
}
