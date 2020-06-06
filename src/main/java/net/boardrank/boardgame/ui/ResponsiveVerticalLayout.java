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

    public ResponsiveVerticalLayout(Component... components) {
        this();
        add(components);
    }

    private void initScreenType(int width) {
        if (width >= 600) {
            this.width = 35;
            this.screenType = ScreenType.LARGE;
        } else {
            this.width = 20;
            this.screenType = ScreenType.SMALL;
        }
        setWidth(this.width+"em");
    }

}