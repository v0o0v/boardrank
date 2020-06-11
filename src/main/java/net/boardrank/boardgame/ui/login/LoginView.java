package net.boardrank.boardgame.ui.login;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    private static final String URL = "/oauth2/authorization/google";

    public LoginView() {
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Anchor googleLoginButton = new Anchor(URL, new Image("icons/google.png", "Google Login"));
        add(googleLoginButton);

        Image boardRank = new Image("logo/android-chrome-192x192.png", "BoardRank");
        boardRank.setMaxWidth("128px");
        boardRank.setMaxHeight("128px");
        add(
                new H1("")
                , new H1("")
                , boardRank
                , new H1("")
                , googleLoginButton
        );
    }
}


