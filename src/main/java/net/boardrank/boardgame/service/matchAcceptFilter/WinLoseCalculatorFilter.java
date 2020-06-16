package net.boardrank.boardgame.service.matchAcceptFilter;

import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class WinLoseCalculatorFilter implements MatchAcceptFilter {

    @Autowired
    GameMatchService gameMatchService;

    @Override
    @Transactional
    public void handle(GameMatch gameMatch) {
        gameMatch.getRankentries().stream()
                .forEach(rankEntry -> {

                    if (gameMatch.getWinnerList().contains(rankEntry.getAccount()))
                        rankEntry.getAccount().win();
                    else
                        rankEntry.getAccount().lose();

                    gameMatchService.getAccountService().saveAccount(rankEntry.getAccount());
                });
    }

}
