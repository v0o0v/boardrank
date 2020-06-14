package net.boardrank.boardgame.ui.myfriend;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.common.FriendButton;
import net.boardrank.boardgame.ui.common.ResponsiveDialog;
import net.boardrank.boardgame.ui.common.UserButton;
import net.boardrank.boardgame.ui.event.DialogSuccessCloseActionEvent;

import java.util.Set;

@Slf4j
public class FriendInviteDialog extends ResponsiveDialog {

    private GameMatchService gameMatchService;

    private AccountService accountService;

    private Grid<Account> gridAccount = new Grid<>(Account.class);

    private Board board = new Board();
    private TextField txt_friend;

    public FriendInviteDialog(
            GameMatchService gameMatchService
            , AccountService accountService
            , ComponentEventListener<DialogSuccessCloseActionEvent> listener
    ) {
        this.gameMatchService = gameMatchService;
        this.accountService = accountService;
        getEventBus().addListener(DialogSuccessCloseActionEvent.class, listener);

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        board.setSizeFull();
        add(board);

        createHeader();
        createContent();
        createFooter();
    }

    private void createHeader() {
        board.addRow(new Label("친구 찾기"));
        txt_friend = new TextField("친구의 이름을 입력해 주세요");
        txt_friend.setMinLength(1);
        Button btn_friend = new Button(new Icon(VaadinIcon.SEARCH));
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.END);
        layout.addAndExpand(txt_friend);
        layout.add(btn_friend);
        board.addRow(layout);

        btn_friend.addClickListener(event -> {
            if (!txt_friend.getValue().isEmpty()) {
                updateFriendList();
            }
        });
    }

    private void createContent() {
        gridAccount.removeAllColumns();
        gridAccount.setWidthFull();

        gridAccount.addColumn(new ComponentRenderer<>(account -> {
            return new UserButton(gameMatchService, account);
        })).setHeader("이름");

        gridAccount.addColumn(new ComponentRenderer<>(account -> {
            return new FriendButton(gameMatchService, accountService.getCurrentAccount(), account);
        })).setHeader("친구 요청");

        gridAccount.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
        });

        board.addRow(gridAccount);
    }

    private void createFooter() {
        Button abort = new Button("닫기");
        abort.addClickListener(buttonClickEvent -> close());

        HorizontalLayout footer = new HorizontalLayout();
        footer.add(abort);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        board.addRow(footer);
    }


    private void checkValidation() {

    }

    private void updateFriendList() {
        Set<Account> friends = accountService.getAccountsContainsName(txt_friend.getValue());
        Account me = accountService.getCurrentAccount();
        friends.remove(me);
        friends.remove(me.getFriendsAsAccounType());
        gridAccount.setItems(friends);
    }

}
