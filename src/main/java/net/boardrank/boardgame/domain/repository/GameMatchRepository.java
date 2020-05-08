package net.boardrank.boardgame.domain.repository;

import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.account.domain.Account;
import net.boardrank.boardgame.domain.GameMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameMatchRepository extends JpaRepository<GameMatch,Long> {

//    public List<GameMatch> findGameMatchesByPaticiantAccountsContaining(Account account);

//    public List<GameMatch> findGameMatchesByRankentriesContainingAndAndRankentries(Account account);

//    public List<GameMatch> findGameMatchesByRankEntriesContainsAndGameMatchStatusIsNot(Account account, GameMatchStatus gameMatchStatus);

}
