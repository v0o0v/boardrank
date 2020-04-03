package com.oscar.ui;

import com.oscar.backend.entity.Company;
import com.oscar.backend.entity.Contact;
import com.oscar.backend.service.CompanyService;
import com.oscar.backend.service.ContactService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("")
@CssImport("./styles/shared-styles.css")
public class MainView extends VerticalLayout {

    private ContactService contactService;
    private CompanyService companyService;

    private Grid<Contact> grid = new Grid<>(Contact.class);
    private TextField filterText = new TextField();
    private ContactForm contactForm;

    public MainView(ContactService contactService, CompanyService companyService) {
        this.contactService = contactService;
        this.companyService = companyService;
        contactForm = new ContactForm(this.companyService.findAll());

        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureFilter();

        Div content = new Div();
        content.setSizeFull();
        content.addClassName("content");
        content.add(grid, this.contactForm);

        add(filterText, content);

        updateList();
    }

    private void configureFilter() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email", "status", "company");

        grid.removeColumnByKey("company");

        grid.addColumn(contact -> {
            Company company = contact.getCompany();
            return company == null ? "-" : company.getName();
        }).setHeader("Company");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList() {
        grid.setItems(this.contactService.findAll(this.filterText.getValue()));
    }
}