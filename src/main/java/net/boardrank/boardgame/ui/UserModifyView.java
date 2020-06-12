package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.service.AccountService;

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


        form.addFormItem(profile_layout,"Profile Image");
        form.add(new TextField("이름", account.getName(), ""));
        form.add(new TextField("email", account.getEmail(), ""));

        add(new ResponsiveVerticalLayout(form));
    }
}