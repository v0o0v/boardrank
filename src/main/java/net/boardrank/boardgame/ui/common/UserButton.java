package net.boardrank.boardgame.ui.common;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.service.GameMatchService;

public class UserButton extends Button {

    GameMatchService gameMatchService;
    Account account;

    public UserButton(GameMatchService gameMatchService
                      ,Account account) {

        this.gameMatchService = gameMatchService;
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

        addClickListener(event -> {
            UserDialog userDialog = new UserDialog(gameMatchService,account);
            userDialog.open();
        });

    }
}