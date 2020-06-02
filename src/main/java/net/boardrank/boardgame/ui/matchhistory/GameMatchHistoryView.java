package net.boardrank.boardgame.ui.matchhistory;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.GameMatchStatus;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.ResponsiveVerticalLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class GameMatchHistoryView extends ResponsiveVerticalLayout {

    GameMatchService gameMatchService;

    private Grid<GameMatch> grid = new Grid<>(GameMatch.class);

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
        grid.removeAllColumns();

        grid.addColumn(match -> {
            return match.getBoardGame().getName();
        }).setHeader("보드게임");

        grid.addColumn(match -> {
            return match.getRankentries().size();
        }).setHeader("인원").setKey("인원");

        grid.addColumn(match -> {
            return match.getWinnerByString();
        }).setHeader("우승");

        grid.addColumn(new LocalDateRenderer<GameMatch>(
                gameMatch -> {
                    return gameMatch.getStartedTime().toLocalDate();
                }
                ,DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)))
                .setHeader("날짜").setKey("날짜");

        grid.addColumn(match -> {
            return match.getPlayingTime();
        }).setHeader("Time(min)").setKey("플탐");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
            col.setSortable(true);
        });

        grid.addThemeVariants(
//                GridVariant.LUMO_COMPACT
//                ,GridVariant.LUMO_WRAP_CELL_CONTENT
//                , GridVariant.LUMO_ROW_STRIPES
//                , GridVariant.MATERIAL_COLUMN_DIVIDERS
        );

        grid.addItemClickListener(event -> {
            GameMatch match = event.getItem();
            ClosedMatchDialog dialog = new ClosedMatchDialog(gameMatchService, match);
            dialog.open();
        });

    }

    private void updateList() {
        grid.setItems(this.gameMatchService
                .getGamesOfCurrentSessionAccountOnGameStatus(GameMatchStatus.resultAccepted));
    }
}