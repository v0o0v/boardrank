package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;

public class ResponsiveDialog extends Dialog{

    protected int width;
    protected ScreenType screenType;

    public ResponsiveDialog() {

        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            initScreenType(extendedClientDetails.getScreenWidth());
        });

        UI.getCurrent().getPage().addBrowserWindowResizeListener(event -> {
            initScreenType(event.getWidth());
        });

    }

    private void initScreenType(int width){
        this.width = width;
        if(width>=600){
            setWidth("25em");
            this.screenType = ScreenType.LARGE;
        }else {
            setWidth("18em");
            this.screenType = ScreenType.SMALL;
        }
    }
}