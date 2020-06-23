package net.boardrank.boardgame.ui.common;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.GameMatchService;

public class FriendGrid extends Grid<Account> {

    GameMatchService gameMatchService;
    AccountService accountService;
    Account me;

    public FriendGrid(GameMatchService gameMatchService, Account me) {

        this.gameMatchService = gameMatchService;
        this.accountService = this.gameMatchService.getAccountService();
        this.me = me;

        configureGrid();
    }

    private void configureGrid() {
        removeAllColumns();

        addColumn(new ComponentRenderer<>(account -> {
            return new UserButton(gameMatchService, account);
        })).setHeader("이름");

        addColumn(account -> {
            return account.getBoardPoint();
        }).setHeader("BP").setSortable(true);

        addColumn(account -> {
            return account.getAngelPoint();
        }).setHeader("AP").setSortable(true);

        getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
        });

        addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        setItems(me.getFriendsAsAccounType());
    }

}