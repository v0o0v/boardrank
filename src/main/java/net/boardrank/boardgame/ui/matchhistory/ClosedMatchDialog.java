package net.boardrank.boardgame.ui.matchhistory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.Boardgame;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.RankEntry;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.service.ImageUtilService;
import net.boardrank.boardgame.ui.common.ResponsiveDialog;
import net.boardrank.boardgame.ui.common.UserButton;
import net.boardrank.boardgame.ui.currentmatch.MatchCommentListView;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ClosedMatchDialog extends ResponsiveDialog {

    private GameMatchService gameMatchService;
    private GameMatch gameMatch;
    private Account currentAccount;

    private ComboBox<Boardgame> combo_boardgame = new ComboBox<>();
    private List<ComboBox<Boardgame>> expansionBoardgameComboList = new ArrayList<>();
    private DatePicker startDate = new DatePicker();
    private TimePicker startTime = new TimePicker();
    private DatePicker finishedDate = new DatePicker();
    private TimePicker finishedTime = new TimePicker();
    private Grid<RankEntry> gridParty = new Grid<>();
    private FormLayout form;
    private ComboBox<Account> combo_bgProvider = new ComboBox<>();
    private ComboBox<Account> combo_ruleSupporter = new ComboBox<>();
    private VerticalLayout imageView;
    private Upload upload = new Upload();

    public ClosedMatchDialog(GameMatchService gameMatchService, GameMatch gameMatch) {
        this.gameMatchService = gameMatchService;
        this.gameMatch = gameMatch;
        this.currentAccount = this.gameMatchService.getAccountService().getCurrentAccount();

        initComponent();
        resetValue();
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

        form = new FormLayout();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("1px",1)
                ,new FormLayout.ResponsiveStep("300px",2)
        );

        if (!gameMatch.getWinnerByString().isEmpty()) {
            VerticalLayout top = new VerticalLayout();
            top.getStyle().set("border", "2px solid #705050");
            top.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            top.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
            Icon icon = new Icon(VaadinIcon.TROPHY);
            top.add(icon);
            gameMatch.getWinnerList().forEach(account -> {
                top.add(new UserButton(gameMatchService,account));
            });
            form.add(top,2);
        }

        combo_boardgame.setReadOnly(true);
        combo_boardgame.setItems(gameMatch.getBoardGame());
        combo_boardgame.setValue(gameMatch.getBoardGame());
        combo_boardgame.setLabel("보드게임");
        form.add(combo_boardgame,2);
        gameMatch.getExpansions().forEach(exp -> {
            ComboBox<Boardgame> comboExp = new ComboBox<>();
            comboExp.setItems(exp);
            comboExp.setValue(exp);
            comboExp.setReadOnly(true);
            comboExp.setLabel("확장판");
            expansionBoardgameComboList.add(comboExp);
            form.add(comboExp,2);
        });

        form.add(combo_bgProvider,1);
        combo_bgProvider.setLabel("보드게임 제공");
        combo_bgProvider.setReadOnly(true);
        combo_bgProvider.setItems(gameMatch.getAllParticiants());
        if(gameMatch.getBoardgameProvider()!=null)
            combo_bgProvider.setValue(gameMatch.getBoardgameProvider());

        form.add(combo_ruleSupporter,1);
        combo_ruleSupporter.setLabel("룰 설명");
        combo_ruleSupporter.setReadOnly(true);
        combo_ruleSupporter.setItems(gameMatch.getAllParticiants());
        if(gameMatch.getRankentries()!=null)
            combo_ruleSupporter.setValue(gameMatch.getRuleSupporter());

        form.add(this.createPartyGrid(),2);
        startDate.setLabel("시작 날짜");
        startDate.setReadOnly(true);
        form.add(startDate, 1);
        startTime.setLabel("시작 시간");
        startTime.setReadOnly(true);
        form.add(startTime, 1);
        finishedDate.setLabel("종료 날짜");
        finishedDate.setReadOnly(true);
        form.add(finishedDate,1);
        finishedTime.setLabel("종료 시간");
        finishedTime.setReadOnly(true);
        form.add(finishedTime,1);

        form.add(new H5("Images"),2);
        form.add(createImageView(), 2);
        form.add(createUploadView(),2);
        form.add(new MatchCommentListView(this.gameMatchService, this.gameMatch.getId()),2);

        HorizontalLayout close = new HorizontalLayout();
        close.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        close.add(new Button("닫기", event -> close()));
        add(new VerticalLayout(form),close);
    }

    private Component createUploadView() {

        upload.setUploadButton(new Button("사진 올리기"));
        upload.setDropAllowed(false);
        upload.setAcceptedFileTypes("image/jpeg");
        upload.setMaxFileSize(10 * 1024 * 1024);//10MB

        MemoryBuffer buffer = new MemoryBuffer();
        upload.setReceiver(buffer);

        Account account = gameMatchService.getAccountService().getCurrentAccount();

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

                BufferedImage originalImage = ImageIO.read(buffer.getInputStream());
                ImageReader reader = ImageIO.getImageReadersBySuffix("jpg").next();
                reader.setInput(ImageIO.createImageInputStream(buffer.getInputStream()));
                IIOMetadata metadata = reader.getImageMetadata(0);

                //resize
                BufferedImage resizedImage = originalImage.getWidth() >= originalImage.getHeight()
                        ? ImageUtilService.resizeByWidth(originalImage, 1600)
                        : ImageUtilService.resizeByHeight(originalImage, 1600);

                //compress
                ImageWriter writer = ImageIO.getImageWritersByMIMEType(event.getMIMEType()).next();
                File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
                OutputStream os = new FileOutputStream(tempFile);
                writer.setOutput(ImageIO.createImageOutputStream(os));
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.9f);
                writer.write(null
                        , new IIOImage(resizedImage, null, metadata)
                        , param);

                //s3에 upload
                String filename = gameMatchService.uploadImage(
                        new FileInputStream(tempFile)
                        , event.getMIMEType()
                        , account
                );

                reader.dispose();
                os.close();
                writer.dispose();
                tempFile.delete();

                //db에 image filename 추가
                gameMatch = gameMatchService.addImage(gameMatch, filename, account);
                imageViewReset();
            } catch (Exception e) {
                log.error("이미지 업로드 시 문제 발생", e);
                Notification notification = new Notification("문제가 발생하였습니다.");
                notification.setDuration(1500);
                notification.open();
            }
        });

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
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
            Image image = new Image(gameMatchService.getURLAsCloundFront(imageURL.getFilename()), "No Image");
            image.setMaxWidth((width - 3) + "em");
            imageView.add(image);
            imageView.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, image);
            Button button = new Button("삭제", event -> {
                gameMatch = gameMatchService.deleteImage(gameMatch, imageURL.getFilename());
                imageViewReset();
            });
            button.addThemeVariants(ButtonVariant.LUMO_SMALL);
            if (this.currentAccount.equals(imageURL.getOwner()))
                imageView.add(button);
            imageView.setHorizontalComponentAlignment(FlexComponent.Alignment.START, button);
        });

        if (gameMatch.getImages().size() < 5) upload.setVisible(true);
        else upload.setVisible(false);
    }

    private Grid createPartyGrid() {
        gridParty = new Grid<>();
        gridParty.setItems(gameMatch.getRankentries());
        gridParty.removeAllColumns();
        gridParty.addColumn(new ComponentRenderer<>(rankEntry -> {
            return new UserButton(gameMatchService, rankEntry.getAccount());
        })).setHeader("참가자");
        gridParty.addColumn(RankEntry::getScore).setHeader("점수");
        gridParty.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
        });
        int gridHeight = gameMatch.getRankentries().size() * 47 + 58;
        gridParty.setMaxHeight(gridHeight + "px");
        return gridParty;
    }

}



