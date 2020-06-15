package net.boardrank.boardgame.service.matchAcceptFilter;

import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MatchAcceptFilterChain {

    List<MatchAcceptFilter> matchAcceptFilters = new ArrayList<>();

    ApplicationContext ac;

    GameMatchService gameMatchService;

    public MatchAcceptFilterChain(
            ApplicationContext applicationContext
            , GameMatchService gameMatchService
    ) {
        this.ac = applicationContext;
        this.gameMatchService = gameMatchService;

        matchAcceptFilters.add(ac.getBean(BoardRankPointCalculatorFilter.class));
        matchAcceptFilters.add(ac.getBean(WinLoseCalculatorFilter.class));
    }

    public void doFilterChain(GameMatch gameMatch) {
        matchAcceptFilters.forEach(matchAcceptFilter -> {
            try {
                matchAcceptFilter.handle(gameMatchService, gameMatch);
            } catch (Exception e) {
                log.error("matchAcceptFilters 처리 중 에러발생", e);
            }
        });
    }
}
