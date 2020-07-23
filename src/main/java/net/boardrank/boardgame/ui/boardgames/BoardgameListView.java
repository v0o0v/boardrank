package net.boardrank.boardgame.ui.boardgames;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.NewBoardGameDialog;
import net.boardrank.boardgame.ui.common.ResponsiveVerticalLayout;

public class BoardgameListView extends ResponsiveVerticalLayout {

    final private GameMatchService gameMatchService;

    private Grid<Boardgame> boardgameGrid = new Grid<>(Boardgame.class);

    public BoardgameListView(GameMatchService gameMatchService) {
        this.gameMatchService = gameMatchService;

        setMargin(false);
        setPadding(false);

        configureGrid();
        add(this.initAddNewBoardgame());
        add(boardgameGrid);
        updateList();
    }

    private void configureGrid() {
        boardgameGrid.setSizeFull();
        boardgameGrid.removeAllColumns();

        boardgameGrid.addColumn(Boardgame::getName).setHeader("이름");

        boardgameGrid.addColumn(Boardgame::getBase).setHeader("기본판");

        boardgameGrid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
            col.setSortable(true);
        });
    }

    private Component initAddNewBoardgame() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setMargin(false);
        layout.setSpacing(false);
        Button btn_addNewBoardgame = new Button("신규 보드게임 추가", event -> {
            NewBoardGameDialog newBoardGameDialog = new NewBoardGameDialog(
                    gameMatchService.getBoardgameService()
                    , gameMatchService.getAccountService()
                    , event1 -> updateList()
            );
            newBoardGameDialog.open();
        });
        layout.add(btn_addNewBoardgame);
        layout.setHorizontalComponentAlignment(Alignment.END, btn_addNewBoardgame);
        return layout;
    }

    private void updateList() {
        boardgameGrid.setItems(gameMatchService.getBoardgameService().getAllBoardgame());
    }
}