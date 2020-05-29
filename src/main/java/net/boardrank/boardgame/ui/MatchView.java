package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.GameMatchStatus;
import net.boardrank.boardgame.domain.RankEntry;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MatchView extends ResponsiveVerticalLayout {

    private GameMatchService gameMatchService;
    private BoardgameService boardgameService;
    private GameMatch gameMatch;

    private Button btn_changeMatchStatus = new Button();
    private ComboBox<Boardgame> combo_boardgame = new ComboBox<>();
    private List<ComboBox<Boardgame>> expansionBoardgameComboList = new ArrayList<>();
    private DatePicker startDate = new DatePicker();
    private TimePicker startTime = new TimePicker();
    private DatePicker finishedDate = new DatePicker();
    private TimePicker finishedTime = new TimePicker();
    private Grid<RankEntry> gridParty = new Grid<>();

    private Image img_status = new Image("icons/Ready.png", "Ready");

    public MatchView(GameMatchService gameMatchService, GameMatch gameMatch, BoardgameService boardgameService) {
        this.gameMatchService = gameMatchService;
        this.boardgameService = boardgameService;
        this.gameMatch = gameMatch;

        initLayout();
        initComponent();
        initEvent();
        applyGameStatus();
        resetValue();
    }

    private void initEvent() {
        this.btn_changeMatchStatus.addClickListener(event -> {
            switch (gameMatch.getGameMatchStatus()) {
                case init:
                    toInProgress();
                    break;
                case proceeding:
                    toFinished();
                    break;
                case finished:
                    toResultAccepted();
                    break;
                case resultAccepted:
                    break;
            }
            applyGameStatus();
            resetValue();
        });
    }

    private void toResultAccepted() {
        this.gameMatch = gameMatchService.setGameMatchStatus(gameMatch, GameMatchStatus.resultAccepted);
    }

    private void toFinished() {
        ScoreInputDialog scoreInputDialog = new ScoreInputDialog(gameMatch, event -> {
            this.gameMatch = gameMatchService.setGameMatchStatus(gameMatch, GameMatchStatus.finished);
            this.gameMatch = gameMatchService.setFinishTime(gameMatch, LocalDateTime.now());
            this.gameMatch = gameMatchService.save(gameMatch);
            gridParty.setItems(gameMatch.getRankentries());
            applyGameStatus();
            resetValue();
        });
        scoreInputDialog.open();
    }

    private void toInProgress() {
        this.gameMatch = gameMatchService.setGameMatchStatus(gameMatch, GameMatchStatus.proceeding);
        this.gameMatch = gameMatchService.setStartTime(gameMatch, LocalDateTime.now());
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

        FormLayout form = new FormLayout();

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
        form.add(startDate);
        startTime.setLabel("시작 시간");
        form.add(startTime);
        finishedDate.setLabel("종료 날짜");
        form.add(finishedDate);
        finishedTime.setLabel("종료 시간");
        form.add(finishedTime);

        add(form);
        add(btn_changeMatchStatus);
        add(new MatchCommentView(this.gameMatchService, this.gameMatch.getId()));
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

    private void setEditable(boolean editable) {
        this.combo_boardgame.setReadOnly(!editable);
        this.startDate.setReadOnly(!editable);
        this.startTime.setReadOnly(!editable);
        this.finishedDate.setReadOnly(!editable);
        this.finishedTime.setReadOnly(!editable);
    }

    private void applyGameStatus() {
        switch (gameMatch.getGameMatchStatus()) {
            case init:
                this.btn_changeMatchStatus.setText("게임 시작 하기");
                this.btn_changeMatchStatus.setEnabled(true);
                this.setEditable(false);
                this.img_status.setSrc("icons/Ready.png");
                break;
            case proceeding:
                this.btn_changeMatchStatus.setText("점수입력/게임종료 하기");
                this.btn_changeMatchStatus.setEnabled(true);
                this.setEditable(false);
                this.img_status.setSrc("icons/In-Progress.png");
                break;
            case finished:
                this.btn_changeMatchStatus.setText("게임 결과 승인 대기중");
                this.btn_changeMatchStatus.setEnabled(false);
                this.setEditable(false);
                this.img_status.setSrc("icons/Waiting-Result.png");
                break;
            case resultAccepted:
                this.btn_changeMatchStatus.setText("게임 결과 승인 완료됨");
                this.btn_changeMatchStatus.setEnabled(false);
                this.setEditable(false);
                this.img_status.setSrc("icons/Finished.png");
        }
    }

    private void initLayout() {
        getStyle().set("border", "1px solid #101010");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }

}



