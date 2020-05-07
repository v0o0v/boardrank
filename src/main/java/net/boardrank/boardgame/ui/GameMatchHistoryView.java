package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;

@Route(value = "GameMatchHistoryView", layout = MainLayout.class)
public class GameMatchHistoryView extends VerticalLayout {

    GameMatchService gameMatchService;

    private Grid<GameMatch> grid = new Grid<>(GameMatch.class);

    public GameMatchHistoryView(GameMatchService gameMatchService) {
        this.gameMatchService = gameMatchService;

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();

        add(content);
        updateList();

        initResposive();
    }

    private void initResposive() {
        UI.getCurrent().getPage().addBrowserWindowResizeListener(event -> {
            int height = event.getHeight();
            int width = event.getWidth();

            if(width<=800){
                grid.getColumnByKey("종료시간").setVisible(false);
                grid.getColumnByKey("방이름").setVisible(false);
            }else{
                grid.getColumnByKey("종료시간").setVisible(true);
                grid.getColumnByKey("방이름").setVisible(true);
            }

        });
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.removeAllColumns();

        grid.addColumn(match -> {
            return match.getBoardGame().getName();
        }).setHeader("보드게임");

        grid.addColumn(match -> {
            return match.getMatchTitle();
        }).setHeader("방이름").setKey("방이름");

        grid.addColumn(match -> {
            return match.getWinnerByString();
        }).setHeader("1등");

        grid.addColumn(match -> {
            return match.getPaticiant().getAccounts().size();
        }).setHeader("방인원");

        grid.addColumn(new LocalDateTimeRenderer<>(
                GameMatch::getFinishedTime,
                "yyyy-MM-dd HH:mm"))
                .setHeader("종료시간").setKey("종료시간");

        grid.addColumn(match -> {
            return match.getPlayingTime();
        }).setHeader("플레이시간(분)");

//        grid.setColumns("보드게임", "방이름", "1등", "방인원", "방상태", "시작시간", "완료시간");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
            col.setSortable(true);

        });

    }

    private void updateList() {
        grid.setItems(this.gameMatchService.getGamesOfCurrentSessionAccount());
    }
}