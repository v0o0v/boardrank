package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.service.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyRankListView extends ResponsiveVerticalLayout {

    AccountService accountService;

    private Grid<Account> grid = new Grid<>(Account.class);

    public MyRankListView(AccountService accountService) {
        this.accountService = accountService;

        configureGrid();
        add(grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.removeAllColumns();

        grid.addColumn(account -> {
            return account.getName();
        }).setHeader("이름");

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
        accounts.addAll(this.accountService.getCurrentAccount().getFriends().stream()
                .map(friend -> {
                    return friend.getFriend();
                })
                .collect(Collectors.toList())
        );
        accounts.sort((o1, o2) -> {
            return o2.getBoardPoint().compareTo(o1.getBoardPoint());
        });
        grid.setItems(accounts);
        grid.select(this.accountService.getCurrentAccount());
    }
}