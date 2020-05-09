package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.GameMatchStatus;
import net.boardrank.boardgame.domain.RankEntry;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;

import java.time.LocalDateTime;

public class MatchView extends VerticalLayout {

    private GameMatchService gameMatchService;

    private BoardgameService boardgameService;

    private GameMatch gameMatch;

    private Button btn_changeMatchStatus;

    private ComboBox<Boardgame> combo_boardgame;
    private TextField txt_place;
    private DatePicker startDate;
    private TimePicker startTime;
    private DatePicker finishedDate;
    private TimePicker finishedTime;
    private Grid<RankEntry> gridParty;

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
                    this.gameMatch = gameMatchService.setGameMatchStatus(gameMatch, GameMatchStatus.proceeding);
                    this.gameMatch = gameMatchService.setStartTime(gameMatch, LocalDateTime.now());
                    break;
                case proceeding:
                    this.gameMatch = gameMatchService.setGameMatchStatus(gameMatch, GameMatchStatus.finished);
                    this.gameMatch = gameMatchService.setFinishTime(gameMatch, LocalDateTime.now());
                    break;
                case finished:
                    break;
                case resultAccepted:
            }
            applyGameStatus();
            resetValue();
        });
    }

    private void resetValue(){
        startDate.clear();
        if(gameMatch.getStartedTime()!=null)
            startDate.setValue(gameMatch.getStartedTime().toLocalDate());
        startTime.clear();
        if(gameMatch.getStartedTime()!=null)
            startTime.setValue(gameMatch.getStartedTime().toLocalTime());
        finishedDate.clear();
        if (gameMatch.getFinishedTime() != null)
            finishedDate.setValue(gameMatch.getStartedTime().toLocalDate());
        finishedTime.clear();
        if (gameMatch.getFinishedTime() != null)
            finishedTime.setValue(gameMatch.getStartedTime().toLocalTime());
    }

    private void initComponent() {

        //위 사이드
        VerticalLayout layout_up = new VerticalLayout();
        add(layout_up);
        layout_up.setMargin(false);
        layout_up.setPadding(false);
        layout_up.setSpacing(false);
        ////타이틀
        VerticalLayout layout_title = new VerticalLayout();
        layout_up.add(layout_title);
        layout_title.setMargin(false);
        layout_title.setPadding(false);
        layout_title.setSpacing(false);
        layout_title.setAlignItems(Alignment.CENTER);
        layout_title.addAndExpand(new H2(gameMatch.getMatchTitle()));

//        ==============================================
//        TextField txt_title = new TextField();
//        txt_title.setValue(gameMatch.getMatchTitle());
//        txt_title.setTitle("Match 이름");
//        txt_title.setWidthFull();
//        txt_title.setMaxWidth("500px");
//        txt_title.setMaxLength(20);
//        txt_title.setMinLength(1);
//        layout_title.addAndExpand(txt_title);
//        ==============================================


        //중간 사이드
        Board layout_mid = new Board();
        add(layout_mid);
        Row row = new Row();
        layout_mid.add(row);
        ////아래왼쪽 사이드
        VerticalLayout layout_down_left = new VerticalLayout();
        row.add(layout_down_left);
        layout_down_left.setAlignItems(Alignment.STRETCH);
        layout_down_left.setSizeFull();
        //////보드게임
        HorizontalLayout layout_boardgame = new HorizontalLayout();
        layout_down_left.add(layout_boardgame);
        layout_boardgame.setSizeFull();
        layout_boardgame.setAlignItems(Alignment.STRETCH);
        layout_boardgame.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        combo_boardgame = new ComboBox<>();
        combo_boardgame.setLabel("보드게임");
        combo_boardgame.setItems(boardgameService.getAllBoardgame());
        combo_boardgame.setValue(gameMatch.getBoardGame());
        layout_boardgame.addAndExpand(combo_boardgame);
        layout_boardgame.setSizeFull();
        //////장소
        HorizontalLayout layout_place = new HorizontalLayout();
        layout_down_left.add(layout_place);
        layout_place.setAlignItems(Alignment.STRETCH);
        layout_place.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        txt_place = new TextField();
        txt_place.setLabel("장소");
        txt_place.setWidthFull();
        txt_place.setValue("어디어디 보드게임방~");
        layout_place.addAndExpand(txt_place);
        //////시작시간
        HorizontalLayout layout_startTime = new HorizontalLayout();
        layout_down_left.add(layout_startTime);
        layout_down_left.setSizeFull();
        layout_startTime.setAlignItems(Alignment.AUTO);
        startDate = new DatePicker("시작 날짜");
        layout_startTime.add(startDate);
        startDate.setWidthFull();
        startTime = new TimePicker("시작 시간");
        layout_startTime.add(startTime);
        startTime.setWidthFull();
        //////종료시간
        HorizontalLayout layout_finishedTime = new HorizontalLayout();
        layout_down_left.add(layout_finishedTime);
        layout_finishedTime.setSizeFull();
        layout_finishedTime.setAlignItems(Alignment.STRETCH);
        finishedDate = new DatePicker("종료 날짜");
        layout_finishedTime.addAndExpand(finishedDate);
        finishedTime = new TimePicker("종료 시간");
        layout_finishedTime.addAndExpand(finishedTime);
        ////아래오른쪽 사이드
        VerticalLayout layout_down_right = new VerticalLayout();
        row.add(layout_down_right);
        //////참가자
        VerticalLayout layout_party = new VerticalLayout();
        layout_down_right.add(layout_party);
        layout_party.setAlignItems(Alignment.AUTO);

        gridParty = new Grid<>();
        layout_party.add(gridParty);
        gridParty.setItems(gameMatch.getRankentries());
        gridParty.removeAllColumns();

        gridParty.addColumn(new ComponentRenderer<>(rankEntry -> {
            Button friend = new Button(rankEntry.getAccount().getName());
            return friend;
        })).setHeader("참가자");

        gridParty.addColumn(rankEntry -> {
            return rankEntry.getRank();
        }).setHeader("등수").setSortable(true);

        gridParty.addColumn(rankEntry -> {
            return rankEntry.getScore();
        }).setHeader("점수").setSortable(true);

        gridParty.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
        });

        //Bottom 사이드
        VerticalLayout layout_bottom = new VerticalLayout();
        add(layout_bottom);
        layout_bottom.setAlignItems(Alignment.CENTER);
        btn_changeMatchStatus = new Button();
        btn_changeMatchStatus.setWidthFull();
        btn_changeMatchStatus.setMaxWidth("500px");
        layout_bottom.addAndExpand(btn_changeMatchStatus);
    }

    private void setEditable(boolean editable) {
        this.txt_place.setEnabled(editable);
        this.combo_boardgame.setEnabled(editable);
        this.startDate.setEnabled(editable);
        this.startTime.setEnabled(editable);
        this.finishedDate.setEnabled(editable);
        this.finishedTime.setEnabled(editable);
    }

    private void applyGameStatus() {
        switch (gameMatch.getGameMatchStatus()) {
            case init:
                this.btn_changeMatchStatus.setText("게임 시작 하기");
                this.btn_changeMatchStatus.setEnabled(true);
                this.setEditable(true);
                break;
            case proceeding:
                this.btn_changeMatchStatus.setText("게임 종료. 결과 입력 하기");
                this.btn_changeMatchStatus.setEnabled(true);
                this.setEditable(true);
                break;
            case finished:
                this.btn_changeMatchStatus.setText("결과 승인 하기");
                this.btn_changeMatchStatus.setEnabled(true);
                this.setEditable(false);
                break;
            case resultAccepted:
                this.btn_changeMatchStatus.setText("게임 결과 승인 완료");
                this.btn_changeMatchStatus.setEnabled(false);
                this.setEditable(false);
        }
    }

    private void initLayout() {
        UI.getCurrent().getPage().setTitle("BoardRank");
        getStyle().set("border", "2px solid #101010");
        getStyle().set("margin-bottom", "50px");
    }

}



