package com.khapilov.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Ross Khapilov
 * @version 1.0 23.05.2018
 */
class RecordDataToTxt extends AbstractAction {
    private JDialog dialog;
    private final JFrame frame;

    RecordDataToTxt(String name, ImageIcon image, JFrame frame) {
        super(name);
        putValue(Action.SMALL_ICON, image);
        putValue(Action.SHORT_DESCRIPTION, "Create a data files");
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (dialog == null) dialog = new RecordDialog(frame);
        if (!dialog.isVisible()) dialog.setVisible(true);
    }
}
