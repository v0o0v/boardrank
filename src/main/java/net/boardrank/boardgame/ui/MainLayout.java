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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import net.boardrank.account.service.AccountService;
import net.boardrank.boardgame.service.BoardgameService;

@CssImport("./styles/shared-styles.css")
@PageTitle("Board Rank")
public class MainLayout extends AppLayout {

    private AccountService accountService;

    private BoardgameService boardGameService;

    public MainLayout(AccountService accountService, BoardgameService boardGameService) {
        this.accountService = accountService;
        this.boardGameService = boardGameService;

        createHeader();
        createDrawer();

        UI.getCurrent().navigate(MyRankListView.class);
    }

    private void createHeader() {
        H1 logo = new H1("Board Rank");
        logo.addClassName("logo");

        Anchor logout = new Anchor("/logout", new Icon(VaadinIcon.EXIT_O));

        Button createMatch = new Button("Create New Match");
        createMatch.addClickListener(event -> {
            MatchCreateDialog matchCreateDialog = new MatchCreateDialog(accountService, boardGameService);
            matchCreateDialog.open();
        });

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, createMatch, logout);
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        addToNavbar(header);
    }

    private void createDrawer() {
        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        Button btn_myRank = new Button("My Rank", new Icon(VaadinIcon.CHART_3D));
        btn_myRank.addClickListener(e -> UI.getCurrent().navigate(MyRankListView.class));
        btn_myRank.setWidthFull();
        tabs.add(new Tab(btn_myRank));

        Button btn_matchHistory = new Button("Match History", new Icon(VaadinIcon.LINE_CHART));
        btn_matchHistory.addClickListener(e -> UI.getCurrent().navigate(MatchHistoryView.class));
        btn_matchHistory.setWidthFull();
        tabs.add(new Tab(btn_matchHistory));

        Button btn_friend = new Button("My Friends", new Icon(VaadinIcon.GROUP));
        btn_friend.addClickListener(e -> UI.getCurrent().navigate(FriendListView.class));
        btn_friend.setWidthFull();
        tabs.add(new Tab(btn_friend));

        tabs.setSelectedIndex(0);
        addToDrawer(tabs);
    }
}
