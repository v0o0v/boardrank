package net.boardrank.boardgame.service.matchAcceptFilter;

import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AngelPointCalculatorFilter implements MatchAcceptFilter {

    @Autowired
    GameMatchService gameMatchService;

    @Override
    public void handle(GameMatch gameMatch) {
        Account boardgameProvider = gameMatch.getBoardgameProvider();
        if (boardgameProvider != null) {
            boardgameProvider.addAP(1);
            gameMatchService.getAccountService().saveAccount(boardgameProvider);
        }

        Account ruleSupporter = gameMatch.getRuleSupporter();
        if (ruleSupporter != null) {
            ruleSupporter.addAP(1);
            gameMatchService.getAccountService().saveAccount(ruleSupporter);
        }
    }
}
