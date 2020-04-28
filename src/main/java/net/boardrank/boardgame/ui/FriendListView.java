package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.account.domain.Friend;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.domain.Game;
import net.boardrank.boardgame.service.GameService;

@Route(value = "FriendListView", layout = MainLayout.class)
public class FriendListView extends VerticalLayout {

    AccountService accountService;

    private Grid<Friend> grid = new Grid<>(Friend.class);

    public FriendListView(AccountService accountService) {
        this.accountService = accountService;

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();

        add(content);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.removeAllColumns();

        grid.addColumn(friend -> {
            return friend.getId();
        }).setHeader("ID");

        grid.addColumn(friend -> {
            return friend.getFriend().getName();
        }).setHeader("친구 이름");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
        });

    }

    private void updateList() {
        grid.setItems(this.accountService.getCurrentAccount().getFriends());
    }
}