package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;

import java.util.List;

@Route(layout = MainLayout.class)
public class CurrentMatchListView extends VerticalLayout {

    GameMatchService gameMatchService;
    AccountService accountService;
    BoardgameService boardgameService;

    public CurrentMatchListView(GameMatchService gameMatchService
            , AccountService accountService
            , BoardgameService boardgameService
    ) {
        this.gameMatchService = gameMatchService;
        this.accountService = accountService;
        this.boardgameService = boardgameService;

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        initLayout();
        initComponent();

        setSpacing(true);
        setMargin(true);
        setPadding(true);
    }

    private void initLayout() {
        setSizeFull();
    }

    public void initComponent() {
        Account account = accountService.getCurrentAccount();
        List<GameMatch> inprogressMatches = gameMatchService.getInprogressMatches(account);
        inprogressMatches.sort((o1, o2) -> o2.getCreatedTime().compareTo(o1.getCreatedTime()));

        if (inprogressMatches != null) {
            inprogressMatches.stream()
                    .forEach(gameMatch -> {
                        MatchView matchView = new MatchView(gameMatchService, gameMatch, boardgameService);
                        add(matchView);
                    });
        }
    }

}