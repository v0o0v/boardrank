package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ResponsiveVerticalLayout extends VerticalLayout {

    protected int width;
    protected ScreenType screenType;

    public ResponsiveVerticalLayout() {

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            initScreenType(extendedClientDetails.getScreenWidth());
        });

        UI.getCurrent().getPage().addBrowserWindowResizeListener(event -> {
            initScreenType(event.getWidth());
        });
    }

    public ResponsiveVerticalLayout(Component... components){
        this();
        add(components);
    }

    private void initScreenType(int width){
        this.width = width;
        if(width>=600){
            setWidth("35em");
            this.screenType = ScreenType.LARGE;
        }else {
            setWidth("20em");
            this.screenType = ScreenType.SMALL;
        }
    }

}