package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.GameMatchService;

@Route(layout = MainLayout.class)
public class FriendTab extends VerticalLayout {

    public FriendTab(AccountService accountService) {
        setAlignItems(Alignment.CENTER);
        addAndExpand(new FriendListView(accountService));

        setSpacing(false);
        setMargin(false);
        setPadding(false);
    }

}