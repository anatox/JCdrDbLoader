package ru.rtk.gui;

import ru.rtk.file.handler.GlobalArgs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SettingsAction extends JFrame {
    private JTextField verbose;
    private JTextField dbsid;
    private JTextField rdbmsip;
    private JTextField dblogin;
    private JTextField dbpassword;
    private JTextField pathFolder;
    private JButton okButton;
    private JButton canButton;
    private JButton imButton;
    private JComboBox ATScombo;
    Properties mySettings;
    File f = null;
    private JFileChooser chooser;
    private BufferedImage image;
    private BufferedImage image2;

    public SettingsAction() throws Exception{
        setTitle("Settings");
        setSize(400,250);

        JPanel content = new JPanel(new GridBagLayout());
        content.add(new JLabel("Verbose"), new GridBagConstraints(0,0,1,1,0,0,
                GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        verbose = new JTextField(15);
        verbose.setToolTipText("true показывать процессинг");
        content.add(verbose, new GridBagConstraints(1,0,1,1,0,0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        content.add(new JLabel("DbsID"), new GridBagConstraints(0,1,1,1,0,0,
                GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        dbsid = new JTextField(15);
        dbsid.setToolTipText("Имя базы");
        content.add(dbsid, new GridBagConstraints(1,1,1,1,0,0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        content.add(new JLabel("RdbmsIP"), new GridBagConstraints(0,2,1,1,0,0,
                GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        rdbmsip = new JTextField(15);
        rdbmsip.setToolTipText("IP адрес сервера");
        content.add(rdbmsip, new GridBagConstraints(1,2,1,1,0,0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        content.add(new JLabel("DbLogin"), new GridBagConstraints(0,3,1,1,0,0,
                GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        dblogin = new JTextField(15);
        dblogin.setToolTipText("Логин");
        content.add(dblogin, new GridBagConstraints(1,3,1,1,0,0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        content.add(new JLabel("DbPassword"), new GridBagConstraints(0,4,1,1,0,0,
                GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        dbpassword = new JTextField(15);
        dbpassword.setToolTipText("Пароль");
        content.add(dbpassword, new GridBagConstraints(1,4,1,1,0,0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));

        content.add(new JLabel("SrcPath"), new GridBagConstraints(0,5,1,1,0,0,
                GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        pathFolder = new JTextField(15);
        pathFolder.setToolTipText("Путь к каталогу с исходными файлами");
        content.add(pathFolder, new GridBagConstraints(1,5,1,1,0.8,0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        image = ImageIO.read(getClass().getResourceAsStream("/folder-close-icon.png"));
        ImageIcon icon = new ImageIcon(image);
        imButton = new JButton(icon);

        image2 = ImageIO.read(getClass().getResourceAsStream("/folder-documents-icon.png"));
        imButton.setRolloverIcon(new ImageIcon(image2));
        imButton.setToolTipText("Установить каталог");
        imButton.setBorderPainted(false);
        imButton.setFocusPainted(false);
        imButton.setContentAreaFilled(false);

        imButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // Выбираем каталог в который будут складываться тарификационные файлы
                chooser=new JFileChooser();
                chooser.setCurrentDirectory(new File("."));
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = chooser.showOpenDialog(SettingsAction.this);
                if(result==JFileChooser.APPROVE_OPTION){
                    System.out.println(chooser.getSelectedFile().getPath());
                    pathFolder.setText(chooser.getSelectedFile().getPath());
                }
            }
        });
        content.add(imButton, new GridBagConstraints(2,5,1,1,0.2,0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1,1,1,1),0,0));

        //добавляем ComboBox
        ATScombo = new JComboBox();
        ATScombo.addItem("AXE10");
        ATScombo.addItem("NEAX61");
        ATScombo.addItem("Si2000");
        ATScombo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {

                if(ATScombo.getSelectedItem().equals("AXE10")){
                    System.out.println("это AXE10!");
                }
                if(ATScombo.getSelectedItem().equals("NEAX61")){
                    System.out.println("это NEAX61!");
                }
            }
        });
        content.add(ATScombo, new GridBagConstraints(1,7,1,1,0.2,0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1,1,1,1),0,0));
        getContentPane().add(content, BorderLayout.NORTH);

        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                mySettings.setProperty("verbose", verbose.getText());
                mySettings.setProperty("dbsid", dbsid.getText());
                mySettings.setProperty("rdbmsip", rdbmsip.getText());
                mySettings.setProperty("dblogin", dblogin.getText());
                mySettings.setProperty("dbpassword", dbpassword.getText());
                mySettings.setProperty("pathfolder", pathFolder.getText());
                mySettings.setProperty("typeats", ATScombo.getSelectedItem().toString());
                FileOutputStream out;

                if(verbose.getText().equals("true")) GlobalArgs.verb = true; else GlobalArgs.verb = false;
                GlobalArgs.dbsid = dbsid.getText();
                GlobalArgs.rdbmsip = rdbmsip.getText();
                GlobalArgs.dblogin = dblogin.getText();
                GlobalArgs.dbpassword = dbpassword.getText();
                GlobalArgs.pathFolder = pathFolder.getText();
                GlobalArgs.typeATS = ATScombo.getSelectedItem().toString();
                try {
                    out = new FileOutputStream("mysettings.ini");
                    mySettings.store(out, null);
                    out.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                setVisible(false);
            }

        });

        canButton = new JButton("Cancel");
        canButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
            }

        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(canButton);
        add(buttonPanel,BorderLayout.CENTER);

        pack();
        setVisible(true);
        getMySettings();
    }

    private void getMySettings() throws Exception{
        mySettings = new Properties();
        f = new File("mysettings.ini");
        if(f.exists()){
            FileInputStream in = new FileInputStream("mysettings.ini");
            mySettings.load(in);
            in.close();

            verbose.setText(mySettings.getProperty("verbose","none"));
            dbsid.setText(mySettings.getProperty("dbsid","none"));
            rdbmsip.setText(mySettings.getProperty("rdbmsip","none"));
            dblogin.setText(mySettings.getProperty("dblogin","none"));
            dbpassword.setText(mySettings.getProperty("dbpassword","none"));
            pathFolder.setText(mySettings.getProperty("pathfolder","none"));

            ATScombo.setSelectedItem(mySettings.getProperty("typeats","AXE10"));
        } else {
            verbose.setText("none");
            dbsid.setText("none");
            rdbmsip.setText("none");
            dblogin.setText("none");
            dbpassword.setText("none");
            pathFolder.setText("none");
        }
    }
}
