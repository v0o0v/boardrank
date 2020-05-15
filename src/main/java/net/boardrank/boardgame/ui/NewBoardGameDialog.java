package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.RankEntry;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.ui.event.DialogSuccessCloseActionEvent;

import java.util.Set;

@Slf4j
public class NewBoardGameDialog extends Dialog {

    private BoardgameService boardgameService;

    private AccountService accountService;

    private Board board = new Board();
    private TextField txt_boardgame;

    public NewBoardGameDialog(BoardgameService boardgameService
            , AccountService accountService
            , ComponentEventListener<DialogSuccessCloseActionEvent> listener) {

        this.boardgameService = boardgameService;
        this.accountService = accountService;
        getEventBus().addListener(DialogSuccessCloseActionEvent.class, listener);

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        createHeader();
        createContent();
        createFooter();

        setWidth("480px");
        board.setSizeFull();
        add(board);
    }

    private void createHeader() {
        board.addRow(new H5("보드 게임 추가"));
    }

    private void createContent() {

        Grid<Boardgame> grid = new Grid<>(Boardgame.class);
        grid.removeAllColumns();
        grid.addColumn(Boardgame::getName).setHeader("보드게임 이름").setAutoWidth(true);
        grid.addColumn(boardgame -> boardgame.getCreator().getName()).setHeader("기여자").setAutoWidth(true);
        grid.setItems(boardgameService.getAllBoardgame());
        board.addRow(grid);

        txt_boardgame = new TextField();
        txt_boardgame.setMinLength(1);
        txt_boardgame.setLabel("보드게임 이름");
        board.addRow(txt_boardgame);

    }

    private void createFooter() {
        Button abort = new Button("취소");
        abort.addClickListener(buttonClickEvent -> close());
        Button confirm = new Button("보드 게임 추가");
        confirm.addClickListener(buttonClickEvent -> {
            try {
                checkValidation();
                boardgameService.addBoardgame(txt_boardgame.getValue(), accountService.getCurrentAccount());
                getEventBus().fireEvent(new DialogSuccessCloseActionEvent(this));
                close();
            } catch (Exception e) {
                Notification notification = new Notification(
                        e.getMessage(), 2000,
                        Notification.Position.MIDDLE);
                notification.open();
            }
        });

        HorizontalLayout footer = new HorizontalLayout();
        footer.add(abort, confirm);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        board.addRow(footer);
    }


    private void checkValidation() {
        if (txt_boardgame.isInvalid())
            throw new RuntimeException("입력값이 올바르지 않습니다.");

        if (boardgameService.isExistAsName(txt_boardgame.getValue()))
            throw new RuntimeException("이미 존재합니다.");
    }

}
