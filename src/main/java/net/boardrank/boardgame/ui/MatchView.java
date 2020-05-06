package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;

public class MatchView extends VerticalLayout {

    private GameMatchService gameMatchService;

    private BoardgameService boardgameService;

    private GameMatch gameMatch;

    public MatchView(GameMatchService gameMatchService, GameMatch gameMatch, BoardgameService boardgameService) {
        this.gameMatchService = gameMatchService;
        this.boardgameService = boardgameService;
        this.gameMatch = gameMatch;

        initLayout();
        initComponent(gameMatch);
    }

    private void initComponent(GameMatch gameMatch) {

        //타이틀
        VerticalLayout layout_title = new VerticalLayout();
        layout_title.setAlignItems(Alignment.CENTER);
        Label lbl_title = new Label(gameMatch.getMatchTitle());
        layout_title.addAndExpand(lbl_title);
        layout_title.setSizeFull();
        add(layout_title);

        //보드게임
        VerticalLayout layout_boardgame = new VerticalLayout();
        layout_boardgame.setAlignItems(Alignment.CENTER);
        ComboBox<Boardgame> combo_boardgame = new ComboBox<>();
        combo_boardgame.setLabel("보드게임");
        combo_boardgame.setItems(boardgameService.getAllBoardgame());
        combo_boardgame.setValue(gameMatch.getBoardGame());
        layout_boardgame.addAndExpand(combo_boardgame);
        layout_boardgame.setSizeFull();
        add(layout_boardgame);

        //참가자
        VerticalLayout layout_party = new VerticalLayout();
        layout_party.setAlignItems(Alignment.STRETCH);
        gameMatch.getPaticiant().getAccounts().stream()
                .forEach(account -> {
                    Button button = new Button(account.getName());
                    button.addClickListener(event -> {
                        //TODO
                    });
                    layout_party.addAndExpand(button);
                });
        layout_party.setSizeFull();
        add(layout_party);

    }

    private void initLayout() {
        setPadding(true);
        setSpacing(true);
//        setMargin(true);

        getStyle().set("border", "1px solid #101010");
//        getStyle().set("border", "1px solid #9E9E9E");
//        setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
//        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
    }

}
