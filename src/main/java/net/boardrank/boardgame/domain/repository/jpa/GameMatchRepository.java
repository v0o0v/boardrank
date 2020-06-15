package net.boardrank.boardgame.domain.repository.jpa;

import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.GameMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameMatchRepository extends JpaRepository<GameMatch,Long> {

    public List<GameMatch> findGameMatchesByRankentriesAccount(Account account);

    public List<GameMatch> findGameMatchesByRankentriesAccountAndGameMatchStatusIsNot(Account account, GameMatchStatus gameMatchStatus);

    public List<GameMatch> findGameMatchesByRankentriesAccountAndGameMatchStatusIs(Account account, GameMatchStatus gameMatchStatus);

    public List<GameMatch> findTop5GameMatchesByRankentriesAccountAndAcceptedTimeIsNotNullOrderByAcceptedTimeDesc(Account account);
}
