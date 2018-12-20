package ru.rtk.gui;

/***
 * Класс, создающий иконку в трее
 * Содержит внутренний класс, обрабатывающий нажатие мышкой на иконку
 *
 * */

import ru.rtk.file.handler.GlobalArgs;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CreateTrayIcon {

    public void ShowTrayIcon() throws IOException, AWTException {
        BufferedImage bufferedImage;

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        SystemTray tray = SystemTray.getSystemTray();
//        bufferedImage = ImageIO.read(getClass().getResource("coins.png"));
        bufferedImage = ImageIO.read(getClass().getResourceAsStream("/coins.png"));
        PopupMenu menu = new PopupMenu();

        MenuItem frameItem = new MenuItem("Show Frame");
        frameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuFrame frame;
                try {
                    frame = new MenuFrame();
                    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    frame.setVisible(true);
                    GlobalArgs.isVisibleFrame = true;
                } catch (Exception ex) {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
            }
        });
        menu.add(frameItem);

        MenuItem closeItem = new MenuItem("Close");
        closeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(closeItem);
        TrayIcon icon = new TrayIcon(bufferedImage, "Открыть двойным кликом", menu);
        icon.setImageAutoSize(true);
        icon.addMouseListener(new TrayMouseListener());

        tray.add(icon);

        //проверяем есть ли файл настроек
        GlobalArgs ga = new GlobalArgs();
        try {
            ga.checkSettingsFile();
        } catch (IOException ex) {
            System.out.println("error:" + ex);
        }
    }


    private class TrayMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                MenuFrame frame;
                try {
                    frame = new MenuFrame();
                    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    frame.setVisible(true);
                    GlobalArgs.isVisibleFrame = true;
                } catch (Exception ex) {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
            }
        }
    }
}
