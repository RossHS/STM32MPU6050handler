package com.khapilov.gui;

import com.khapilov.dataparser.AngleData;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

import javax.swing.*;


/**
 * Cube, the position of which depends on the position of the MPU6050
 *
 * @author Ross Khapilov
 * @version 1.0 01.06.2018
 */
class FigureApp extends JFrame {
    private AngleData angleData;
    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;

    /**
     * @param frame main frame reference.
     */
    FigureApp(MainFrame frame) {
        angleData = (AngleData) frame.getDataArray()[2];
        JFXPanel jfxPanel = new JFXPanel();
        Box box = new Box(400, 400, 400);
        box.setMaterial(new PhongMaterial(Color.AQUAMARINE));
        Group root = new Group(box);
        root.setTranslateX(350);
        root.setTranslateY(350);
        root.setTranslateZ(100);
        Rotate rxBox = new Rotate(0, Rotate.X_AXIS);
        Rotate ryBox = new Rotate(0, Rotate.Y_AXIS);
        Rotate rzBox = new Rotate(0, Rotate.Z_AXIS);
        box.getTransforms().addAll(rxBox, ryBox, rzBox);
        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        jfxPanel.setScene(scene);
        scene.setFill(Color.SNOW);
        SwingUtilities.invokeLater(() -> {
            setTitle("3D Model");
            add(jfxPanel);
            pack();
            setResizable(false);
            setVisible(true);
        });

        Thread print3D = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(3);
                    rxBox.setAngle(-angleData.getPitch());
                    ryBox.setAngle(-angleData.getYaw());
                    rzBox.setAngle(-angleData.getRoll());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        print3D.start();
    }
}
