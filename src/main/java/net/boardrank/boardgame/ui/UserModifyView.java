package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.ui.login.PasswordChangeDialog;

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
        form.add(new TextField("이름", account.getName(), ""));
        form.add(new TextField("email", account.getEmail(), ""));
        form.add(new Span(new Label("⠀")));
        form.add(new Button("비밀번호 변경", event -> {
            new PasswordChangeDialog(accountService).open();
        }));

        add(new ResponsiveVerticalLayout(form));
    }

}