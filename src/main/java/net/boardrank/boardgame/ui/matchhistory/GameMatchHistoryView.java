package net.boardrank.boardgame.ui.matchhistory;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.GameMatchStatus;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.common.GameMatchGrid;
import net.boardrank.boardgame.ui.common.ResponsiveVerticalLayout;

public class GameMatchHistoryView extends ResponsiveVerticalLayout {

    GameMatchService gameMatchService;

    private Grid<GameMatch> grid;

    public GameMatchHistoryView(GameMatchService gameMatchService) {
        this.gameMatchService = gameMatchService;

        setMargin(false);
        setPadding(false);

        initComponent();
        initResposive();
        updateList();
    }

    private void initComponent() {
        configureGrid();
        addAndExpand(grid);
    }

    private void initResposive() {
        UI.getCurrent().getPage().addBrowserWindowResizeListener(event -> {

            switch (screenType) {
                case SMALL:
                    grid.getColumnByKey("인원").setVisible(false);
                    grid.getColumnByKey("플탐").setVisible(false);
                    break;
                case MEDIUM:
                case LARGE:
                    grid.getColumnByKey("인원").setVisible(true);
                    grid.getColumnByKey("플탐").setVisible(true);
                    break;
            }
        });
    }

    private void configureGrid() {
        grid = new GameMatchGrid(gameMatchService);
    }

    private void updateList() {
        grid.setItems(this.gameMatchService
                .getGamesOfCurrentSessionAccountOnGameStatus(GameMatchStatus.resultAccepted));
    }
}