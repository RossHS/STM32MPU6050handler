package com.khapilov.gui;

import com.khapilov.dataparser.AccelerationData;
import com.khapilov.dataparser.AngleData;
import com.khapilov.dataparser.AngularVelocityData;
import jssc.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * @author Ross Khapilov
 * @version 1.0 20.05.2018
 */
// TODO: 21.05.2018 основная проблема с циклом while(dataIsReady) который рисует график.
// Именно графики являются причиной утечки памяти
public class MainFrame extends JFrame {
    private SerialPort chosenPort;

    private final AccelerationData accelerationData = new AccelerationData();
    private final AngularVelocityData angularVelocityData = new AngularVelocityData();
    private final AngleData angleData = new AngleData();
    private boolean dataIsReady = false;

    private final JButton connectButton;

    private final JComboBox<String> portList;
    private final  JTextArea textArea;

    private final XYSeries[] accelerationSeries = new XYSeries[3];
    private final XYSeries[] angularVelocitySeries = new XYSeries[3];
    private final XYSeries[] angleSeries = new XYSeries[3];

    private int xAxies = 0;

    public MainFrame() {
        //***********************************************//
        //панель с кнопками
        JPanel selectPanel = new JPanel();
        portList = new JComboBox<>();
        String[] portNames = SerialPortList.getPortNames();
        for (String s : portNames)
            portList.addItem(s);
        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ConnectButtonEvent());

        JButton cleanChartButton = new JButton("Clean chart");
        cleanChartButton.addActionListener(e -> {
            for (XYSeries s : accelerationSeries)
                s.clear();
            for (XYSeries s : angularVelocitySeries)
                s.clear();
            for (XYSeries s : angleSeries)
                s.clear();
            xAxies = 0;
        });

        selectPanel.add(portList);
        selectPanel.add(connectButton);
        selectPanel.add(cleanChartButton);
        //***********************************************//
        //панель с цифрами
        JPanel dataPanel = new JPanel();
        textArea = new JTextArea(12, 12);
        textArea.setFont(new Font(null, Font.BOLD, 18));
        textArea.setEditable(false);
        dataPanel.add(textArea);
        //*********************************************//
        //графики
        JPanel chartPanel = new JPanel(new GridLayout(0, 1));

        XYSeriesCollection accelerationDataset = makeDataset(accelerationSeries, "Ax", "Ay", "Az");
        JFreeChart accelerationChart = ChartFactory.createXYLineChart(null, "Time",
                "Acceleration", accelerationDataset);
        accelerationChart.getXYPlot().setRangeAxisLocation(AxisLocation.TOP_OR_RIGHT);
        accelerationChart.getXYPlot().setRangeZeroBaselineVisible(true);

        XYSeriesCollection angularVelocityDataset = makeDataset(angularVelocitySeries, "Wx", "Wy", "Wz");
        JFreeChart angularVelocityChart = ChartFactory.createXYLineChart(null, "Time",
                "Angular velocity", angularVelocityDataset);
        angularVelocityChart.getXYPlot().setRangeAxisLocation(AxisLocation.TOP_OR_RIGHT);
        angularVelocityChart.getXYPlot().setRangeZeroBaselineVisible(true);

        XYSeriesCollection angleDataset = makeDataset(angleSeries, "Roll", "Pitch", "Yaw");
        JFreeChart angleChart = ChartFactory.createXYLineChart(null, "Time",
                "Angle", angleDataset);
        angleChart.getXYPlot().setRangeAxisLocation(AxisLocation.TOP_OR_RIGHT);
        angleChart.getXYPlot().setRangeZeroBaselineVisible(true);

        chartPanel.add(new ChartPanel(accelerationChart));
        chartPanel.add(new ChartPanel(angularVelocityChart));
        chartPanel.add(new ChartPanel(angleChart));

        add(selectPanel, BorderLayout.NORTH);
        add(dataPanel, BorderLayout.WEST);
        add(chartPanel, BorderLayout.CENTER);
        //*****************************************************//
        //Меню
        JMenu fileMenu = new JMenu("File");
        JMenuItem record = fileMenu.add(new RecordDataToTxt("Record",
                new ImageIcon(IconHelper.getImage("icon/Note.png")), this));
        record.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));

        JMenuItem scan = fileMenu.add(new Scan("Scan", portList,
                new ImageIcon(IconHelper.getImage("icon/Magnifier.png"))));
        scan.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        //****************************************************//
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
        pack();
    }

    private XYSeriesCollection makeDataset(XYSeries[] series, String... key) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i < series.length; i++) {
            series[i] = new XYSeries(key[i]);
            series[i].setMaximumItemCount(400);
            dataset.addSeries(series[i]);
        }
        return dataset;
    }

    public SerialPort getSerialPort() {
        return chosenPort;
    }

    private class ConnectButtonEvent implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Thread GUIinfo;
            if (connectButton.getText().equals("Connect")) {
                //attempt to connect to Serial Port
                chosenPort = new SerialPort(Objects.requireNonNull(portList.getSelectedItem()).toString());
                try {
                    chosenPort.openPort();
                    chosenPort.setParams(SerialPort.BAUDRATE_115200,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    chosenPort.addEventListener(new PortListener());
                } catch (SerialPortException exc) {
                    exc.printStackTrace();
                }
                if (chosenPort.isOpened()) {
                    connectButton.setText("Disconnect");
                    portList.setEnabled(false);
                }
                try {
                    Thread.sleep(200);
                    GUIinfo = new Thread(new GUIupdater());
                    GUIinfo.start();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } else {
                //disconnect from serial port
                try {
                    chosenPort.closePort();
                } catch (SerialPortException e1) {
                    e1.printStackTrace();
                }
                portList.setEnabled(true);
                dataIsReady = false;
                connectButton.setText("Connect");
            }
        }
    }

    private class PortListener implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            if (serialPortEvent.getEventValue() > 0) {
                try {
                    String[] array = chosenPort.readHexStringArray(11);
                    switch (array[1]) {
                        case "51":
                            accelerationData.setData(array);
                            dataIsReady = true;
                            accelerationData.calculateAllAccelerations();
                            break;
                        case "52":
                            angularVelocityData.setData(array);
                            angularVelocityData.calculateAllAngularVelocities();
                            break;
                        case "53":
                            angleData.setData(array);
                            angleData.calculateAllAngles();
                            break;
                        default:
                            chosenPort.readHexString(1); //if we get data with an offset
                            dataIsReady = false;
                            System.out.println("Skip");
                    }
                } catch (SerialPortException exc) {
                    exc.printStackTrace();
                }
            }
        }
    }

    private class GUIupdater implements Runnable {
        @Override
        public void run() {
            String info;
            System.out.println(Thread.currentThread().getName() + " GUIupdater");
            try {
                while (dataIsReady) {
                    Thread.sleep(5);
                    xAxies++;
                    info = String.format("Ax % .2f \n" + "Ay % .2f \n" + "Az % .2f \n\n" +
                                    "Wx % .2f \n" + "Wy % .2f \n" + "Wz % .2f \n\n" +
                                    "Roll % .2f \n" + "Pitch % .2f \n" + "Yaw % .2f \n\n" +
                                    "Temperature % .2f \n",
                            accelerationData.getAx(), accelerationData.getAy(), accelerationData.getAz(),
                            angularVelocityData.getWx(), angularVelocityData.getWy(), angularVelocityData.getWz(),
                            angleData.getRoll(), angleData.getPitch(), angleData.getYaw(),
                            angleData.calculateTemperature());
                    textArea.setText(info);

                    accelerationSeries[0].add(xAxies, accelerationData.getAx());
                    accelerationSeries[1].add(xAxies, accelerationData.getAy());
                    accelerationSeries[2].add(xAxies, accelerationData.getAz());

                    angularVelocitySeries[0].add(xAxies, angularVelocityData.getWx());
                    angularVelocitySeries[1].add(xAxies, angularVelocityData.getWy());
                    angularVelocitySeries[2].add(xAxies, angularVelocityData.getWz());

                    angleSeries[0].add(xAxies, angleData.getRoll());
                    angleSeries[1].add(xAxies, angleData.getPitch());
                    angleSeries[2].add(xAxies, angleData.getYaw());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
