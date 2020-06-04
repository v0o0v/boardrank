package net.boardrank.boardgame.ui.currentmatch;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import net.boardrank.boardgame.domain.Comment;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.service.TimeUtilService;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

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
        info.add(new H6(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        add(info, new H6(comment.getContent()));
    }

}