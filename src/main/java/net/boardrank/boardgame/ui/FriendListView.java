package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import net.boardrank.account.domain.Friend;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.service.FriendService;

@Route(value = "FriendListView", layout = MainLayout.class)
public class FriendListView extends VerticalLayout {

    AccountService accountService;

    FriendService friendService;

    private Grid<Friend> grid = new Grid<>(Friend.class);

    public FriendListView(AccountService accountService, FriendService friendService) {
        this.accountService = accountService;
        this.friendService = friendService;

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
            return friend.getFriend().getName();
        }).setHeader("친구 이름").setSortable(true);

        grid.addColumn(new ComponentRenderer<>(friend -> {
            Button remove = new Button("Remove", event -> {

                ConfirmDialog dialog = new ConfirmDialog("친구 삭제",
                        "삭제하시겠습니까? 삭제를 해도 상대방 친구 리스트에는 내가 그대로 남아있습니다.",
                        "삭제", deleteEvent -> {
                    friendService.removeFriend(accountService.getCurrentAccount(), friend);
                    updateList();
                }, "Cancel", cancelEvent -> {
                });
                dialog.setConfirmButtonTheme("error primary");
                dialog.open();
            });

            return remove;
        })).setHeader("친구 삭제");

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