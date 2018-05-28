package com.khapilov;

import com.khapilov.gui.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * @author Ross Khapilov
 * @version 1.0 19.05.2018
 */
public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setTitle("STM32 MPU6050");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
            System.out.println(Thread.currentThread().getName() + " invoker");
        });
        System.out.println(Thread.currentThread().getName() + " main");
    }
}
