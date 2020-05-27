package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.domain.Notice;
import net.boardrank.boardgame.domain.NoticeResponse;

import java.time.format.DateTimeFormatter;

public class NoticeRequestFriendView extends ResponsiveVerticalLayout {

    AccountService accountService;

    Notice notice;

    public NoticeRequestFriendView(AccountService accountService,Notice notice) {
        this.accountService = accountService;
        this.notice = notice;

        initComponent();
    }

    private void initComponent() {
//        getStyle().set("border", "1px solid #101010");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        H3 lbl_freq = new H3("친구 요청");
        add(lbl_freq);
        add(new Label(notice.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        add(new HorizontalLayout(
                new Button(notice.getFrom().getName(), event -> {
                })
                , new Label("님께서 친구 요청을 하셨습니다.")
        ));
        add(new HorizontalLayout(
                new Button("거절하기", event -> {
                    accountService.handleRequestFriend(notice, NoticeResponse.Deny);
                    getUI().get().close();
                    getUI().get().getPage().reload();
                }),
                new Button("수락하기", event -> {
                    accountService.handleRequestFriend(notice, NoticeResponse.Accept);
                    getUI().get().close();
                    getUI().get().getPage().reload();
                })
        ));

        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        add(progressBar);
    }

}