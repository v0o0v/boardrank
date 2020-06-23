package net.boardrank.boardgame.ui.common;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;

import java.util.List;

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

        if (account.getOnelineIntroduce() != null && !account.getOnelineIntroduce().equals(""))
            layout.add(new H5(account.getOnelineIntroduce()));

        FormLayout form = new FormLayout();
        form.setSizeFull();

        form.addFormItem(new H5(account.getBoardPoint().toString()), "BoardRank Point");
        form.addFormItem(new H5(account.getAngelPoint().toString()), "Angel Point");
        form.addFormItem(new H5(account.getWinCount() + "승 " + account.getLoseCount() + "패"), "승패");
        form.addFormItem(new Div(), "Last Matches");
        form.add(initLastMatchs());
        form.addFormItem(new Div(), "Friend List");
        form.add(new FriendGrid(gameMatchService, account));
        form.addFormItem(new Div(), "");
        form.add(new FriendButton(gameMatchService
                , gameMatchService.getAccountService().getCurrentAccount()
                , account
        ));
        layout.add(form);
        add(layout);

        VerticalLayout bottom = new VerticalLayout();
        bottom.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        bottom.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);
        bottom.add(new Button("닫기", event -> close()));
        add(bottom);
    }

    private Grid initLastMatchs() {
        GameMatchGrid grid = new GameMatchGrid(gameMatchService);
        List<GameMatch> last5Match = gameMatchService.getLast5Match(account);
        grid.setItems(last5Match);
        grid.setCompact(true);
        return grid;
    }

}
