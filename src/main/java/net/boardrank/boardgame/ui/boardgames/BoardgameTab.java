package net.boardrank.boardgame.ui.boardgames;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.MainLayout;

@Route(layout = MainLayout.class)
public class BoardgameTab extends VerticalLayout {

    public BoardgameTab(GameMatchService gameMatchService) {
        setAlignItems(Alignment.CENTER);
        addAndExpand(new BoardgameListView(gameMatchService));
    }

}