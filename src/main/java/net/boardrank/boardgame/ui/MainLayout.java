package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

@CssImport("./styles/shared-styles.css")
@PageTitle("Board Rank")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Board Rank");
        logo.addClassName("logo");

        Anchor logout = new Anchor("/logout", "Log out");

        Button createMatch = new Button("Create New Match");
        MatchCreateDialog matchCreateDialog = new MatchCreateDialog();
        createMatch.addClickListener(event -> matchCreateDialog.open());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, createMatch, logout);
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Match History", GameListView.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                new RouterLink("My Rank", GameListView.class)
                , new RouterLink("My Friends", FriendListView.class)
                , listLink
        ));
    }
}
