package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import net.boardrank.boardgame.service.AccountService;

public class SignUpDialog extends Dialog {

    private AccountService accountService;

    private EmailField emailField = new EmailField();
    private PasswordField passwordField1 = new PasswordField();
    private PasswordField passwordField2 = new PasswordField();
    private TextField name = new TextField();

    public SignUpDialog(AccountService accountService) {
        this.accountService = accountService;

        createHeader();
        createContent();
        createFooter();
    }

    private void createHeader() {
        add(new Label("신규 가입"));
    }

    private void createContent() {

        VerticalLayout content = new VerticalLayout();

        emailField.setLabel("Email: ");
        content.add(emailField);

        passwordField1.setLabel("Password: ");
        content.add(passwordField1);

        passwordField2.setLabel("Password 확인: ");
        content.add(passwordField2);

        name.setLabel("이름: ");
        name.setMaxLength(10);
        content.add(name);

        add(content);
    }

    private void createFooter() {
        Button abort = new Button("취소");
        abort.addClickListener(buttonClickEvent -> close());

        Button confirm = new Button("가입");
        confirm.addClickListener(buttonClickEvent -> {
            try {
                checkValidation();
                accountService.addNewAccount(emailField.getValue(), passwordField1.getValue(), name.getValue());
                Dialog welcomePopup = new Dialog(new Label("가입을 축하드립니다."));
                welcomePopup.open();
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
        if(emailField.isInvalid())
            throw new RuntimeException("email 형식이 아닙니다.");

        if(emailField.isEmpty())
            throw new RuntimeException("email을 입력해주세요.");

        if(accountService.isExistEmail(emailField.getValue()))
            throw new RuntimeException("이미 있는 email입니다.");


        if(passwordField1.isEmpty() || passwordField2.isEmpty())
            throw new RuntimeException("password를 입력해주세요.");
        
        if(!passwordField1.getValue().equals(passwordField2.getValue())) {
            throw new RuntimeException("패스워드가 동일하지 않습니다.");
        }

        if(name.isEmpty())
            throw new RuntimeException("name을 입력해 주세요.");

        if(accountService.isExistName(name.getValue()))
            throw new RuntimeException("이미 있는 이름입니다.");
    }
}
