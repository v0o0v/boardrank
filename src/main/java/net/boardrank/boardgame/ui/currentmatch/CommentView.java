package net.boardrank.boardgame.ui.currentmatch;

import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import net.boardrank.boardgame.domain.Comment;
import net.boardrank.boardgame.service.TimeUtilService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentView extends VerticalLayout {

    Comment comment;

    public CommentView(Comment comment) {
        this.comment = comment;

        setSpacing(false);
        setMargin(false);
        setPadding(false);

        initComponent();
    }

    private void initComponent() {
        HorizontalLayout info = new HorizontalLayout();
        info.add(new H6(comment.getAccountName()));
        info.addAndExpand(new H6(" "));
        LocalDateTime createdAt = TimeUtilService.transUTCToKTC(comment.getCreatedAt());
        info.add(new H6(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        add(info, new H6(comment.getContent()));
    }

}