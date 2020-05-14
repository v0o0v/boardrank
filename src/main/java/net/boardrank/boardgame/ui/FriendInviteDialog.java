package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.account.domain.Account;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.ui.event.DialogSuccessCloseActionEvent;

import java.util.Set;

@Slf4j
public class FriendInviteDialog extends Dialog {

    private AccountService accountService;

    private Grid<Account> gridAccount = new Grid<>(Account.class);

    private Board board = new Board();
    private TextField txt_friend;

    public FriendInviteDialog(AccountService accountService, ComponentEventListener<DialogSuccessCloseActionEvent> listener) {
        this.accountService = accountService;
        getEventBus().addListener(DialogSuccessCloseActionEvent.class, listener);

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        setWidth("400px");
        board.setSizeFull();
        add(board);

        createHeader();
        createContent();
        createFooter();

//        updateFriendList();
    }

    private void createHeader() {
        board.addRow(new Label("친구 찾기"));
        txt_friend = new TextField("친구의 이름을 입력해 주세요");
        Button btn_friend = new Button(new Icon(VaadinIcon.SEARCH));
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.END);
        layout.addAndExpand(txt_friend);
        layout.add(btn_friend);
        board.addRow(layout);

        btn_friend.addClickListener(event -> {
            updateFriendList();
        });
    }

    private void createContent() {
        gridAccount.removeAllColumns();
        gridAccount.setWidthFull();

        gridAccount.addColumn(account -> account.getName()
        ).setHeader("이름");

        gridAccount.addColumn(new ComponentRenderer<>(account -> {
            if (accountService.isProgressMakeFriend(accountService.getCurrentAccount(), account)) {
                return new Label("친구 요청중입니다.");
            } else {
                Button btn_newFriend = new Button("친구 요청하기");
                btn_newFriend.addClickListener(event -> {
                    if (!txt_friend.getValue().isEmpty()) {
                        accountService.requestFriend(accountService.getCurrentAccount(), account);
                        updateFriendList();
                    }
                });
                return btn_newFriend;
            }
        })).setHeader("");

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
