package net.boardrank.boardgame.ui.common;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.service.GameMatchService;

@Slf4j
public class UserDialog extends ResponsiveDialog {

    private GameMatchService gameMatchService;

    private Account account;

    public UserDialog(
            GameMatchService gameMatchService
            , Account account) {

        this.gameMatchService = gameMatchService;
        this.account = account;

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);


        if (account.getPictureURL() == null)
            layout.add(new Icon(VaadinIcon.USER));
        else {
            Image image = new Image(account.getPictureURL(), "");
            image.setMaxWidth("96px");
            image.setMaxHeight("96px");
            layout.add(image);
        }
        layout.add(new H3(account.getName()));

        FormLayout form = new FormLayout();
        form.setSizeFull();

        form.addFormItem(new H5(account.getBoardPoint().toString()), "BoardRank Point");
        form.addFormItem(new H5(account.getAngelPoint().toString()), "Angel Point");
        form.addFormItem(new H5(account.getWinCount() + "승 " + account.getLoseCount() + "패"), "승패");
        form.add(new FriendButton(gameMatchService
                , gameMatchService.getAccountService().getCurrentAccount()
                , account
        ));

//        if (!gameMatchService.getAccountService().getCurrentAccount().equals(account)
//                && !gameMatchService.getAccountService().getCurrentAccount().isFriend(account))
//            form.add(new Button("친구 요청", event -> {
//                gameMatchService.getAccountService()
//                        .requestFriend(gameMatchService.getAccountService().getCurrentAccount(), account);
//                UI.getCurrent().getPage().reload();
//            }));

        layout.add(form);
        add(layout);
    }

}
