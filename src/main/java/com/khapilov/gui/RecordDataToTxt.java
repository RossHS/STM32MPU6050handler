package com.khapilov.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Menu item for record data from COM port.
 *
 * @author Ross Khapilov
 * @version 1.0 23.05.2018
 */
class RecordDataToTxt extends AbstractAction {
    private JDialog dialog;
    private final MainFrame frame;

    /**
     * @param name  the name for the menu item.
     * @param image the Icon for the menu item.
     * @param frame parent Frame.
     */
    RecordDataToTxt(String name, ImageIcon image, MainFrame frame) {
        super(name);
        putValue(Action.SMALL_ICON, image);
        putValue(Action.SHORT_DESCRIPTION, "Create a data files");
        this.frame = frame;
    }

    /**
     * After click at {@code Record}, if we don`t have {@code RecordDialog} object, create one, else set visible.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (dialog == null) dialog = new RecordDialog(frame);
        if (!dialog.isVisible()) dialog.setVisible(true);
    }
}
