package com.khapilov.gui;

import com.khapilov.dataparser.AccelerationData;
import com.khapilov.dataparser.AngleData;
import com.khapilov.dataparser.AngularVelocityData;
import com.khapilov.dataparser.Data;
import jssc.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Dialog for Gyro mouse.
 *
 * @author Ross Khapilov
 * @version 1.0 31.05.2018
 */
class MouseDialog extends JDialog {

    private final JPanel northPanel;

    private boolean onAction = false;

    private final AccelerationData accelerationData;
    private final AngularVelocityData angularVelocityData;
    private final AngleData angleData;

    private Robot mouse;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private double xScale = screenSize.getWidth() / 80;
    private double yScale = screenSize.getHeight() / 80;

    private int wheelConst = 50;
    private int clickConst = 17;

    /**
     * @param owner main frame reference.
     * @throws AWTException Signals that an Abstract Window Toolkit exception has occurred.
     */
    MouseDialog(MainFrame owner) throws AWTException {
        super(owner, "Gyro mouse", false);
        mouse = new Robot();
        Data[] data = owner.getDataArray();
        accelerationData = (AccelerationData) data[0];
        angularVelocityData = (AngularVelocityData) data[1];
        angleData = (AngleData) data[2];
        //**************Top panel with sittings sliders***************//
        northPanel = new JPanel(new GridBagLayout());

        // TODO: 01.06.2018 Доделать x
        JSlider sliderX = new JSlider();
        addSlider(sliderX, "X", 20, 4);
        sliderX.setValue(80);
        sliderX.addChangeListener(e -> xScale = screenSize.getWidth() / sliderX.getValue());
        sliderX.setEnabled(false);

        // TODO: 01.06.2018 Доделать y
        JSlider sliderY = new JSlider();
        addSlider(sliderY, "Y", 20, 4);
        sliderY.setValue(80);
        sliderY.setEnabled(false);


        JSlider sliderScroll = new JSlider();
        addSlider(sliderScroll, "Wheel", 10, 2);
        sliderScroll.setMinimum(30);
        sliderScroll.setMaximum(90);
        sliderScroll.setValue(wheelConst);
        sliderScroll.addChangeListener(e -> wheelConst = sliderScroll.getValue());

        JSlider sliderClick = new JSlider();
        addSlider(sliderClick, "Click", 5, 1);
        sliderClick.setMinimum(10);
        sliderClick.setMaximum(30);
        sliderClick.setValue(clickConst);
        sliderClick.addChangeListener(e -> clickConst = sliderClick.getValue());

        northPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Sensitivity"));

        add(northPanel, BorderLayout.NORTH);
        //*************Middle panel with buttons******************//
        JPanel middlePanel = new JPanel();

        // TODO: 01.06.2018 реализовать zero
        JButton setZeroButton = new JButton("Set zero");
        setZeroButton.addActionListener(e -> {
        });
        setZeroButton.setEnabled(false);

        JButton onButton = new JButton("ON");
        onButton.addActionListener(e -> {
            SerialPort serialPort = owner.getSerialPort();
            if (serialPort == null || !serialPort.isOpened()) {
                JOptionPane.showMessageDialog(this, "Serial Port isn`t open",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (onButton.getText().equals("ON")) {
                    onButton.setText("OFF");
                    onAction = true;
                    Thread actionThread = new Thread(new MouseAction());
                    actionThread.start();
                } else {
                    onButton.setText("ON");
                    onAction = false;
                }
            }
        });

        middlePanel.add(onButton);
        middlePanel.add(setZeroButton);


        add(middlePanel, BorderLayout.CENTER);
        setResizable(false);
        setLocation(screenSize.width / 4, screenSize.height / 4);
        pack();
    }

    /**
     * Setup JSlider and add it to the JPanel.
     *
     * @param s           JSlider reference.
     * @param description JSlider name.
     * @param majorSpace  Major spaces between ticks.
     * @param minorSpace  Minor spaces between ticks.
     */
    private void addSlider(JSlider s, String description, int majorSpace, int minorSpace) {
        s.addChangeListener(null);
        s.setMajorTickSpacing(majorSpace);
        s.setMinorTickSpacing(minorSpace);
        s.setPaintTicks(true);
        s.setPaintLabels(true);
        JPanel panel = new JPanel();
        panel.add(s);
        panel.add(new JLabel(description));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = northPanel.getComponentCount();
        gbc.anchor = GridBagConstraints.WEST;
        northPanel.add(panel, gbc);
    }

    /**
     * New thread that showing the work of the gyro mouse.
     */
    class MouseAction implements Runnable {
        @Override
        public void run() {
            try {
                while (onAction) {
                    Thread.sleep(5);

                    int x = (int) ((Math.abs(angleData.getYaw() - 180) - 140) * xScale);
                    int y = (int) ((angleData.getPitch() + 180 - 140) * yScale);
                    mouse.mouseMove(x, y);


                    if (angleData.getRoll() > wheelConst) mouse.mouseWheel(-1);
                    else if (angleData.getRoll() < -wheelConst) mouse.mouseWheel(1);

                    if (accelerationData.getAz() > clickConst) {
                        mouse.mousePress(InputEvent.BUTTON1_MASK);
                        mouse.mouseRelease(InputEvent.BUTTON1_MASK);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
