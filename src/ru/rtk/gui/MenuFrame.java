package ru.rtk.gui;

import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class MenuFrame extends JFrame {
    public static final int DEFAULT_WIDTH=500;
    public static final int DEFAULT_HEIGHT=300;
    public static JLabel statusLabel;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu optionsMenu;
    public static DefaultListModel listModel = new DefaultListModel();

    public MenuFrame() throws Exception{
        setTitle("Загрузка тарификационных файлов в базу данных");
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/coins.png"));
        setIconImage(image);

        //создаем меню
        menuBar=new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.add(new AbstractAction("Exit") {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                System.exit(0);
            }
        });
        optionsMenu = new JMenu("Options");
        optionsMenu.add(new AbstractAction("Settings"){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                try {
                    new SettingsAction();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
        //конец создания меню

        //Добавляем StatusBar и List
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5,5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JList list = new JList(listModel);
        list.setSelectedIndex(0);
        mainPanel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 20));

        statusPanel.setLayout(new GridLayout());
        statusLabel = new JLabel("");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);

        getContentPane().add(mainPanel);

    }
}
