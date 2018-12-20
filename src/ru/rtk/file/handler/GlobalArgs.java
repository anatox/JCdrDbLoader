package ru.rtk.file.handler;

import ru.rtk.cdr.parser.axe10.AXE10Parser;
import ru.rtk.cdr.parser.neax61.Neax61Parser;
import ru.rtk.cdr.parser.si2000.Si2000parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GlobalArgs {
    public static Si2000parser si2Kparser;
    public static Neax61Parser hCDRFile;
    public static AXE10Parser hTTFile;
    Properties mySettings;
    File f = null;

    public static boolean progress = false;
    public static String filename = null;
    public static String dirname = null;
    public static String dblogin = null;
    public static String dbpassword = null;
    public static String dbsid = null;
    public static String rdbmsip = null;
    static public boolean verb = false;
    public static String typeATS = null;
    public static String pathFolder = null; //путь к каталогу который будет под наблюдением
    public static String pathDstFolder = null; //путь к каталогу в который будем копировать
    public static boolean isVisibleFrame = false;
    public static String cause;

    public static List<String> listFiles = new ArrayList<String>();  //список файлов которые подлежат обработке

    public void checkSettingsFile() throws IOException{
        f = new File("mysettings.ini");
        if(f.exists()){
            setMyOptions();

        } else {
            System.out.println("Проведите настройку доступа к Oracle!");
        }
    }

    void setMyOptions() throws IOException {
        // устанавливаем переменные согласно файла настроек
        mySettings = new Properties();
        f = new File("mysettings.ini");
        FileInputStream in = new FileInputStream("mysettings.ini");
        mySettings.load(in);
        in.close();
        if(mySettings.getProperty("verbose").equals("true"))
        {
            verb=true;
        }   else {
            verb=false;
        }
        dbsid = mySettings.getProperty("dbsid");
        rdbmsip = mySettings.getProperty("rdbmsip");
        dblogin = mySettings.getProperty("dblogin");
        dbpassword = mySettings.getProperty("dbpassword");
        pathFolder = mySettings.getProperty("pathfolder");
        pathDstFolder = mySettings.getProperty("pathdstfolder");
        typeATS = mySettings.getProperty("typeats");
        System.out.println("dbsid=" + dbsid);
    }

    public static String md5sum()
    {
        String md5 = null;
        try
        {
            FileInputStream f = new FileInputStream(new File(ffname()));
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(f);
            f.close();
        }
        catch (IOException ex)
        {
            System.out.println("exception in GlobalArgs.md5sum(): " + ex );
        }
        return md5;
    }

    public static String curdate()
    {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    public static String ffname()
    {
//        String fullfn = dirname + "\\" + filename ;
        String fullfn = dirname + "/" + filename ;
        return fullfn;
    }

}
