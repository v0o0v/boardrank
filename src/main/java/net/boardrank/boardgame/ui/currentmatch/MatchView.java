package net.boardrank.boardgame.ui.currentmatch;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import net.boardrank.boardgame.domain.*;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.service.TimeUtilService;
import net.boardrank.boardgame.ui.ResponsiveVerticalLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MatchView extends ResponsiveVerticalLayout {

    private GameMatchService gameMatchService;
    private BoardgameService boardgameService;
    private AccountService accountService;

    private GameMatch gameMatch;

    private Button btn_changeMatchStatus = new Button();
    private ComboBox<Boardgame> combo_boardgame = new ComboBox<>();
    private List<ComboBox<Boardgame>> expansionBoardgameComboList = new ArrayList<>();
    private DatePicker startDate = new DatePicker();
    private TimePicker startTime = new TimePicker();
    private DatePicker finishedDate = new DatePicker();
    private TimePicker finishedTime = new TimePicker();
    private Grid<RankEntry> gridParty = new Grid<>();
    private HorizontalLayout top;
    private ComboBox<Account> combo_bgProvider = new ComboBox<>();
    private ComboBox<Account> combo_ruleSupporter = new ComboBox<>();
    private VerticalLayout imageView;
    private Upload upload = new Upload();

    public MatchView(GameMatchService gameMatchService
            , GameMatch gameMatch
            , BoardgameService boardgameService
            , AccountService accountService
    ) {
        this.gameMatchService = gameMatchService;
        this.boardgameService = boardgameService;
        this.accountService = accountService;
        this.gameMatch = gameMatch;

        initLayout();
        initComponent();
        initEvent();
        applyGameStatus();
        resetValue();
        initOwnerActionEnable();
    }

    private void initOwnerActionEnable() {
        if (accountService.getCurrentAccount().equals(gameMatch.getCreatedMember())) {
            top.setEnabled(true);
        } else {
            top.setEnabled(false);
        }
    }

    private void initEvent() {
        this.btn_changeMatchStatus.addClickListener(event -> {
            switch (gameMatch.getGameMatchStatus()) {
                case init:
                    toInProgress();
                    break;
                case proceeding:
                    toFinished();
                    break;
                case finished:
                    toResultAccepted();
                    break;
                case resultAccepted:
                    break;
            }
            applyGameStatus();
            resetValue();
        });

        this.startTime.addValueChangeListener(event -> {
            try {
                LocalDateTime oldDateTime = gameMatch.getStartedTime();
                LocalTime toTime = event.getValue();
                if (oldDateTime == null || toTime == null) return;
                LocalDateTime newDateTime = LocalDateTime.of(oldDateTime.toLocalDate(), toTime);
                gameMatch.setStartedTime(TimeUtilService.transKTCToUTC(newDateTime));
                this.gameMatch = gameMatchService.save(gameMatch);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        this.startDate.addValueChangeListener(event -> {
            try {
                LocalDateTime oldDateTime = gameMatch.getStartedTime();
                LocalDate newDate = event.getValue();
                if (oldDateTime == null || newDate == null) return;
                LocalDateTime newDateTime = LocalDateTime.of(newDate, oldDateTime.toLocalTime());
                gameMatch.setStartedTime(TimeUtilService.transKTCToUTC(newDateTime));
                this.gameMatch = gameMatchService.save(gameMatch);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        this.finishedTime.addValueChangeListener(event -> {
            try {
                LocalDateTime oldDateTime = gameMatch.getFinishedTime();
                LocalTime toTime = event.getValue();
                if (oldDateTime == null || toTime == null) return;
                LocalDateTime newDateTime = LocalDateTime.of(oldDateTime.toLocalDate(), toTime);
                gameMatch.setFinishedTime(TimeUtilService.transKTCToUTC(newDateTime));
                this.gameMatch = gameMatchService.save(gameMatch);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        this.finishedDate.addValueChangeListener(event -> {
            try {
                LocalDateTime oldDateTime = gameMatch.getFinishedTime();
                LocalDate newDate = event.getValue();
                if (oldDateTime == null || newDate == null) return;
                LocalDateTime newDateTime = LocalDateTime.of(newDate, oldDateTime.toLocalTime());
                gameMatch.setFinishedTime(TimeUtilService.transKTCToUTC(newDateTime));
                gameMatch = gameMatchService.save(gameMatch);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        this.combo_bgProvider.addValueChangeListener(event -> {
            gameMatch.setBoardgameProvider(event.getValue());
            this.gameMatch = gameMatchService.save(gameMatch);
        });

        this.combo_ruleSupporter.addValueChangeListener(event -> {
            gameMatch.setRuleSupporter(event.getValue());
            this.gameMatch = gameMatchService.save(gameMatch);
        });
    }

    private void toResultAccepted() {
        this.gameMatch = gameMatchService.setGameMatchStatus(gameMatch, GameMatchStatus.resultAccepted);
    }

    private void toFinished() {
        ScoreInputDialog scoreInputDialog = new ScoreInputDialog(gameMatch, event -> {
            this.gameMatch = gameMatchService.setGameMatchStatus(gameMatch, GameMatchStatus.finished);
            this.gameMatch = gameMatchService.setFinishTime(gameMatch, LocalDateTime.now());
            this.gameMatch = gameMatchService.save(gameMatch);
            gridParty.setItems(gameMatch.getRankentries());
            applyGameStatus();
            resetValue();
        });
        scoreInputDialog.open();
    }

    private void toInProgress() {
        this.gameMatch = gameMatchService.setGameMatchStatus(gameMatch, GameMatchStatus.proceeding);
        this.gameMatch = gameMatchService.setStartTime(gameMatch, LocalDateTime.now());
    }

    private void resetValue() {
        startDate.clear();
        if (gameMatch.getStartedTime() != null)
            startDate.setValue(gameMatch.getStartedTime().toLocalDate());
        startTime.clear();
        if (gameMatch.getStartedTime() != null)
            startTime.setValue(gameMatch.getStartedTime().toLocalTime());
        finishedDate.clear();
        if (gameMatch.getFinishedTime() != null)
            finishedDate.setValue(gameMatch.getFinishedTime().toLocalDate());
        finishedTime.clear();
        if (gameMatch.getFinishedTime() != null)
            finishedTime.setValue(gameMatch.getFinishedTime().toLocalTime());
    }

    private void initComponent() {

        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("1px", 1)
                , new FormLayout.ResponsiveStep("300px", 2)
        );

        combo_boardgame.setReadOnly(true);
        combo_boardgame.setItems(gameMatch.getBoardGame());
        combo_boardgame.setValue(gameMatch.getBoardGame());
        combo_boardgame.setLabel("보드게임");
        form.add(combo_boardgame, 2);
        gameMatch.getExpansions().forEach(exp -> {
            ComboBox<Boardgame> comboExp = new ComboBox<>();
            comboExp.setItems(exp);
            comboExp.setValue(exp);
            comboExp.setReadOnly(true);
            comboExp.setLabel("확장판");
            expansionBoardgameComboList.add(comboExp);
            form.add(comboExp, 2);
        });

        form.add(combo_bgProvider, 1);
        combo_bgProvider.setLabel("보드게임 제공");
        combo_bgProvider.setItems(gameMatch.getAllParticiants());
        if (gameMatch.getBoardgameProvider() != null)
            combo_bgProvider.setValue(gameMatch.getBoardgameProvider());
        combo_bgProvider.setClearButtonVisible(true);

        form.add(combo_ruleSupporter, 1);
        combo_ruleSupporter.setLabel("룰 설명");
        combo_ruleSupporter.setItems(gameMatch.getAllParticiants());
        if (gameMatch.getRankentries() != null)
            combo_ruleSupporter.setValue(gameMatch.getRuleSupporter());
        combo_ruleSupporter.setClearButtonVisible(true);

        form.add(this.createPartyGrid(), 2);
        startDate.setLabel("시작 날짜");
        form.add(startDate, 1);
        startTime.setLabel("시작 시간");
        form.add(startTime, 1);
        finishedDate.setLabel("종료 날짜");
        form.add(finishedDate, 1);
        finishedTime.setLabel("종료 시간");
        form.add(finishedTime, 1);

        form.add(createImageView(), 2);
        form.add(createUploadView(), 2);
        form.add(new MatchCommentListView(this.gameMatchService, this.gameMatch.getId()), 2);

        top = new HorizontalLayout();
        top.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        top.addAndExpand(btn_changeMatchStatus);
        top.add(new Button(new Icon(VaadinIcon.COG), event -> {
            new MatchDetailModifyDialog(gameMatchService, gameMatch).open();
        }));

        add(top);
        add(form);
    }

    private Component createUploadView() {

        MemoryBuffer buffer = new MemoryBuffer();
        upload.setReceiver(buffer);
        upload.setUploadButton(new Button("사진 올리기"));
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setDropAllowed(false);
        upload.setMaxFileSize(10 * 1024 * 1024);

        Account account = accountService.getCurrentAccount();

        upload.addFileRejectedListener(event -> {
            Notification notification = new Notification("문제가 발생하였습니다. " + event.getErrorMessage());
            notification.setDuration(1500);
            notification.open();
        });

        upload.addFailedListener(event -> {
            Notification notification = new Notification("문제가 발생하였습니다. " + event.getReason());
            notification.setDuration(1500);
            notification.open();
        });

        upload.addSucceededListener(event -> {
            try {
                String filename = gameMatchService.uploadImage(
                        gameMatch
                        , buffer.getInputStream()
                        , event.getMIMEType()
                        , account
                );
                gameMatch = gameMatchService.addImage(gameMatch, filename, account);
                imageViewReset();
            } catch (Exception e) {
                Notification notification = new Notification("문제가 발생하였습니다.");
                notification.setDuration(1500);
                notification.open();
            }
        });

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(Alignment.END);
        layout.setJustifyContentMode(JustifyContentMode.END);
        layout.addAndExpand(upload);

        return layout;
    }

    private Component createImageView() {
        imageView = new VerticalLayout();
        imageView.setMaxHeight("40em");
        imageView.setPadding(false);
        imageView.setMargin(false);
        imageView.getStyle().set("overflow-y", "auto");
        imageViewReset();
        return imageView;
    }

    private void imageViewReset() {
        imageView.removeAll();
        gameMatch.getImages().forEach(imageURL -> {
            Image image = new Image(gameMatchService.getURLAsCloundFront(imageURL.getFilename()), "파일어딨니");
            image.setMaxWidth((width - 3) + "em");
            imageView.add(image);
            imageView.setHorizontalComponentAlignment(Alignment.CENTER, image);
            Button button = new Button("삭제", event -> {
                gameMatch = gameMatchService.deleteImage(gameMatch, imageURL.getFilename());
                imageViewReset();
            });
            button.addThemeVariants(ButtonVariant.LUMO_SMALL);
            imageView.add(button);
            imageView.setHorizontalComponentAlignment(Alignment.START, button);
        });

        if (gameMatch.getImages().size() < 5) upload.setVisible(true);
        else upload.setVisible(false);

    }

    private Grid createPartyGrid() {
        gridParty = new Grid<>();
        gridParty.setItems(gameMatch.getRankentries());
        gridParty.removeAllColumns();
        gridParty.addColumn(new ComponentRenderer<>(rankEntry -> {
            return new Button(rankEntry.getAccount().getName());
        })).setHeader("참가자");
        gridParty.addColumn(RankEntry::getScore).setHeader("점수");
        gridParty.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
        });
        int gridHeight = gameMatch.getRankentries().size() * 4 + 3;
        gridParty.setMaxHeight(gridHeight + "em");
        return gridParty;
    }

    private void applyGameStatus() {
        switch (gameMatch.getGameMatchStatus()) {
            case init:
                this.btn_changeMatchStatus.setText("게임 시작 하기");
                this.btn_changeMatchStatus.setEnabled(true);
                if (accountService.getCurrentAccount().equals(gameMatch.getCreatedMember())) {
                    this.startDate.setReadOnly(true);
                    this.startTime.setReadOnly(true);
                    this.finishedDate.setReadOnly(true);
                    this.finishedTime.setReadOnly(true);
                    this.combo_bgProvider.setReadOnly(false);
                    this.combo_ruleSupporter.setReadOnly(false);
                }
                break;
            case proceeding:
                this.btn_changeMatchStatus.setText("점수입력/게임종료 하기");
                this.btn_changeMatchStatus.setEnabled(true);
                if (accountService.getCurrentAccount().equals(gameMatch.getCreatedMember())) {
                    this.startDate.setReadOnly(false);
                    this.startTime.setReadOnly(false);
                    this.finishedDate.setReadOnly(true);
                    this.finishedTime.setReadOnly(true);
                    this.combo_bgProvider.setReadOnly(false);
                    this.combo_ruleSupporter.setReadOnly(false);
                }
                break;
            case finished:
                this.btn_changeMatchStatus.setText("게임 결과 승인 대기중");
                this.btn_changeMatchStatus.setEnabled(false);
                if (accountService.getCurrentAccount().equals(gameMatch.getCreatedMember())) {
                    this.startDate.setReadOnly(false);
                    this.startTime.setReadOnly(false);
                    this.finishedDate.setReadOnly(false);
                    this.finishedTime.setReadOnly(false);
                    this.combo_bgProvider.setReadOnly(false);
                    this.combo_ruleSupporter.setReadOnly(false);
                }
                break;
            case resultAccepted:
                this.btn_changeMatchStatus.setText("게임 결과 승인 완료됨");
                this.btn_changeMatchStatus.setEnabled(false);
                if (accountService.getCurrentAccount().equals(gameMatch.getCreatedMember())) {
                    this.startDate.setReadOnly(true);
                    this.startTime.setReadOnly(true);
                    this.finishedDate.setReadOnly(true);
                    this.finishedTime.setReadOnly(true);
                    this.combo_bgProvider.setReadOnly(true);
                    this.combo_ruleSupporter.setReadOnly(true);
                }
        }
    }

    private void initLayout() {
        getStyle().set("border", "1px solid #101010");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        setMargin(true);
    }

}



