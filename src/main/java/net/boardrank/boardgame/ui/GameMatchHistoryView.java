package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.GameMatchStatus;
import net.boardrank.boardgame.service.GameMatchService;

public class GameMatchHistoryView extends ResponsiveVerticalLayout {

    GameMatchService gameMatchService;

    private Grid<GameMatch> grid = new Grid<>(GameMatch.class);

    public GameMatchHistoryView(GameMatchService gameMatchService) {
        this.gameMatchService = gameMatchService;
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
                    grid.getColumnByKey("종료시간").setVisible(false);
                    break;
                case MEDIUM:
                case LARGE:
                    grid.getColumnByKey("종료시간").setVisible(true);
                    break;
            }
        });
    }

    private void configureGrid() {
        grid.removeAllColumns();

        grid.addColumn(match -> {
            return match.getBoardGame().getName();
        }).setHeader("보드게임");

        grid.addColumn(match -> {
            return match.getRankentries().size();
        }).setHeader("방인원");

        grid.addColumn(match -> {
            return match.getWinnerByString();
        }).setHeader("1등");

        grid.addColumn(new LocalDateTimeRenderer<>(
                GameMatch::getStartedTime,
                "yyyy-MM-dd HH:mm"))
                .setHeader("시작시간").setKey("종료시간");

        grid.addColumn(match -> {
            return match.getPlayingTime();
        }).setHeader("플레이시간(분)");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
            col.setSortable(true);
        });

        grid.addThemeVariants(
                GridVariant.LUMO_COMPACT
                ,GridVariant.LUMO_WRAP_CELL_CONTENT
                , GridVariant.LUMO_ROW_STRIPES
                , GridVariant.MATERIAL_COLUMN_DIVIDERS
        );

    }

    private void updateList() {
        grid.setItems(this.gameMatchService.getGamesOfCurrentSessionAccountOnGameStatus(GameMatchStatus.resultAccepted));
    }
}