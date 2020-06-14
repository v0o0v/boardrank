package net.boardrank.boardgame.ui.common;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.service.GameMatchService;

public class FriendButton extends Button {

    public FriendButton(GameMatchService gameMatchService
            , Account from
            , Account to
    ) {
        switch (gameMatchService.getAccountService().getFriendStatus(from, to)) {
            case Me:
                setText("나 입니다.");
                setVisible(false);
                break;
            case FriendRequested:
                setText("친구요청중입니다.");
                setEnabled(false);
                break;
            case Friend:
                setText("친구사이입니다.");
                setEnabled(false);
                break;
            case NotFriend:
                setText("친구 요청");
                addClickListener(event -> {
                    gameMatchService.getAccountService().requestFriend(from, to);
                    UI.getCurrent().getPage().reload();
                });
                break;
            default:
                throw new RuntimeException("친구상태가 올바르지 않습니다. from:" + from + " to:" + to);
        }

    }
}