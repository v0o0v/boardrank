package net.boardrank.boardgame.domain.repository;

import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameMatchRepository extends JpaRepository<GameMatch,Long> {

    public List<GameMatch> findGameMatchesByPaticiantAccountsContaining(Account account);

}
