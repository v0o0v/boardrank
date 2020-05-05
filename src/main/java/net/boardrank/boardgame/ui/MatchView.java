package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.GameMatchService;

import java.util.Arrays;

public class MatchView extends VerticalLayout {

    private GameMatchService gameMatchService;

    private GameMatch gameMatch;

    public MatchView(GameMatchService gameMatchService, GameMatch gameMatch) {
        this.gameMatchService = gameMatchService;
        this.gameMatch = gameMatch;

        initLayout();
        initComponent(gameMatch);
    }

    private void initComponent(GameMatch gameMatch) {
        Label title = new Label(gameMatch.getMatchTitle());
        add(title);

        ComboBox<Boardgame> boardgame = new ComboBox<>();
        boardgame.setItems(Arrays.asList(gameMatch.getBoardGame()));

        gameMatch.getPaticiant().getAccounts().stream()
                .map(account -> new Button(account.getName()))
                .forEach(button -> add(button));

        add(boardgame);
    }

    private void initLayout() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        setMargin(true);

        getStyle().set("border", "3px solid #9E9E9E");
        setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    }

}
