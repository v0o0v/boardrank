package net.boardrank.boardgame.ui.myrank;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.common.ResponsiveVerticalLayout;
import net.boardrank.boardgame.ui.common.UserButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyRankListView extends ResponsiveVerticalLayout {

    GameMatchService gameMatchService;
    AccountService accountService;

    private Grid<Account> grid = new Grid<>(Account.class);

    public MyRankListView(GameMatchService gameMatchService) {
        this.gameMatchService = gameMatchService;
        this.accountService = this.gameMatchService.getAccountService();

        setMargin(false);
        setPadding(false);

        configureGrid();
        add(grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.removeAllColumns();

        grid.addColumn(new ComponentRenderer<>(account -> {
            return new UserButton(gameMatchService, account);
        })).setHeader("이름");

        Grid.Column<Account> board_point = grid.addColumn(account -> {
            return account.getBoardPoint();
        }).setHeader("Board Point").setSortable(true);

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
            col.setSortable(true);
        });
    }

    private void updateList() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(this.accountService.getCurrentAccount());
        accounts.addAll(this.accountService.getCurrentAccount().getFriendsAsAccounType());

        accounts.sort((o1, o2) -> {
            return o2.getBoardPoint().compareTo(o1.getBoardPoint());
        });

        grid.setItems(accounts);
    }
}