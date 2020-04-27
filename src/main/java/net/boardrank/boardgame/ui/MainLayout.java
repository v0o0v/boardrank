package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;

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
//        RouterLink link_history = new RouterLink("Match History", GameListView.class);
//        link_history.setHighlightCondition(HighlightConditions.sameLocation());

        Button btn_myRank = new Button("My Rank" , new Icon(VaadinIcon.CHART_3D));
        btn_myRank.addClickListener( e -> UI.getCurrent().navigate(GameListView.class));
        btn_myRank.setWidthFull();
        addToDrawer(btn_myRank);

        Button btn_matchHistory = new Button("Match History" , new Icon(VaadinIcon.LINE_CHART));
        btn_matchHistory.addClickListener( e -> UI.getCurrent().navigate(GameListView.class));
        btn_matchHistory.setWidthFull();
        addToDrawer(btn_matchHistory);

        Button btn_friend = new Button("My Friends" , new Icon(VaadinIcon.GROUP));
        btn_friend.addClickListener( e -> UI.getCurrent().navigate(FriendListView.class));
        btn_friend.setWidthFull();
        addToDrawer(btn_friend);
    }
}
