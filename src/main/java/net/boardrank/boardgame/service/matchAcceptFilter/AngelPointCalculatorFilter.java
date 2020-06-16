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
        gameMatch.getRankentries().stream()
                .forEach(rankEntry -> {
                    Account account = rankEntry.getAccount();
                    account.addAP(gameMatch.calcAngelPoint(rankEntry));
                    gameMatchService.getAccountService().saveAccount(account);
                });
    }
}
