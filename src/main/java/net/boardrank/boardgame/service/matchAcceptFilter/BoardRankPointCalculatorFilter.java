package net.boardrank.boardgame.service.matchAcceptFilter;

import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BoardRankPointCalculatorFilter implements MatchAcceptFilter {

    @Value("${net.boardrank.point.win}")
    Integer winPoint;

    @Value("${net.boardrank.point.lose}")
    Integer losePoint;

    @Autowired
    GameMatchService gameMatchService;

    @Override
    @Transactional
    public void handle(GameMatch gameMatch) {
        gameMatch.getRankentries().stream()
                .forEach(rankEntry -> {
                    rankEntry.getAccount().addBP(gameMatch.calcBoardrankPoint(winPoint, losePoint, rankEntry));
                    gameMatchService.getAccountService().saveAccount(rankEntry.getAccount());
                });
    }

}
