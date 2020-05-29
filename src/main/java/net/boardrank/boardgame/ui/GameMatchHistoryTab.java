package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.service.GameMatchService;

@Route(value = "GameMatchHistoryTab", layout = MainLayout.class)
public class GameMatchHistoryTab extends VerticalLayout {

    public GameMatchHistoryTab(GameMatchService gameMatchService) {
        setAlignItems(Alignment.CENTER);
        addAndExpand(new GameMatchHistoryView(gameMatchService));
    }

}