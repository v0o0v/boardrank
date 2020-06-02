package net.boardrank.boardgame.ui.myfriend;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import net.boardrank.boardgame.domain.Friend;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.ui.ResponsiveVerticalLayout;

import java.time.format.DateTimeFormatter;

public class FriendListView extends ResponsiveVerticalLayout {

    AccountService accountService;

    private Grid<Friend> grid = new Grid<>(Friend.class);

    public FriendListView(AccountService accountService) {
        this.accountService = accountService;

        setMargin(false);
        setPadding(false);
        setSpacing(false);

        add(this.initInviteFriendButton());
        add(this.initFriendGrid());
    }

    private Component initInviteFriendButton() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        Button btn_newFriend = new Button("새친구 추가하기");
        layout.add(btn_newFriend);
        layout.setHorizontalComponentAlignment(Alignment.END, btn_newFriend);
        btn_newFriend.addClickListener(event -> {
            FriendInviteDialog friendInviteDialog = new FriendInviteDialog(accountService, null);
            friendInviteDialog.open();
        });
        return layout;
    }

    private Component initFriendGrid() {
        configureFriendListGrid();
        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();
        updateFriendListGrid();
        return content;
    }

    private void configureFriendListGrid() {
        grid.removeAllColumns();

        grid.addColumn(friend -> {
            return friend.getFriend().getName();
        }).setHeader("친구 이름").setSortable(true);

        grid.addColumn(friend -> {
            return friend.getDday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }).setHeader("친구된 날").setSortable(true);

        grid.addColumn(new ComponentRenderer<>(friend -> {
            Button remove = new Button("삭제", event -> {

                ConfirmDialog dialog = new ConfirmDialog("삭제하시겠습니까?",
                        "삭제를 해도 상대방 친구 리스트에는 그대로 남아있습니다.",
                        "삭제", deleteEvent -> {
                    accountService.removeFriend(accountService.getCurrentAccount(), friend);
                    updateFriendListGrid();
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

    private void updateFriendListGrid() {
        grid.setItems(this.accountService.getCurrentAccount().getFriends());
    }
}