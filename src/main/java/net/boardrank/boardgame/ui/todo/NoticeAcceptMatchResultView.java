package net.boardrank.boardgame.ui.todo;

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
import net.boardrank.boardgame.ui.common.ResponsiveVerticalLayout;
import net.boardrank.boardgame.ui.common.UserButton;

import java.time.format.DateTimeFormatter;

public class NoticeAcceptMatchResultView extends ResponsiveVerticalLayout {

    GameMatchService gameMatchService;

    Notice notice;

    Account me;

    public NoticeAcceptMatchResultView(GameMatchService gameMatchService, Notice notice, Account me) {
        this.gameMatchService = gameMatchService;
        this.notice = notice;
        this.me = me;

        initComponent();
    }

    private void initComponent() {
        getStyle().set("border", "1px solid #e0e0e0");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        H3 lbl_title = new H3("Match 결과 승인 요청");
        add(lbl_title);
        add(new Label(notice.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        add(new HorizontalLayout(
                new UserButton(gameMatchService, notice.getFrom())
                , new Label("님께서 Match 결과 승인을 요청하셨습니다.")
        ));
        GameMatch gameMatch = notice.getGameMatch();

        VerticalLayout match = new VerticalLayout();
        add(match);
        match.setDefaultHorizontalComponentAlignment(Alignment.START);
        match.add(new H6("■ 보드게임 : " + gameMatch.getFullBoardgameString()));
        match.add(new H6("■ 종료시간 : " + gameMatch.getFinishedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        Grid<RankEntry> gridParty = new Grid<>();
        gridParty.removeAllColumns();
        gridParty.setWidthFull();
        gridParty.addColumn(new ComponentRenderer<>(rankEntry -> {
            VerticalLayout layout = new VerticalLayout(new UserButton(gameMatchService,rankEntry.getAccount()));
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
        gridParty.setItems(gameMatch.getRankentries());
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