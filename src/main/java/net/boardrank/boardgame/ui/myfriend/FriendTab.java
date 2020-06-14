package net.boardrank.boardgame.ui.myfriend;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.MainLayout;

@Route(layout = MainLayout.class)
public class FriendTab extends VerticalLayout {

    public FriendTab(GameMatchService gameMatchService, AccountService accountService) {
        setAlignItems(Alignment.CENTER);
        addAndExpand(new FriendListView(gameMatchService, accountService));

        setSpacing(false);
        setMargin(false);
        setPadding(false);
    }

}