package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ResponsiveVerticalLayout extends VerticalLayout {

    protected int width;

    public ResponsiveVerticalLayout() {

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            width = extendedClientDetails.getScreenWidth();
            if(width>=600){
                setWidth("35em");
            }else {
                setWidth("20em");
            }
        });

        UI.getCurrent().getPage().addBrowserWindowResizeListener(event -> {
            width = event.getWidth();
            if(width>=600){
                setWidth("35em");
            }else {
                setWidth("20em");
            }
        });

//        setPadding(true);
//        setMargin(false);
    }

}