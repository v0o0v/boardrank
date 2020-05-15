package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import net.boardrank.boardgame.domain.*;
import net.boardrank.boardgame.service.GameMatchService;

import java.time.format.DateTimeFormatter;

public class NoticeAcceptMatchResultView extends VerticalLayout {

    GameMatchService gameMatchService;
    
    Notice notice;

    Account me;
    
    public NoticeAcceptMatchResultView(GameMatchService gameMatchService, Notice notice, Account me) {
        this.gameMatchService = gameMatchService;
        this.notice = notice;
        this.me = me;

        setWidth("450px");
        initComponent();
    }

    private void initComponent() {
        getStyle().set("border", "1px solid #101010");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        H3 lbl_title = new H3("Match 결과 승인 요청");
        add(lbl_title);
        add(new Label(notice.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        
        add(new HorizontalLayout(
                new Button(notice.getFrom().getName(), event -> {
                })
                , new Label("님께서 Match 결과 승인을 요청하셨습니다.")
        ));
        GameMatch gameMatch = notice.getGameMatch();

        VerticalLayout match = new VerticalLayout();
        add(match);
        match.setDefaultHorizontalComponentAlignment(Alignment.START);
        match.add(new H6("■ 보드게임 : "+gameMatch.getBoardGame().getName()));
        match.add(new H6("■ 게임장소 : "+gameMatch.getPlace()));
        match.add(new H6("■ 종료시간 : "+gameMatch.getFinishedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        Grid<RankEntry> gridParty = new Grid<>();
        gridParty.setItems(gameMatch.getRankentries());
        gridParty.removeAllColumns();
        gridParty.setWidthFull();
        gridParty.addColumn(new ComponentRenderer<>(rankEntry -> {
            VerticalLayout layout = new VerticalLayout(new Button(rankEntry.getAccount().getName()));
            layout.setDefaultHorizontalComponentAlignment(Alignment.START);
            return layout;
        })).setHeader("참가자");
        gridParty.addColumn(RankEntry::getScore).setHeader("점수");
        gridParty.addColumn(RankEntry::getRank).setHeader("등수");
        gridParty.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
        });
        match.add(gridParty);
        
        add(new HorizontalLayout(
                new Button("거절하기", event -> {
                    gameMatchService.handleMatchAccept(notice, NoticeResponse.Deny, me);
                    getUI().get().close();
                    getUI().get().getPage().reload();
                }),
                new Button("수락하기", event -> {
                    gameMatchService.handleMatchAccept(notice, NoticeResponse.Accept, me);
                    getUI().get().close();
                    getUI().get().getPage().reload();
                })
        ));
    }

}