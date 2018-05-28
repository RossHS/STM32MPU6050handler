package com.khapilov.gui;

import jssc.SerialPortList;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Ross Khapilov
 * @version 1.0 23.05.2018
 */
class Scan extends AbstractAction {
    private final JComboBox<String> portList;

    public Scan(String name, JComboBox<String> portList, ImageIcon image) {
        super(name);
        this.portList = portList;
        putValue(Action.SMALL_ICON,image);
        putValue(Action.SHORT_DESCRIPTION,"Scan connected COM ports");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        portList.removeAllItems();
        String[] portNames = SerialPortList.getPortNames();
        for (String s : portNames) {
            portList.addItem(s);
        }
    }
}
