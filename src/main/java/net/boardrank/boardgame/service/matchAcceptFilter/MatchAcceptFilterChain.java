package net.boardrank.boardgame.service.matchAcceptFilter;

import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.GameMatch;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MatchAcceptFilterChain {

    List<MatchAcceptFilter> matchAcceptFilters = new ArrayList<>();

    public MatchAcceptFilterChain(ApplicationContext ac) {

        matchAcceptFilters.add(ac.getBean(BoardRankPointCalculatorFilter.class));
        matchAcceptFilters.add(ac.getBean(WinLoseCalculatorFilter.class));
        matchAcceptFilters.add(ac.getBean(AngelPointCalculatorFilter.class));
        matchAcceptFilters.add(ac.getBean(AccountMatchStatusFilter.class));

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
