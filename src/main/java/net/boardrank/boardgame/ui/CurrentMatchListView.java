package net.boardrank.boardgame.ui;

import com.google.common.collect.Lists;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
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
        setSizeFull();
//        setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
//        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
    }

    private void initComponent() {
        Account account = accountService.getCurrentAccount();
        List<GameMatch> inprogressMatches = gameMatchService.getInprogressMatches(account);
        Board board = new Board();

        Lists.partition(inprogressMatches, 3).stream()
                .forEach(gameMatches -> {
                    Row row = new Row();
                    gameMatches.stream().forEach(gameMatch -> {
                        row.add(new MatchView(gameMatchService, gameMatch, boardgameService));
                    });
                    row.add("　");
                    board.add(row);
                });

        add(board);
    }


}