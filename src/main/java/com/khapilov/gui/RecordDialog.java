package com.khapilov.gui;

import com.khapilov.dataparser.Buffer;
import jssc.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;

/**
 * Dialog for record data.
 *
 * @author Ross Khapilov
 * @version 1.0 24.05.2018
 */
class RecordDialog extends JDialog {
    private int second;

    private final MainFrame parent;

    private final JProgressBar progressBar;

    private final JTextField timeField;
    private final JButton recordButton;

    private final JCheckBox binaryBox;
    private final JCheckBox accelerationBox;
    private final JCheckBox angularVelocityBox;
    private final JCheckBox angleBox;

    /**
     * @param owner parent frame reference.
     */
    RecordDialog(MainFrame owner) {
        super(owner, "Record data dialog", true);
        parent = owner;
        //*************Bottom progress panel**********************//
        progressBar = new JProgressBar(0, 10);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(57, 180, 145));
        add(progressBar, BorderLayout.SOUTH);

        //*******Top input time and record button panel**********//
        JPanel topPanel = new JPanel();

        JLabel timeLabel = new JLabel("Seconds: ");
        timeField = new JTextField("10", 10);
        timeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char typedChar = e.getKeyChar();
                if (!Character.isDigit(typedChar)
                        || typedChar == KeyEvent.VK_BACK_SPACE
                        || typedChar == KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });
        recordButton = new JButton("Record");
        recordButton.addActionListener(new RecordButtonEvent());

        topPanel.add(timeLabel);
        topPanel.add(timeField);
        topPanel.add(recordButton);
        add(topPanel, BorderLayout.NORTH);

        //**************Middle checkbox panel*******************//
        JPanel middlePanel = new JPanel();

        binaryBox = new JCheckBox("Binary", true);
        accelerationBox = new JCheckBox("Acceleration");
        angularVelocityBox = new JCheckBox("Angular");
        angleBox = new JCheckBox("Angle");

        middlePanel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Record type"));

        middlePanel.add(binaryBox);
        middlePanel.add(accelerationBox);
        middlePanel.add(angularVelocityBox);
        middlePanel.add(angleBox);
        add(middlePanel, BorderLayout.CENTER);
        //***************************************************//

        System.out.println(Thread.currentThread().getName() + " recorddada");

        setResizable(false);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimension.width / 4, dimension.height / 4);
        pack();
    }

    /**
     * @param b set enable all components.
     */
    private void setEnabledComponents(boolean b) {
        recordButton.setEnabled(b);
        binaryBox.setEnabled(b);
        accelerationBox.setEnabled(b);
        angularVelocityBox.setEnabled(b);
        angleBox.setEnabled(b);
        timeField.setEnabled(b);
    }

    /**
     * Action listener for record button witch prepare this frame and start new thread.
     */
    class RecordButtonEvent implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SerialPort serialPort = parent.getSerialPort();
            if (serialPort == null || !serialPort.isOpened())
                JOptionPane.showMessageDialog(RecordDialog.this, "Serial Port isn`t open",
                        "Error", JOptionPane.ERROR_MESSAGE);
            else {
                second = Integer.parseInt(timeField.getText());
                progressBar.setMaximum(second);
                progressBar.setValue(0);
                setEnabledComponents(false);

                Thread recordThread = new Thread(new RecordThread());
                recordThread.start();
            }
        }
    }

    /**
     * Inner class which work in new thread and write different data in txt files.
     */
    class RecordThread implements Runnable {
        private int currentTime = 0;
        private int targetTime = second;

        @Override
        public void run() {
            String date = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
            File recordDir = new File("Record/Date " + date);
            if (!recordDir.exists()) //noinspection ResultOfMethodCallIgnored
                recordDir.mkdirs();

            try {
                Buffer.setUpTamer(targetTime * 1000);
                while (currentTime < targetTime) {
                    Thread.sleep(1000);
                    currentTime++;
                    progressBar.setValue(currentTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(RecordDialog.this, "Error!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (binaryBox.isSelected()) {
                writeData(recordDir, "Binary.txt", Buffer.getAllDataDeque());
            }
            if (accelerationBox.isSelected()) {
                writeData(recordDir, "Acceleration.txt", Buffer.getAccelerationDeque());
            }
            if (angularVelocityBox.isSelected()) {
                writeData(recordDir, "AngularVelocity.txt", Buffer.getAngularVelocityDeque());
            }
            if (angleBox.isSelected()) {
                writeData(recordDir, "Angle.txt", Buffer.getAngleDeque());
            }
            Buffer.clearOutBuffer();
            JOptionPane.showMessageDialog(RecordDialog.this, "Data successfully recorded!",
                    "done", JOptionPane.INFORMATION_MESSAGE);
            setEnabledComponents(true);
        }

        /**
         * Method to help write data to a file.
         *
         * @param recordDir folder for recording.
         * @param fileName  name of the data file.
         * @param queue     the queue from which we take the data.
         */
        private void writeData(File recordDir, String fileName, Queue<String> queue) {
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(new File(recordDir, fileName), true))) {
                int counter = 0;
                while (!queue.isEmpty()) {
                    counter++;
                    writer.write(queue.poll() + "\n");
                }
                writer.write(counter + "");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
