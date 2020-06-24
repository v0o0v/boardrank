package net.boardrank.boardgame.ui.currentmatch;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import net.boardrank.boardgame.domain.Comment;
import net.boardrank.boardgame.service.GameMatchService;

import java.util.List;

public class MatchCommentListView extends VerticalLayout {

    GameMatchService gameMatchService;

    Long gameMatchId;

    VerticalLayout layout_commentList = new VerticalLayout();

    HorizontalLayout layout_commentInput = new HorizontalLayout();

    public MatchCommentListView(GameMatchService gameMatchService, Long gameMatchId) {
        this.gameMatchService = gameMatchService;
        this.gameMatchId = gameMatchId;

        setSizeFull();
        setSpacing(false);
        setMargin(true);
        setPadding(false);

        initComponent();
        reset();
    }

    private void initComponent() {
        add(new H5("Comments"));
        initCommentListView();
        initCommentInputLayout();

        setMaxHeight("400px");
    }

    private void initCommentInputLayout() {
        layout_commentInput.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        TextField textField = new TextField();
        textField.setClearButtonVisible(true);
        textField.setMaxLength(500);
        layout_commentInput.addAndExpand(textField);
        Button button = new Button("입력");
        button.addClickListener(event -> {
            if (textField.isInvalid() || textField.isEmpty()) return;
            gameMatchService.addComment(gameMatchId
                    , gameMatchService.getAccountService().getCurrentAccount()
                    , textField.getValue());
            textField.clear();
            reset();
        });
        layout_commentInput.add(button);
        add(layout_commentInput);
    }

    private void initCommentListView() {
        layout_commentList.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        layout_commentList.getStyle().set("overflow-y", "auto");
        add(layout_commentList);
    }

    private void reset() {
        this.layout_commentList.removeAll();
        List<Comment> commentsByMatchId = gameMatchService.getCommentsByMatchId(gameMatchId);
        commentsByMatchId.forEach(comment -> {
            layout_commentList.add(new CommentView(gameMatchService, comment, event -> reset()));
        });
    }
}