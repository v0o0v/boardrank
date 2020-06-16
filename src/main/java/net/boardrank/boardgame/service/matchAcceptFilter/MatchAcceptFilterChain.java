package net.boardrank.boardgame.service.matchAcceptFilter;

import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MatchAcceptFilterChain {

    List<MatchAcceptFilter> matchAcceptFilters = new ArrayList<>();

    public MatchAcceptFilterChain(ApplicationContext ac, GameMatchService gameMatchService) {

        matchAcceptFilters.add(ac.getBean(BoardRankPointCalculatorFilter.class));
        matchAcceptFilters.add(ac.getBean(WinLoseCalculatorFilter.class));
    }

    public void doFilterChain(GameMatch gameMatch) {
        matchAcceptFilters.forEach(matchAcceptFilter -> {
            try {
                matchAcceptFilter.handle(gameMatch);
            } catch (Exception e) {
                log.error("matchAcceptFilters 처리 중 에러발생", e);
            }
        });
    }
}
