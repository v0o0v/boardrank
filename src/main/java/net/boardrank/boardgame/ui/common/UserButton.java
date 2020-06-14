package net.boardrank.boardgame.ui.common;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import net.boardrank.boardgame.domain.Account;

public class UserButton extends Button {

    Account account;

    public UserButton(Account account) {
        this.account = account;
        setText("  " + account.getName());

        if (account.getPictureURL() != null) {
            setIcon(new UserIcon(account));
        } else {
            setIcon(new Icon(VaadinIcon.USER));
        }

        addThemeVariants(
                ButtonVariant.LUMO_SMALL
                , ButtonVariant.LUMO_PRIMARY
                , ButtonVariant.LUMO_SUCCESS
        );
    }
}