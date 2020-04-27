package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDateTime;

//@Route("login")
//@PageTitle("Login")
public class MatchCreateDialog extends Dialog {

    private Button confirm;

    public MatchCreateDialog() {
        createHeader();
        createContent();
        createFooter();
    }

    public void addConfirmationListener(ComponentEventListener listener) {
        confirm.addClickListener(listener);
    }

    private void createHeader() {
        add(new Label("New Match"));
    }

    private void createContent() {
        VerticalLayout content = new VerticalLayout();

        HorizontalLayout layout_matchName = new HorizontalLayout();
        Label lbl_matchName = new Label("매치 이름 : ");
        TextField txt_matchName = new TextField("보드게임 " + LocalDateTime.now().toString());
        layout_matchName.add(lbl_matchName, txt_matchName);
        content.add(layout_matchName);


        add(content);
    }

    private void createFooter() {
        Button abort = new Button("Abort");
        abort.addClickListener(buttonClickEvent -> close());
        confirm = new Button("Confirm");
        confirm.addClickListener(buttonClickEvent -> close());

        HorizontalLayout footer = new HorizontalLayout();
        footer.add(abort, confirm);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        add(footer);
    }

}
