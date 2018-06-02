package com.khapilov.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Menu item for Gyro Mouse feature.
 *
 * @author Ross Khapilov
 * @version 1.0 31.05.2018
 */
class GyroMouse extends AbstractAction {
    private JDialog dialog;
    private final MainFrame frame;

    /**
     * @param name  the name for the menu item.
     * @param image the Icon for the menu item.
     * @param frame parent Frame.
     */
    GyroMouse(String name, ImageIcon image, MainFrame frame) {
        super(name);
        putValue(Action.SMALL_ICON, image);
        putValue(Action.SHORT_DESCRIPTION, "Use a gyro as a mouse");
        this.frame = frame;
    }

    /**
     * After click at {@code Gyro Mouse}, if we don`t have {@code MouseDialog} object, create one, else set visible.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (dialog == null) {
            try {
                dialog = new MouseDialog(frame);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
        }
        if (!dialog.isVisible()) dialog.setVisible(true);
    }
}
