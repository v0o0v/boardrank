package net.boardrank.boardgame.ui.matchhistory;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.RankEntry;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.ResponsiveDialog;
import net.boardrank.boardgame.ui.currentmatch.MatchCommentView;

import java.util.ArrayList;
import java.util.List;

public class ClosedMatchDialog extends ResponsiveDialog {

    private GameMatchService gameMatchService;
    private GameMatch gameMatch;

    private ComboBox<Boardgame> combo_boardgame = new ComboBox<>();
    private List<ComboBox<Boardgame>> expansionBoardgameComboList = new ArrayList<>();
    private DatePicker startDate = new DatePicker();
    private TimePicker startTime = new TimePicker();
    private DatePicker finishedDate = new DatePicker();
    private TimePicker finishedTime = new TimePicker();
    private Grid<RankEntry> gridParty = new Grid<>();
    private FormLayout form;

    public ClosedMatchDialog(GameMatchService gameMatchService, GameMatch gameMatch) {
        this.gameMatchService = gameMatchService;
        this.gameMatch = gameMatch;

        initComponent();
        resetValue();
    }

    private void resetValue() {
        startDate.clear();
        if (gameMatch.getStartedTime() != null)
            startDate.setValue(gameMatch.getStartedTime().toLocalDate());
        startTime.clear();
        if (gameMatch.getStartedTime() != null)
            startTime.setValue(gameMatch.getStartedTime().toLocalTime());
        finishedDate.clear();
        if (gameMatch.getFinishedTime() != null)
            finishedDate.setValue(gameMatch.getFinishedTime().toLocalDate());
        finishedTime.clear();
        if (gameMatch.getFinishedTime() != null)
            finishedTime.setValue(gameMatch.getFinishedTime().toLocalTime());
    }

    private void initComponent() {

        form = new FormLayout();

        if (!gameMatch.getWinnerByString().isEmpty()) {
            HorizontalLayout top = new HorizontalLayout();
            top.getStyle().set("border", "2px solid #705050");
            top.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            top.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            Icon icon = new Icon(VaadinIcon.TROPHY);
            top.add(icon);
            gameMatch.getWinnerList().forEach(account -> {
                top.add(new H4(account.getName()));
            });
            form.add(top);
        }

        combo_boardgame.setReadOnly(true);
        combo_boardgame.setItems(gameMatch.getBoardGame());
        combo_boardgame.setValue(gameMatch.getBoardGame());
        combo_boardgame.setLabel("보드게임");
        form.add(combo_boardgame);
        gameMatch.getExpansions().forEach(exp -> {
            ComboBox<Boardgame> comboExp = new ComboBox<>();
            comboExp.setItems(exp);
            comboExp.setValue(exp);
            comboExp.setReadOnly(true);
            comboExp.setLabel("확장판");
            expansionBoardgameComboList.add(comboExp);
            form.add(comboExp);
        });
        form.add(this.createPartyGrid());
        startDate.setLabel("시작 날짜");
        startTime.setLabel("시작 시간");
        form.add(startDate, startTime);
        finishedDate.setLabel("종료 날짜");
        finishedTime.setLabel("종료 시간");
        form.add(finishedDate, finishedTime);
        form.add(new MatchCommentView(this.gameMatchService, this.gameMatch.getId()));

        HorizontalLayout close = new HorizontalLayout();
        close.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        close.add(new Button("닫기", event -> close()));
        add(new VerticalLayout(form),close);
    }

    private Grid createPartyGrid() {
        gridParty = new Grid<>();
        gridParty.setItems(gameMatch.getRankentries());
        gridParty.removeAllColumns();
        gridParty.addColumn(new ComponentRenderer<>(rankEntry -> {
            return new Button(rankEntry.getAccount().getName());
        })).setHeader("참가자");
        gridParty.addColumn(RankEntry::getScore).setHeader("점수");
        gridParty.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
        });
        int gridHeight = gameMatch.getRankentries().size() * 4 + 3;
        gridParty.setMaxHeight(gridHeight + "em");
        return gridParty;
    }

}



