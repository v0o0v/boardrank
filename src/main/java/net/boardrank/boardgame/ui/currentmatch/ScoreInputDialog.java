package net.boardrank.boardgame.ui.currentmatch;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.RankEntry;
import net.boardrank.boardgame.ui.common.ResponsiveDialog;
import net.boardrank.boardgame.ui.event.DialogSuccessCloseActionEvent;

@Slf4j
public class ScoreInputDialog extends ResponsiveDialog {

    private GameMatch gameMatch;

    private Grid<RankEntry> gridParty = new Grid<>(RankEntry.class);

    private Board board = new Board();

    public ScoreInputDialog(GameMatch gameMatch) {

        this.gameMatch = gameMatch;

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        createHeader();
        createContent();
        createFooter();

        board.setSizeFull();
        add(board);

        update();
    }

    public void addDialogSuccessCloseActionEvent(ComponentEventListener listener){
        getEventBus().addListener(DialogSuccessCloseActionEvent.class, listener);
    }
    public GameMatch getGameMatch() {
        return gameMatch;
    }

    private void createHeader() {
        board.addRow(new Label("게임 결과 입력"));
    }

    private void createContent() {
        gridParty.removeAllColumns();
        gridParty.setWidthFull();

        gridParty.addColumn(new ComponentRenderer<>(rankEntry -> {
            Button btn_user = new Button(rankEntry.getAccount().getName());
            VerticalLayout layout = new VerticalLayout(btn_user);
            return layout;
        })).setHeader("참가자").setWidth("6em");

        gridParty.addColumn(new ComponentRenderer<>(rankEntry -> {
            IntegerField num_score = new IntegerField();
            num_score.setWidth("4em");
            num_score.setHasControls(false);
            if (rankEntry.getScore() != null)
                num_score.setValue(rankEntry.getScore());

            num_score.addValueChangeListener(event -> {
                rankEntry.setScore(event.getValue());
                gameMatch.resetRank();
                update();
            });
            num_score.setValueChangeMode(ValueChangeMode.LAZY);
            return num_score;
        })).setHeader("점수").setWidth("6em");

        gridParty.addColumn(rankEntry -> rankEntry.getRank()
        ).setHeader("등수").setWidth("3em");

        gridParty.getColumns().forEach(col -> {
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
        });

        board.addRow(gridParty);
    }

    private void createFooter() {
        Button abort = new Button("취소");
        abort.addClickListener(buttonClickEvent -> close());
        Button confirm = new Button("게임 결과 입력 완료");
        confirm.addClickListener(buttonClickEvent -> {
            try {
                checkValidation();
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

    }

    private void update(){
        gridParty.setItems(gameMatch.getRankentries());
    }

}
