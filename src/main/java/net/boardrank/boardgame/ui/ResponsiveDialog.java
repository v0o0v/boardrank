package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class ResponsiveDialog extends Dialog{

    protected int width;

    public ResponsiveDialog() {

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            width = extendedClientDetails.getScreenWidth();
            if(width>=600){
                setWidth("25em");
            }else {
                setWidth("18em");
            }
        });

        UI.getCurrent().getPage().addBrowserWindowResizeListener(event -> {
            width = event.getWidth();
            if(width>=600){
                setWidth("25em");
            }else {
                setWidth("18em");
            }
        });

    }
}