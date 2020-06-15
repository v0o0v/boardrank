package net.boardrank.boardgame.service.matchAcceptFilter;

import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BoardRankPointCalculatorFilter implements MatchAcceptFilter {

    @Value("${net.boardrank.point.win}")
    Integer winPoint;

    @Value("${net.boardrank.point.lose}")
    Integer losePoint;

    @Override
    @Transactional
    public void handle(GameMatchService gameMatchService, GameMatch gameMatch) {
        gameMatch.getRankentries().stream()
                .forEach(rankEntry -> {
                    int bp = gameMatch.getNumOfSmallerThanMe(rankEntry) * winPoint
                            - gameMatch.getNumOfGreaterThanMe(rankEntry) * losePoint;
                    rankEntry.getAccount().addBP(bp);
                    gameMatchService.getAccountService().saveAccount(rankEntry.getAccount());
                });
    }

}
