package com.khapilov.gui;

import jssc.SerialPortList;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Menu item for scan connected COM port.
 *
 * @author Ross Khapilov
 * @version 1.0 23.05.2018
 */
class Scan extends AbstractAction {
    private final JComboBox<String> portList;

    /**
     * @param name     the name for the menu item.
     * @param portList JComboBox for adding enabled COM ports.
     * @param image    the Icon for the menu item.
     */
    public Scan(String name, JComboBox<String> portList, ImageIcon image) {
        super(name);
        this.portList = portList;
        putValue(Action.SMALL_ICON, image);
        putValue(Action.SHORT_DESCRIPTION, "Scan connected COM ports");
    }

    /**
     * Update the port list.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        portList.removeAllItems();
        String[] portNames = SerialPortList.getPortNames();
        for (String s : portNames) {
            portList.addItem(s);
        }
    }
}
