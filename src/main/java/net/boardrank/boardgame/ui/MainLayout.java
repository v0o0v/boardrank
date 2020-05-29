package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;

@CssImport("./styles/shared-styles.css")
@PageTitle("BoardRank")
public class MainLayout extends AppLayout implements PageConfigurator {

    private AccountService accountService;

    private BoardgameService boardGameService;

    private GameMatchService gameMatchService;
    private Button btn_currentMatch;
    private Tabs tabs;

    public MainLayout(AccountService accountService
            , BoardgameService boardGameService
            , GameMatchService gameMatchService
    ) {
        this.accountService = accountService;
        this.boardGameService = boardGameService;
        this.gameMatchService = gameMatchService;

        createHeader();
        createDrawer();
    }

    private void createHeader() {
        Image logo = new Image("icons/title.png", "BoardRank");
        Anchor logout = new Anchor("/logout", new Icon(VaadinIcon.EXIT_O));
        Button createMatch = new Button("New Match");
        createMatch.addClickListener(event -> {
            GameMatchCreateDialog gameMatchCreateDialog =
                    new GameMatchCreateDialog(
                            accountService
                            , boardGameService
                            , gameMatchService
                            , event1 -> setReloadAndNavigateToCurrentMatch()
                    );
            gameMatchCreateDialog.open();
        });
        Label lbl_empty = new Label();

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, lbl_empty, createMatch, logout);
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.expand(lbl_empty);

        addToNavbar(header);
    }

    private void createDrawer() {
        tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        Button btn_myRank = new Button("My Rank", new Icon(VaadinIcon.CHART_3D));
        btn_myRank.addClickListener(e -> UI.getCurrent().navigate(MyRankTab.class));
        btn_myRank.setWidthFull();
        tabs.add(new Tab(btn_myRank));

        btn_currentMatch = new Button("Current Match", new Icon(VaadinIcon.PLAY));
        btn_currentMatch.addClickListener(e -> UI.getCurrent().navigate(CurrentMatchListView.class));
        btn_currentMatch.setWidthFull();
        tabs.add(new Tab(btn_currentMatch));

        Button btn_matchHistory = new Button("Match History", new Icon(VaadinIcon.LINE_CHART));
        btn_matchHistory.addClickListener(e -> UI.getCurrent().navigate(GameMatchHistoryTab.class));
        btn_matchHistory.setWidthFull();
        tabs.add(new Tab(btn_matchHistory));

        Button btn_ToDoList = new Button("To Do", new Icon(VaadinIcon.PACKAGE));
        btn_ToDoList.addClickListener(e -> UI.getCurrent().navigate(ToDoListView.class));
        btn_ToDoList.setWidthFull();
        tabs.add(new Tab(btn_ToDoList));

        Button btn_friend = new Button("My Friends", new Icon(VaadinIcon.GROUP));
        btn_friend.addClickListener(e -> UI.getCurrent().navigate(FriendTab.class));
        btn_friend.setWidthFull();
        tabs.add(new Tab(btn_friend));

        tabs.setSelectedIndex(0);
        addToDrawer(tabs);
    }

    public void setReloadAndNavigateToCurrentMatch() {
        btn_currentMatch.click();
        UI.getCurrent().getPage().reload();
        this.tabs.setSelectedIndex(1);
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addLink("shortcut icon", "icons/favicon.png");
        settings.addFavIcon("icon", "icons/favicon.png", "32x32");
    }
}
