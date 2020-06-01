package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.ui.event.DialogSuccessCloseActionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class GameMatchCreateDialog extends ResponsiveDialog {

    private AccountService accountService;

    private BoardgameService boardGameService;

    private GameMatchService gameMatchService;

    private ComboBox<Account> me;

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
        add(new Label("새로운 Match 생성"));
    }


    private void createContent() {
        Accordion content = new Accordion();

        //보드게임
        VerticalLayout layout_boardGame = new VerticalLayout();
        layout_boardGame.setAlignItems(FlexComponent.Alignment.STRETCH);
        content.add("보드게임 설정", layout_boardGame);
        combo_boardGame = new ComboBox<>();
        combo_boardGame.setItems(this.boardGameService.getAllBaseBoardgames());
        layout_boardGame.addAndExpand(combo_boardGame, new Button("보드게임 추가하기", event -> {
            NewBoardGameDialog newBoardGameDialog = new NewBoardGameDialog(
                    boardGameService
                    , accountService
                    , event1 -> {
                combo_boardGame.setItems(this.boardGameService.getAllBaseBoardgames());
            });
            newBoardGameDialog.open();
        }));

        //참가자
        VerticalLayout layout_pati = new VerticalLayout();
        layout_pati.setAlignItems(FlexComponent.Alignment.STRETCH);

        VerticalLayout layout_party_combo = new VerticalLayout();
        layout_party_combo.setAlignItems(FlexComponent.Alignment.STRETCH);
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
        layout_party_combo.addAndExpand(party1);
        comboList_party.add(party1);
        content.add("참가자 설정", layout_pati);

        Button btn_addParty = new Button("참가자 추가");
        Button btn_removeParty = new Button("참가자 삭제");

        btn_addParty.addClickListener(event -> {
            ComboBox<Account> newParty = new ComboBox<>();
            newParty.setItems(this.accountService.getCurrentAccount().getFriends().stream()
                    .map(friend -> friend.getFriend())
                    .collect(Collectors.toList())
            );
            layout_party_combo.add(newParty);
            comboList_party.add(newParty);
            btn_removeParty.setEnabled(true);
        });

        btn_removeParty.setEnabled(false);
        btn_removeParty.addClickListener(event -> {
            ComboBox<Account> remove = comboList_party.remove(comboList_party.size() - 1);
            layout_party_combo.remove(remove);
            if (comboList_party.size() <= 2) btn_removeParty.setEnabled(false);
        });
        HorizontalLayout partyBtnLayout = new HorizontalLayout();
        partyBtnLayout.addAndExpand(btn_removeParty, btn_addParty);
        partyBtnLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout_pati.addAndExpand(partyBtnLayout);
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
                this.combo_boardGame.getValue()
                , this.comboList_party.stream()
                        .map(accountComboBox -> accountComboBox.getValue())
                        .collect(Collectors.toList())
                , this.me.getValue()
        );
        log.info("새로운 match가 생성되었습니다 : " + match);
    }

    private void checkValidation() {

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
