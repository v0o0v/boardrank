package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.service.AccountService;

import java.util.Collections;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm login;

    private SignUpDialog signUpDialog;

    private AccountService accountService;

    public LoginView(AccountService accountService) {
        this.accountService = accountService;

        LoginI18n loginI18n = new LoginI18n();
        LoginI18n.Form form = new LoginI18n.Form();
        form.setUsername("Email");
        form.setPassword("Password");
        form.setSubmit("로그인");
        loginI18n.setForm(form);
        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle("오류");
        errorMessage.setTitle("Email과 Password를 확인해주세요.");
        loginI18n.setErrorMessage(errorMessage);

        this.login = new LoginForm(loginI18n);
        this.login.setForgotPasswordButtonVisible(false);

        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        UI.getCurrent().getPage().setTitle("BoardRank");

        Button btn_signup = new Button("신규 가입");
        btn_signup.addClickListener(event -> {
            signUpDialog = new SignUpDialog(this.accountService);
            signUpDialog.open();
        });

        login.setAction("login");

        add(new H1("BoardRank"), login, btn_signup);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // inform the user about an authentication error
        if (!event.getLocation()
                .getQueryParameters()
                .getParameters()
                .getOrDefault("error", Collections.emptyList())
                .isEmpty()) {
            login.setError(true);
        }
    }
}

