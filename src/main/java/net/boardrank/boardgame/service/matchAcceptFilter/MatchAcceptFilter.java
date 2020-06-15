package net.boardrank.boardgame.service.matchAcceptFilter;

import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;

public interface MatchAcceptFilter {

    void handle(GameMatchService gameMatchService, GameMatch gameMatch);

}
