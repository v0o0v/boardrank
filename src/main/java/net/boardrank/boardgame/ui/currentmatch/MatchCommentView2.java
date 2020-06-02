package net.boardrank.boardgame.ui.currentmatch;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
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

public class MatchCommentView2 extends VerticalLayout {

    GameMatchService gameMatchService;

    Long gameMatchId;

    Grid<Comment> commentGrid;

    public MatchCommentView2(GameMatchService gameMatchService, Long gameMatchId) {
        this.gameMatchService = gameMatchService;
        this.gameMatchId = gameMatchId;

        setSizeFull();
        setSpacing(false);
        setMargin(false);
        setPadding(false);

        initComponent();
        resetComment();
    }

    private void initComponent() {
        add(initCommentGrid());
        add(initCommentInputLayout());
    }

    private Component initCommentInputLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        TextField textField = new TextField();
        textField.setMinLength(1);
        textField.setMaxLength(500);
        layout.addAndExpand(textField);
        Button button = new Button("입력");
        button.addClickListener(event -> {
            if (textField.isInvalid() || textField.isEmpty()) return;
            gameMatchService.addComment(gameMatchId
                    , gameMatchService.getAccountService().getCurrentAccount()
                    , textField.getValue());
            UI.getCurrent().getPage().reload();
        });
        layout.add(button);

        return layout;
    }

    private Component initCommentGrid() {
        commentGrid = new Grid<>(Comment.class);
        commentGrid.removeAllColumns();
        commentGrid.setWidthFull();

        commentGrid.addColumn(TemplateRenderer.<Comment>of(
                "<div style='" +
                        "padding: 1px; " +
                        "font-size : 1em;" +
                        "width: 100%;" +
                        "box-sizing: border-box;" +
                        "min-height: 50px;" +
                        "overflow-x: auto;" +
                        "overflow-y: auto;" +
                        "word-break: break-all;" +
                        "'>"
                        + "<div>" +
                        "<span>[[item.name]]</span>  " +
                        "<span>[[item.date]]</span>" +
                        "</div>"
                        + "<p>[[item.content]]</p>"
                        + "</div>")
                .withProperty("name", Comment::getAccountName)
                .withProperty("date", Comment::getCreatedAt)
                .withProperty("content", Comment::getContent)
        ).setHeader("Comments");

        return commentGrid;
    }

    private void resetComment() {
        List<Comment> comments = this.gameMatchService.getCommentsByMatchId(this.gameMatchId);
        this.commentGrid.setItems(comments);
    }
}