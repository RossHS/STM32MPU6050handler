package com.khapilov.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Menu item for show 3D model of cube.
 *
 * @author Ross Khapilov
 * @version 1.0 31.05.2018
 */
class Figure3D extends AbstractAction {
    private JFrame app;
    private MainFrame frame;

    /**
     * @param name  the name for the menu item.
     * @param image the Icon for the menu item.
     * @param frame parent Frame.
     */
    Figure3D(String name, ImageIcon image, MainFrame frame) {
        super(name);
        this.frame = frame;
        putValue(Action.SMALL_ICON, image);
        putValue(Action.SHORT_DESCRIPTION, "Enable 3d modeling");
    }

    /**
     * After click at {@code 3DModel}, if we don`t have {@code FigureApp} object, create one, else set visible.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (app == null) app = new FigureApp(frame);
        if (!app.isVisible()) app.setVisible(true);
    }
}
