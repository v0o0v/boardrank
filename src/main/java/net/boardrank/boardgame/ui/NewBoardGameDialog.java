package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.ui.common.ResponsiveDialog;
import net.boardrank.boardgame.ui.event.DialogSuccessCloseActionEvent;

@Slf4j
public class NewBoardGameDialog extends ResponsiveDialog {

    private BoardgameService boardgameService;

    private AccountService accountService;

    private VerticalLayout layout = new VerticalLayout();
    private TextField txt_boardgame;
    private ComboBox<Boardgame> comboBase;
    private Checkbox checkbox_isExp;

    public NewBoardGameDialog(BoardgameService boardgameService
            , AccountService accountService
            , ComponentEventListener<DialogSuccessCloseActionEvent> listener) {

        this.boardgameService = boardgameService;
        this.accountService = accountService;
        getEventBus().addListener(DialogSuccessCloseActionEvent.class, listener);

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        layout.setPadding(false);
        layout.setMargin(false);
        add(layout);

        createHeader();
        createContent();
        createFooter();


    }

    private void createHeader() {
        layout.add(new H5("보드 게임 추가"));
    }

    private void createContent() {
        layout.add(new Label("보드게임 리스트"));
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Grid<Boardgame> grid = new Grid<>(Boardgame.class);
        grid.removeAllColumns();
        grid.setHeight("12em");
        grid.addColumn(Boardgame::getName).setHeader("이름").setSortable(true);
//        grid.addComponentColumn(boardgame -> {
//            Checkbox checkbox = new Checkbox(boardgame.isExp());
//            checkbox.setReadOnly(true);
//            return checkbox;
//        }).setHeader("확장판");
        grid.addColumn(Boardgame::getBase).setHeader("기본판").setSortable(true);
        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
        });
        grid.setItems(boardgameService.getAllBoardgame());
        layout.add(grid);

        layout.add(new H6("위 리스트를 꼭 확인해 보시고 새로운 보드게임 추가 부탁드립니다. " +
                "중복 추가될 경우 나중에 추가된 보드게임은 언제든 삭제될 수 있습니다."));

        VerticalLayout layout_newBoardGame = new VerticalLayout();
        layout_newBoardGame.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        txt_boardgame = new TextField();
        txt_boardgame.setMinLength(1);
        txt_boardgame.setLabel("보드게임 이름");
        comboBase = new ComboBox<>("기본판", boardgameService.getAllBaseBoardgames());
        comboBase.setEnabled(false);
        comboBase.setClearButtonVisible(true);

        layout_newBoardGame.add(txt_boardgame);
        checkbox_isExp = new Checkbox("확장판입니다.", event -> {
            comboBase.setEnabled(event.getValue());
            if (event.getValue() == false)
                comboBase.clear();
        });
        layout_newBoardGame.add(checkbox_isExp);
        layout_newBoardGame.add(comboBase);
        layout_newBoardGame.getStyle().set("border", "1px solid #e0e0e0");

        layout.add(layout_newBoardGame);
    }

    private void createFooter() {
        Button abort = new Button("취소");
        abort.addClickListener(buttonClickEvent -> close());
        Button confirm = new Button("보드 게임 추가");
        confirm.addClickListener(buttonClickEvent -> {
            try {
                checkValidation();
                boardgameService.addBoardgame(
                        txt_boardgame.getValue()
                        , accountService.getCurrentAccount()
                        , checkbox_isExp.getValue()
                        , comboBase.getValue()
                );
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
        footer.setPadding(true);
        footer.setSpacing(true);
        footer.setMargin(true);
        layout.add(footer);
    }


    private void checkValidation() {
        if (txt_boardgame.isEmpty())
            throw new RuntimeException("입력값이 올바르지 않습니다.");

        if (txt_boardgame.isInvalid())
            throw new RuntimeException("입력값이 올바르지 않습니다.");

        if(checkbox_isExp.getValue() && comboBase.isEmpty()){
            throw new RuntimeException("기본판을 선택해 주세요.");
        }

        if (boardgameService.isExistAsName(txt_boardgame.getValue()))
            throw new RuntimeException("이미 존재합니다.");
    }

}
