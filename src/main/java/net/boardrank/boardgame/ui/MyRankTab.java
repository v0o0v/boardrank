package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.GameMatchService;

@Route(value = "", layout = MainLayout.class)
public class MyRankTab extends VerticalLayout {

    public MyRankTab(AccountService accountService) {
        setAlignItems(Alignment.CENTER);
        addAndExpand(new MyRankListView(accountService));
    }

}