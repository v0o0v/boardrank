package net.boardrank.boardgame.ui.common;

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

    private void initScreenType(int width) {
        if (width >= 600) {
            this.width = 30;
            this.screenType = ScreenType.LARGE;
        } else {
            this.width = 18;
            this.screenType = ScreenType.SMALL;
        }
        setWidth(this.width+"em");
    }
}