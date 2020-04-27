package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.domain.Game;
import net.boardrank.boardgame.service.GameService;

@Route(value = "", layout = MainLayout.class)
public class GameListView extends VerticalLayout {

    GameService gameService;

    private Grid<Game> grid = new Grid<>(Game.class);
//    private TextField filterText = new TextField();
//    private ContactForm form;

    public GameListView(GameService gameService) {
        this.gameService = gameService;

        addClassName("list-view");
        setSizeFull();

//        form = new ContactForm(this.companyService.findAll());
//        form.addListener(ContactForm.SaveEvent.class, this::saveContact);
//        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
//        form.addListener(ContactForm.CloseEvent.class, e -> closeEditor());

        configureGrid();

//        Div content = new Div(grid, form);
        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();

//        add(getToolbar(), content);
        add(content);
        updateList();
//        closeEditor();
    }

//    private void configureFilter() {
//        filterText.setPlaceholder("Filter by name...");
//        filterText.setClearButtonVisible(true);
//        filterText.setValueChangeMode(ValueChangeMode.LAZY);
//        filterText.addValueChangeListener(e -> updateList());
//    }
//
//    private HorizontalLayout getToolbar() {
//        filterText.setPlaceholder("Filter by name...");
//        filterText.setClearButtonVisible(true);
//        filterText.setValueChangeMode(ValueChangeMode.LAZY);
//        filterText.addValueChangeListener(e -> updateList());
//
//        Button addContactButton = new Button("Add contact");
//        addContactButton.addClickListener(click -> addContact());
//
//        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
//        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
//        toolbar.addClassName("toolbar");
//        return toolbar;
//    }
//
//    void addContact() {
//        grid.asSingleSelect().clear();
//        editContact(new Contact());
//    }
//
//    private void saveContact(ContactForm.SaveEvent event) {
//        contactService.save(event.getContact());
//        updateList();
//        closeEditor();
//    }
//
//    private void deleteContact(ContactForm.DeleteEvent event) {
//        contactService.delete(event.getContact());
//        updateList();
//        closeEditor();
//    }
//
//    private void closeEditor() {
//        form.setContact(null);
//        form.setVisible(false);
//        removeClassName("editing");
//    }


    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.removeAllColumns();

        grid.addColumn(game -> {
            return game.getId();
        }).setHeader("ID");

        grid.addColumn(game -> {
            return game.getBoardGame().getName();
        }).setHeader("보드게임");

        grid.addColumn(game -> {
            return game.getGameTitle();
        }).setHeader("방이름");

        grid.addColumn(game -> {
            return game.getWinnerByString();
        }).setHeader("1등");

        grid.addColumn(game -> {
            return game.getPaticiant().getAccounts().size();
        }).setHeader("방인원");

        grid.addColumn(game -> {
            return game.getFinishedTime();
        }).setHeader("종료시간");

        grid.addColumn(game -> {
            return game.getPlayingTime();
        }).setHeader("플레이시간(분)");

//        grid.setColumns("보드게임", "방이름", "1등", "방인원", "방상태", "시작시간", "완료시간");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setResizable(true);

        });

//        grid.asSingleSelect().addValueChangeListener(event ->
//                editContact(event.getValue()));
    }

//    private void editContact(Game game) {
//        if (contact == null) {
//            closeEditor();
//        } else {
//            form.setContact(contact);
//            form.setVisible(true);
//            addClassName("editing");
//        }
//    }
//
    private void updateList() {
        grid.setItems(this.gameService.getGamesOfCurrentSessionAccount());
    }
}