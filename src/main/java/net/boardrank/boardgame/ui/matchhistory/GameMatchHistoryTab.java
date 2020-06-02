package net.boardrank.boardgame.ui.matchhistory;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.MainLayout;

@Route(value = "GameMatchHistoryTab", layout = MainLayout.class)
public class GameMatchHistoryTab extends VerticalLayout {

    public GameMatchHistoryTab(GameMatchService gameMatchService) {
        setAlignItems(Alignment.CENTER);
        addAndExpand(new GameMatchHistoryView(gameMatchService));
    }

}