package net.boardrank.boardgame.ui.event;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.dialog.Dialog;

public class DialogSuccessCloseActionEvent extends ComponentEvent<Dialog> {
    public DialogSuccessCloseActionEvent(Dialog source) {
        super(source, false);
    }
}
