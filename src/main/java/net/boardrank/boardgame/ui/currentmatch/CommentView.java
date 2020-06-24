package net.boardrank.boardgame.ui.currentmatch;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.Comment;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.service.TimeUtilService;
import net.boardrank.boardgame.ui.common.UserButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class CommentView extends VerticalLayout {

    GameMatchService gameMatchService;
    Comment comment;
    ComponentEventListener afterDeleteEvent;

    public CommentView(GameMatchService gameMatchService, Comment comment, ComponentEventListener<ComponentEvent<CommentView>> afterDeleteEvent) {
        this.gameMatchService = gameMatchService;
        this.comment = comment;
        this.afterDeleteEvent = afterDeleteEvent;

        setSpacing(false);
        setMargin(false);
        setPadding(false);

        initComponent();
        setHeightFull();
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
        add(info);

        TextArea textArea = new TextArea();
        textArea.setValue(comment.getContent());
        textArea.setSizeFull();
        textArea.setReadOnly(true);
        add(textArea);

        if (gameMatchService.getAccountService().getCurrentAccount().getId().equals(comment.getAccountId())) {
            Button btn_remove = new Button("삭제");
            btn_remove.addThemeVariants(ButtonVariant.LUMO_SMALL);
            btn_remove.addClickListener(event -> {
                gameMatchService.deleteComments(Arrays.asList(this.comment));
                this.afterDeleteEvent.onComponentEvent(null);
            });
            add(btn_remove);
            setHorizontalComponentAlignment(Alignment.END, btn_remove);
        }

    }

}