package net.boardrank.boardgame.ui.currentmatch;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.GameMatchStatus;
import net.boardrank.boardgame.domain.RankEntry;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.service.TimeUtilService;
import net.boardrank.boardgame.ui.ResponsiveVerticalLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MatchView extends ResponsiveVerticalLayout {

    private GameMatchService gameMatchService;
    private BoardgameService boardgameService;
    private AccountService accountService;

    private GameMatch gameMatch;

    private Button btn_changeMatchStatus = new Button();
    private ComboBox<Boardgame> combo_boardgame = new ComboBox<>();
    private List<ComboBox<Boardgame>> expansionBoardgameComboList = new ArrayList<>();
    private DatePicker startDate = new DatePicker();
    private TimePicker startTime = new TimePicker();
    private DatePicker finishedDate = new DatePicker();
    private TimePicker finishedTime = new TimePicker();
    private Grid<RankEntry> gridParty = new Grid<>();
    private HorizontalLayout top;

    public MatchView(GameMatchService gameMatchService
            , GameMatch gameMatch
            , BoardgameService boardgameService
            , AccountService accountService
    ) {
        this.gameMatchService = gameMatchService;
        this.boardgameService = boardgameService;
        this.accountService = accountService;
        this.gameMatch = gameMatch;

        initLayout();
        initComponent();
        initEvent();
        applyGameStatus();
        resetValue();
        initOwnerActionEnable();
    }

    private void initOwnerActionEnable() {
        if (accountService.getCurrentAccount().equals(gameMatch.getCreatedMember())) {
            top.setEnabled(true);
        } else {
            top.setEnabled(false);
        }
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

        this.startTime.addValueChangeListener(event -> {
            try {
                LocalDateTime oldDateTime = gameMatch.getStartedTime();
                LocalTime toTime = event.getValue();
                if (oldDateTime == null || toTime == null) return;
                LocalDateTime newDateTime = LocalDateTime.of(oldDateTime.toLocalDate(), toTime);
                gameMatch.setStartedTime(TimeUtilService.transKTCToUTC(newDateTime));
                gameMatchService.save(gameMatch);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        this.startDate.addValueChangeListener(event -> {
            try {
                LocalDateTime oldDateTime = gameMatch.getStartedTime();
                LocalDate newDate = event.getValue();
                if (oldDateTime == null || newDate == null) return;
                LocalDateTime newDateTime = LocalDateTime.of(newDate, oldDateTime.toLocalTime());
                gameMatch.setStartedTime(TimeUtilService.transKTCToUTC(newDateTime));
                gameMatchService.save(gameMatch);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        this.finishedTime.addValueChangeListener(event -> {
            try {
                LocalDateTime oldDateTime = gameMatch.getFinishedTime();
                LocalTime toTime = event.getValue();
                if (oldDateTime == null || toTime == null) return;
                LocalDateTime newDateTime = LocalDateTime.of(oldDateTime.toLocalDate(), toTime);
                gameMatch.setFinishedTime(TimeUtilService.transKTCToUTC(newDateTime));
                gameMatchService.save(gameMatch);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        this.finishedDate.addValueChangeListener(event -> {
            try {
                LocalDateTime oldDateTime = gameMatch.getFinishedTime();
                LocalDate newDate = event.getValue();
                if (oldDateTime == null || newDate == null) return;
                LocalDateTime newDateTime = LocalDateTime.of(newDate, oldDateTime.toLocalTime());
                gameMatch.setFinishedTime(TimeUtilService.transKTCToUTC(newDateTime));
                gameMatchService.save(gameMatch);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        startTime.setLabel("시작 시간");
        form.add(startDate, startTime);
        finishedDate.setLabel("종료 날짜");
        finishedTime.setLabel("종료 시간");
        form.add(finishedDate, finishedTime);
        form.add(new MatchCommentView(this.gameMatchService, this.gameMatch.getId()));

        top = new HorizontalLayout();
        top.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        top.addAndExpand(btn_changeMatchStatus);
        top.add(new Button(new Icon(VaadinIcon.COG), event -> {
            new MatchDetailModifyDialog(gameMatchService, gameMatch).open();
        }));
        add(top);
        add(form);
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

    private void applyGameStatus() {
        switch (gameMatch.getGameMatchStatus()) {
            case init:
                this.btn_changeMatchStatus.setText("게임 시작 하기");
                this.btn_changeMatchStatus.setEnabled(true);
                if (accountService.getCurrentAccount().equals(gameMatch.getCreatedMember())) {
                    this.startDate.setReadOnly(true);
                    this.startTime.setReadOnly(true);
                    this.finishedDate.setReadOnly(true);
                    this.finishedTime.setReadOnly(true);
                }
                break;
            case proceeding:
                this.btn_changeMatchStatus.setText("점수입력/게임종료 하기");
                this.btn_changeMatchStatus.setEnabled(true);
                if (accountService.getCurrentAccount().equals(gameMatch.getCreatedMember())) {
                    this.startDate.setReadOnly(false);
                    this.startTime.setReadOnly(false);
                    this.finishedDate.setReadOnly(true);
                    this.finishedTime.setReadOnly(true);
                }
                break;
            case finished:
                this.btn_changeMatchStatus.setText("게임 결과 승인 대기중");
                this.btn_changeMatchStatus.setEnabled(false);
                if (accountService.getCurrentAccount().equals(gameMatch.getCreatedMember())) {
                    this.startDate.setReadOnly(false);
                    this.startTime.setReadOnly(false);
                    this.finishedDate.setReadOnly(false);
                    this.finishedTime.setReadOnly(false);
                }
                break;
            case resultAccepted:
                this.btn_changeMatchStatus.setText("게임 결과 승인 완료됨");
                this.btn_changeMatchStatus.setEnabled(false);
                if (accountService.getCurrentAccount().equals(gameMatch.getCreatedMember())) {
                    this.startDate.setReadOnly(true);
                    this.startTime.setReadOnly(true);
                    this.finishedDate.setReadOnly(true);
                    this.finishedTime.setReadOnly(true);
                }
        }
    }

    private void initLayout() {
        getStyle().set("border", "1px solid #101010");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }

}



