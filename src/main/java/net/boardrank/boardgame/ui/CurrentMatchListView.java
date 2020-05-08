package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.account.domain.Account;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;

import java.util.List;

@Route(value = "CurrentMatchListView", layout = MainLayout.class)
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

        initLayout();
        initComponent();
    }

    private void initLayout() {
        UI.getCurrent().getPage().setTitle("BoardRank");
        setSizeFull();
    }

    private void initComponent() {
        Account account = accountService.getCurrentAccount();
        List<GameMatch> inprogressMatches = gameMatchService.getInprogressMatches(account);
        Board board = new Board();

        if (inprogressMatches != null) {
            inprogressMatches.stream()
                    .forEach(gameMatch -> {
                        MatchView matchView = new MatchView(gameMatchService, gameMatch, boardgameService);
                        board.addRow(matchView);
                    });
        }
        add(board);

    }


}