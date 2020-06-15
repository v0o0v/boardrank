package net.boardrank.boardgame.ui.currentmatch;

import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.Comment;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.service.TimeUtilService;
import net.boardrank.boardgame.ui.common.UserButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentView extends VerticalLayout {

    GameMatchService gameMatchService;
    Comment comment;

    public CommentView(GameMatchService gameMatchService, Comment comment) {
        this.gameMatchService = gameMatchService;
        this.comment = comment;

        setSpacing(false);
        setMargin(false);
        setPadding(false);

        initComponent();
    }

    private void initComponent() {
        HorizontalLayout info = new HorizontalLayout();

        Account account = gameMatchService.getAccountService().getAccount(comment.getAccountId());
        info.add(new UserButton(
                gameMatchService
                , account
        ));

        info.addAndExpand(new H6(" "));
        LocalDateTime createdAt = TimeUtilService.transUTCToKTC(comment.getCreatedAt());
        info.add(new H6(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        add(info, new H6(comment.getContent()));
    }

}