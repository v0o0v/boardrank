package net.boardrank.boardgame.ui.common;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.matchhistory.ClosedMatchDialog;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class GameMatchGrid extends Grid<GameMatch> {

    public GameMatchGrid(GameMatchService gameMatchService) {

        removeAllColumns();

        addColumn(match -> {
            return match.getBoardGame().getName();
        }).setHeader("보드게임");

        addColumn(match -> {
            return match.getRankentries().size();
        }).setHeader("인원").setKey("인원");

        addColumn(new ComponentRenderer<>(gameMatch -> {
            VerticalLayout layout = new VerticalLayout();
            layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
            gameMatch.getWinnerList().forEach(account -> {
                layout.add(new UserButton(gameMatchService, account));
            });
            return layout;
        })).setHeader("우승");

        addColumn(new LocalDateRenderer<GameMatch>(
                gameMatch -> {
                    return gameMatch.getStartedTime().toLocalDate();
                }
                , DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)))
                .setHeader("날짜").setKey("날짜");

        addColumn(match -> {
            return match.getPlayingTime();
        }).setHeader("Time(min)").setKey("플탐");

        getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
            col.setSortable(true);
        });

        addItemClickListener(event -> {
            GameMatch match = event.getItem();
            ClosedMatchDialog dialog = new ClosedMatchDialog(gameMatchService, match);
            dialog.open();
        });

        addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }
    
    public void setCompact(boolean isCompact){
        if(isCompact){
            addThemeVariants(GridVariant.LUMO_COMPACT);
            getColumnByKey("인원").setVisible(false);
            getColumnByKey("플탐").setVisible(false);
        }else {
            removeThemeVariants(GridVariant.LUMO_COMPACT);
            getColumnByKey("인원").setVisible(true);
            getColumnByKey("플탐").setVisible(true);
        }
    }

}