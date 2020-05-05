package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.account.domain.Account;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;

import java.util.List;

@Route(value = "CurrentMatchListView", layout = MainLayout.class)
public class CurrentMatchListView extends VerticalLayout {

    GameMatchService gameMatchService;

    AccountService accountService;

    public CurrentMatchListView(GameMatchService gameMatchService
            , AccountService accountService
    ) {
        this.gameMatchService = gameMatchService;
        this.accountService = accountService;

        initLayout();
        initComponent();
    }

    private void initComponent() {
        Account account = accountService.getCurrentAccount();
        List<GameMatch> gameMatches = gameMatchService.getPlayingMatches(account);
        gameMatches.stream()
                .forEach(gameMatch -> add(new MatchView(gameMatchService, gameMatch)));
    }

    private void initLayout() {
        setSizeFull();
    }

}