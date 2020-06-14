package net.boardrank.boardgame.ui.common;

import com.vaadin.flow.component.html.Image;
import net.boardrank.boardgame.domain.Account;

public class UserIcon extends Image {

    Account account;

    public UserIcon(Account account) {
        this.account = account;

        if (account.getPictureURL() == null)
            throw new RuntimeException("아이콘 이미지가 존재하지 않습니다.");

        setSrc(account.getPictureURL());
        setMaxHeight("24px");
        setMaxWidth("24px");
        getStyle().set("border-radius", "24px");
    }
}