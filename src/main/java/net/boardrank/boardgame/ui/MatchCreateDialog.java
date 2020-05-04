package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import net.boardrank.account.domain.Account;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.domain.BoardGame;
import net.boardrank.boardgame.service.BoardGameService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MatchCreateDialog extends Dialog {

    private AccountService accountService;

    private BoardGameService boardGameService;

    public MatchCreateDialog(AccountService accountService, BoardGameService boardGameService) {
        this.accountService = accountService;
        this.boardGameService = boardGameService;

        createHeader();
        createContent();
        createFooter();
    }

    private void createHeader() {
        add(new Label("새 매치"));
    }

    private void createContent() {
        Accordion content = new Accordion();


        //매치 이름
        HorizontalLayout layout_matchName = new HorizontalLayout();
        layout_matchName.setSizeFull();
        layout_matchName.setAlignItems(FlexComponent.Alignment.CENTER);
        Label lbl_matchName = new Label("매치 이름 : ");
        TextField txt_matchName = new TextField();
        layout_matchName.add(lbl_matchName, txt_matchName);
        content.add("이름 설정", layout_matchName);

        //보드게임
        VerticalLayout layout_boardGame = new VerticalLayout();
        content.add("보드게임 설정", layout_boardGame);
        ComboBox<BoardGame> boardGameComboBox = new ComboBox<>();
        boardGameComboBox.setItems(this.boardGameService.getAllBoardgame());
        layout_boardGame.add(boardGameComboBox);

        //참가자
        VerticalLayout layout_pati = new VerticalLayout();

        VerticalLayout layout_party_combo = new VerticalLayout();
        layout_pati.add(layout_party_combo);

        List<ComboBox> parties = new ArrayList<>();
        ComboBox<Account> me = new ComboBox<>();
        me.setItems(this.accountService.getCurrentAccount());
        me.setValue(this.accountService.getCurrentAccount());
        me.setEnabled(false);
        parties.add(me);
        layout_party_combo.add(me);

        ComboBox<Account> accountComboBox = new ComboBox<>();
        accountComboBox.setItems(this.accountService.getCurrentAccount().getFriends().stream()
                .map(friend -> friend.getFriend())
                .collect(Collectors.toList())
        );
        layout_party_combo.add(accountComboBox);
        parties.add(accountComboBox);
        content.add("참가자 설정", layout_pati);

        Button btn_addParty = new Button("참가자 추가");
        btn_addParty.addClickListener(event -> {
            ComboBox<Account> newParty = new ComboBox<>();
            newParty.setItems(this.accountService.getCurrentAccount().getFriends().stream()
                    .map(friend -> friend.getFriend())
                    .collect(Collectors.toList())
            );
            layout_party_combo.add(newParty);
            parties.add(newParty);
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

            close();
        });

        HorizontalLayout footer = new HorizontalLayout();
        footer.add(abort, confirm);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        add(footer);
    }

}
