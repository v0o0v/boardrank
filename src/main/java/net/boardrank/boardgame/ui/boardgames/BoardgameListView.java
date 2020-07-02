package net.boardrank.boardgame.ui.boardgames;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.common.ResponsiveVerticalLayout;

public class BoardgameListView extends ResponsiveVerticalLayout {

    final private GameMatchService gameMatchService;

    private Grid<Boardgame> grid = new Grid<>(Boardgame.class);

    public BoardgameListView(GameMatchService gameMatchService) {
        this.gameMatchService = gameMatchService;

        setMargin(false);
        setPadding(false);

        configureGrid();
        add(grid);
        updateList();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();

        grid.addColumn(boardgame -> {
            return boardgame.getName();
        }).setHeader("이름");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
            col.setSortable(true);
        });
    }

    private void updateList() {
        grid.setItems(gameMatchService.getBoardgameService().getAllBoardgame());
    }
}