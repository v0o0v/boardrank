package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;

import java.util.concurrent.atomic.AtomicInteger;

public class MatchView extends VerticalLayout {

    private GameMatchService gameMatchService;

    private BoardgameService boardgameService;

    private GameMatch gameMatch;

    Button btn_matchStatus;

    public MatchView(GameMatchService gameMatchService, GameMatch gameMatch, BoardgameService boardgameService) {
        this.gameMatchService = gameMatchService;
        this.boardgameService = boardgameService;
        this.gameMatch = gameMatch;

        initLayout();
        initComponent();
        applyGameStatus();
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
        TextField txt_title = new TextField();
        txt_title.setValue(gameMatch.getMatchTitle());
        txt_title.setTitle("Match 이름");
        txt_title.setWidthFull();
        txt_title.setMaxWidth("500px");
        txt_title.setMaxLength(20);
        txt_title.setMinLength(1);
        layout_title.addAndExpand(txt_title);

        //중간 사이드
        Board layout_mid = new Board();
        add(layout_mid);
        Row row = new Row();
        layout_mid.add(row);
        ////아래왼쪽 사이드
        VerticalLayout layout_down_left = new VerticalLayout();
        row.add(layout_down_left);
        layout_down_left.getStyle().set("border", "1px solid #101010");
        layout_down_left.setAlignItems(Alignment.STRETCH);
        //////보드게임
        HorizontalLayout layout_boardgame = new HorizontalLayout();
        layout_down_left.add(layout_boardgame);
        layout_boardgame.setSizeFull();
        layout_boardgame.setAlignItems(Alignment.STRETCH);
        Label lbl_boardgame = new Label("보드게임: ");
        layout_boardgame.add(lbl_boardgame);
        layout_boardgame.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        ComboBox<Boardgame> combo_boardgame = new ComboBox<>();
        combo_boardgame.setItems(boardgameService.getAllBoardgame());
        combo_boardgame.setValue(gameMatch.getBoardGame());
        layout_boardgame.addAndExpand(combo_boardgame);
        layout_boardgame.setSizeFull();
        //////장소
        HorizontalLayout layout_place = new HorizontalLayout();
        layout_down_left.add(layout_place);
        layout_place.setAlignItems(Alignment.STRETCH);
        Label lbl_place = new Label("　　장소: ");
        layout_place.add(lbl_place);
        layout_place.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Button btn_place = new Button("어디어디 보드게임방");
        layout_place.addAndExpand(btn_place);
        //////시작시간
        HorizontalLayout layout_startTime = new HorizontalLayout();
        layout_down_left.add(layout_startTime);
        layout_down_left.setSizeFull();
        layout_startTime.setAlignItems(Alignment.STRETCH);
        DatePicker startDate = new DatePicker("시작 날짜");
        layout_startTime.addAndExpand(startDate);
        startDate.setValue(gameMatch.getStartedTime().toLocalDate());
        TimePicker startTime = new TimePicker("시작 시간");
        layout_startTime.addAndExpand(startTime);
        startTime.setValue(gameMatch.getStartedTime().toLocalTime());
        //////종료시간
        HorizontalLayout layout_finishedTime = new HorizontalLayout();
        layout_down_left.add(layout_finishedTime);
        layout_finishedTime.setSizeFull();
        layout_finishedTime.setAlignItems(Alignment.STRETCH);
        DatePicker finishedDate = new DatePicker("종료 날짜");
        layout_finishedTime.addAndExpand(finishedDate);
        finishedDate.clear();
        if (gameMatch.getFinishedTime() != null)
            finishedDate.setValue(gameMatch.getStartedTime().toLocalDate());
        TimePicker finishedTime = new TimePicker("종료 시간");
        layout_finishedTime.addAndExpand(finishedTime);
        finishedTime.clear();
        if (gameMatch.getFinishedTime() != null)
            finishedTime.setValue(gameMatch.getStartedTime().toLocalTime());

        ////아래오른쪽 사이드
        VerticalLayout layout_down_right = new VerticalLayout();
        row.add(layout_down_right);
        layout_down_right.getStyle().set("border", "1px solid #101010");
        //////참가자
        VerticalLayout layout_party = new VerticalLayout();
        layout_down_right.add(layout_party);
        layout_party.setMargin(false);
        layout_party.setPadding(false);
        layout_party.setSpacing(false);
        layout_party.getStyle().set("border", "1px");
        layout_party.setAlignItems(Alignment.AUTO);
        AtomicInteger partyNum = new AtomicInteger(1);
        gameMatch.getPaticiant().getAccounts().stream()
                .forEach(account -> {
                    Button button = new Button(account.getName());
                    button.addClickListener(event -> {
                        //TODO
                    });
                    HorizontalLayout layout_eachParty = new HorizontalLayout();
                    layout_party.add(layout_eachParty);
                    layout_eachParty.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    layout_eachParty.add(new Label("참가자" + (partyNum.getAndIncrement()) + ":"));
                    layout_eachParty.addAndExpand(button);
                });

        //Bottom 사이드
        VerticalLayout layout_bottom = new VerticalLayout();
        add(layout_bottom);
        layout_bottom.setAlignItems(Alignment.CENTER);
//        layout_bottom.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        btn_matchStatus = new Button();
        btn_matchStatus.setWidthFull();
        btn_matchStatus.setMaxWidth("500px");
        layout_bottom.addAndExpand(btn_matchStatus);
    }

    private void applyGameStatus() {
        switch (gameMatch.getGameMatchStatus()) {
            case init:
                this.btn_matchStatus.setText("게임 시작");
                break;
            case proceeding:
                this.btn_matchStatus.setText("게임 종료");
                break;
            case finished:
                this.btn_matchStatus.setText("결과 승인");
                break;
            case resultAccepted:
                this.btn_matchStatus.setText("게임 완료");
                this.btn_matchStatus.setEnabled(false);
        }
    }

    private void initLayout() {
        getStyle().set("border", "2px solid #101010");
        getStyle().set("margin-bottom", "50px");
//        getStyle().set("border", "1px solid #9E9E9E");
//        setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
//        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
    }

}


