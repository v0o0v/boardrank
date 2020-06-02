package net.boardrank.boardgame.ui.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import lombok.extern.slf4j.Slf4j;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.ui.ResponsiveDialog;

@Slf4j
public class PasswordChangeDialog extends ResponsiveDialog {

    private AccountService accountService;

    private PasswordField passwordField1 = new PasswordField();
    private PasswordField passwordField2 = new PasswordField();

    public PasswordChangeDialog(AccountService accountService) {
        this.accountService = accountService;

        createHeader();
        createContent();
        createFooter();
    }

    private void createHeader() {
        add(new H5("비밀번호 변경"));
    }

    private void createContent() {

        passwordField1.setLabel("Password: ");
        passwordField1.setWidthFull();
        passwordField1.setMinLength(1);
        add(passwordField1);

        passwordField2.setLabel("Password 확인: ");
        passwordField2.setWidthFull();
        passwordField2.setMinLength(1);
        add(passwordField2);
    }

    private void createFooter() {
        Button abort = new Button("취소");
        abort.addClickListener(buttonClickEvent -> close());

        Button confirm = new Button("변경");
        confirm.addClickListener(buttonClickEvent -> {
            try {
                checkValidation();
                accountService.changePassword(accountService.getCurrentAccount().getEmail(), passwordField1.getValue());
                Notification notification = new Notification("비밀번호가 변경되었습니다.");
                notification.setDuration(1500);
                notification.open();
                close();
            } catch (Exception e) {
                Dialog errorPopup = new Dialog(new Label(e.getMessage()));
                errorPopup.open();
            }
        });

        HorizontalLayout footer = new HorizontalLayout();
        footer.add(abort, confirm);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        add(footer);
    }

    private void checkValidation() {
        if(passwordField1.isEmpty() || passwordField2.isEmpty())
            throw new RuntimeException("password를 입력해주세요.");

        if(!passwordField1.getValue().equals(passwordField2.getValue())) {
            throw new RuntimeException("패스워드가 동일하지 않습니다.");
        }
    }

}
