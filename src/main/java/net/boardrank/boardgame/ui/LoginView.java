package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.boardrank.account.service.AccountService;

import java.util.Collections;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm login = new LoginForm();

    private SignUpDialog signUpDialog;

    private AccountService accountService;

    public LoginView(AccountService accountService) {
        this.accountService = accountService;

        this.login.setForgotPasswordButtonVisible(false);

        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Button btn_signup = new Button("신규 가입");
        btn_signup.addClickListener(event -> {
            signUpDialog = new SignUpDialog(this.accountService);
            signUpDialog.open();
        });

        login.setAction("login");

        add(new H1("Board Rank Login"), login, btn_signup);
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
