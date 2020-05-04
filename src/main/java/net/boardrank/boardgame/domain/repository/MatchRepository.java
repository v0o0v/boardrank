package net.boardrank.boardgame.domain.repository;

import net.boardrank.boardgame.domain.Match;
import net.boardrank.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match,Long> {

    public List<Match> findGamesByPaticiantAccountsContaining(Account account);

}
