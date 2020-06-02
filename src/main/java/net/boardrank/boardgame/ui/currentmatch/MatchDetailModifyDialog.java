package net.boardrank.boardgame.ui.currentmatch;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.GameMatchStatus;
import net.boardrank.boardgame.service.GameMatchService;

@Slf4j
public class MatchDetailModifyDialog extends Dialog {

    private GameMatchService gameMatchService;
    private GameMatch gameMatch;

    private Button btn_removeMatch;

    public MatchDetailModifyDialog(
            GameMatchService gameMatchService
            , GameMatch gameMatch
    ) {
        this.gameMatchService = gameMatchService;
        this.gameMatch = gameMatch;

        btn_removeMatch = new Button("Match 삭제하기", event -> {
            if(gameMatch.getGameMatchStatus().equals(GameMatchStatus.resultAccepted)) {
                Notification notification = new Notification("해당 Match는 삭제할 수 없습니다.(Match Closed)");
                notification.setDuration(1500);
                notification.open();
                return;
            }

            gameMatchService.removeMatch(gameMatch);

            Notification notification = new Notification("Match가 삭제되었습니다.");
            notification.setDuration(1500);
            notification.open();
            close();

            UI.getCurrent().getPage().reload();
        });
        btn_removeMatch.addThemeVariants(ButtonVariant.LUMO_ERROR);
        add(btn_removeMatch);
    }

}
