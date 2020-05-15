package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.event.DialogSuccessCloseActionEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class GameMatchCreateDialog extends Dialog {

    private AccountService accountService;

    private BoardgameService boardGameService;

    private GameMatchService gameMatchService;

    private ComboBox<Account> me;

    private TextField txt_matchName;

    private ComboBox<Boardgame> combo_boardGame;

    private List<ComboBox<Account>> comboList_party;

    public GameMatchCreateDialog(AccountService accountService
            , BoardgameService boardGameService
            , GameMatchService gameMatchService
            , ComponentEventListener<DialogSuccessCloseActionEvent> listener
    ) {
        this.accountService = accountService;
        this.boardGameService = boardGameService;
        this.gameMatchService = gameMatchService;
        super.getEventBus().addListener(DialogSuccessCloseActionEvent.class, listener);

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        createHeader();
        createContent();
        createFooter();
    }

    private void createHeader() {
        add(new Label("Match 생성"));
    }


    private void createContent() {
        Accordion content = new Accordion();


        //매치 이름
        VerticalLayout layout_matchName = new VerticalLayout();
        layout_matchName.setSizeFull();
        layout_matchName.setAlignItems(FlexComponent.Alignment.CENTER);
        txt_matchName = new TextField();
        txt_matchName.setLabel("매치 이름");
        txt_matchName.setValue(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + " 즐거운 게임!");
        txt_matchName.setMinLength(1);
        txt_matchName.setMaxLength(20);
        layout_matchName.add(txt_matchName);
        content.add("이름 설정", layout_matchName);

        //보드게임
        VerticalLayout layout_boardGame = new VerticalLayout();
        content.add("보드게임 설정", layout_boardGame);
        combo_boardGame = new ComboBox<>();
        combo_boardGame.setItems(this.boardGameService.getAllBoardgame());
        layout_boardGame.add(combo_boardGame, new Button("보드게임 추가하기", event -> {
            NewBoardGameDialog newBoardGameDialog = new NewBoardGameDialog(
                    boardGameService
                    , accountService
                    , event1 -> {
                combo_boardGame.setItems(this.boardGameService.getAllBoardgame());
            });
            newBoardGameDialog.open();
        }));

        //참가자
        VerticalLayout layout_pati = new VerticalLayout();

        VerticalLayout layout_party_combo = new VerticalLayout();
        layout_pati.add(layout_party_combo);

        comboList_party = new ArrayList<>();
        me = new ComboBox<>();
        me.setItems(this.accountService.getCurrentAccount());
        me.setValue(this.accountService.getCurrentAccount());
        me.setEnabled(false);
        comboList_party.add(me);
        layout_party_combo.add(me);

        ComboBox<Account> party1 = new ComboBox<>();
        party1.setItems(this.accountService.getCurrentAccount().getFriends().stream()
                .map(friend -> friend.getFriend())
                .collect(Collectors.toList())
        );
        layout_party_combo.add(party1);
        comboList_party.add(party1);
        content.add("참가자 설정", layout_pati);

        Button btn_addParty = new Button("참가자 추가");
        btn_addParty.addClickListener(event -> {
            ComboBox<Account> newParty = new ComboBox<>();
            newParty.setItems(this.accountService.getCurrentAccount().getFriends().stream()
                    .map(friend -> friend.getFriend())
                    .collect(Collectors.toList())
            );
            layout_party_combo.add(newParty);
            comboList_party.add(newParty);
        });
        btn_addParty.setWidthFull();
        layout_pati.add(btn_addParty);

        add(content);
    }

    private void createFooter() {
        Button abort = new Button("취소");
        abort.addClickListener(buttonClickEvent -> close());
        Button confirm = new Button("Match 생성");
        confirm.addClickListener(buttonClickEvent -> {
            try {
                checkValidation();
                makeGameMatch();
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
        add(footer);
    }

    private void makeGameMatch() {
        GameMatch match = this.gameMatchService.makeNewMatch(
                this.txt_matchName.getValue()
                , this.combo_boardGame.getValue()
                , this.comboList_party.stream()
                        .map(accountComboBox -> accountComboBox.getValue())
                        .collect(Collectors.toList())
                , this.me.getValue()
        );
        log.info("새로운 match가 생성되었습니다 : " + match);
    }

    private void checkValidation() {

        //매치 이름 체크
        if (this.txt_matchName.isInvalid())
            throw new RuntimeException("매치 이름을 확인해주세요.");

        //보드게임 체크
        if (this.combo_boardGame.isEmpty())
            throw new RuntimeException("보드게임을 확인해주세요.");

        //참석자 체크
        this.comboList_party.forEach(comboBox -> {
            if (comboBox.isEmpty())
                throw new RuntimeException("참석자를 확인해주세요.");
        });

        for (ComboBox me : this.comboList_party) {
            for (ComboBox other : this.comboList_party) {
                if (me.equals(other)) continue;

                if (me.getValue().equals(other.getValue()))
                    throw new RuntimeException("중복된 참석자가 있습니다.");
            }
        }
    }

}
