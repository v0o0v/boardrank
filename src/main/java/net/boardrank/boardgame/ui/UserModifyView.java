package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.ui.common.ResponsiveVerticalLayout;

@Route(value = "userModifyView", layout = MainLayout.class)
public class UserModifyView extends VerticalLayout {

    AccountService accountService;

    Account account;

    public UserModifyView(AccountService accountService) {
        this.accountService = accountService;
        this.account = accountService.getCurrentAccount();

        initComponent();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private void initComponent() {

        FormLayout form = new FormLayout();

        Image profileImage = new Image(account.getPictureURL(), "No Image");
        profileImage.setMaxWidth("96px");
        profileImage.setMaxHeight("96px");
        VerticalLayout profile_layout = new VerticalLayout(profileImage);
        profile_layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        profile_layout.setJustifyContentMode(JustifyContentMode.CENTER);
        profile_layout.add(new Button("Google에서 가져오기", event -> {
            this.account = accountService.syncProfileImage(account);
            UI.getCurrent().getPage().reload();
        }));
        form.addFormItem(profile_layout, "Profile Image");

        TextField txt_name = new TextField("이름", account.getName(), "");
        txt_name.setMaxLength(20);
        txt_name.setMinLength(1);
        Button btn_changeName = new Button("적용", event -> {
            if(txt_name.isEmpty() || txt_name.isInvalid()){
                Notification notification = new Notification();
                notification.setText("입력하신 이름을 확인해주세요.");
                notification.setDuration(1500);
                notification.open();
                txt_name.setValue(this.account.getName());
                return;
            }
            this.account = accountService.changeName(this.account, txt_name.getValue());
            Notification notification = new Notification();
            notification.setText("이름이 변경되었습니다.");
            notification.setDuration(1500);
            notification.open();
        });
        HorizontalLayout layout_name = new HorizontalLayout();
        layout_name.setJustifyContentMode(JustifyContentMode.CENTER);
        layout_name.setDefaultVerticalComponentAlignment(Alignment.END);
        layout_name.addAndExpand(txt_name);
        layout_name.add(btn_changeName);
        form.add(layout_name);

        TextField txt_oneline = new TextField("한줄소개", account.getOnelineIntroduce(), "");
        txt_oneline.setMaxLength(50);
        Button btn_changeOneLine = new Button("적용", event -> {
            this.account = accountService.changeOnelineIntroduce(this.account, txt_oneline.getValue());
            Notification notification = new Notification();
            notification.setText("한줄소개가 변경되었습니다.");
            notification.setDuration(1500);
            notification.open();
        });
        HorizontalLayout layout_OneLineme = new HorizontalLayout();
        layout_OneLineme.setJustifyContentMode(JustifyContentMode.CENTER);
        layout_OneLineme.setDefaultVerticalComponentAlignment(Alignment.END);
        layout_OneLineme.addAndExpand(txt_oneline);
        layout_OneLineme.add(btn_changeOneLine);
        form.add(layout_OneLineme);

        TextField txt_email = new TextField("email", account.getEmail(), "");
        txt_email.setReadOnly(true);
        form.add(txt_email);

        add(new ResponsiveVerticalLayout(form));
    }
}