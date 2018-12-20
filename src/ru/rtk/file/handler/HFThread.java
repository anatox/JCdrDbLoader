package ru.rtk.file.handler;

import ru.rtk.cdr.db.loader.CdrToDbLoader;
import ru.rtk.cdr.parser.axe10.AXE10Parser;
import ru.rtk.cdr.parser.neax61.Neax61Parser;
import ru.rtk.cdr.parser.si2000.Si2000parser;
import ru.rtk.gui.MenuFrame;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HFThread implements Runnable {
    File f;
    File fSame;
    boolean fileClosed=false;

    @Override
    public void run() {
        while(true){ //в бесконечном цикле
            try {
                if(GlobalArgs.listFiles.size()>0){
                    GlobalArgs.dirname = GlobalArgs.pathFolder;
                    GlobalArgs.filename = GlobalArgs.listFiles.get(0);
                    System.out.println(GlobalArgs.ffname());
                    //здесь обрабатываем файл сначала проверяю не закрыт ли
                    f = new File(GlobalArgs.ffname());
                    fSame = new File(GlobalArgs.ffname());
                    if(f.renameTo(fSame)) fileClosed = true;
                    else fileClosed = false;
                    if(fileClosed) {  //если файл закрыт обрабатываем

                        GlobalArgs.progress = true;
                        CdrToDbLoader loader = new CdrToDbLoader();

                        if(GlobalArgs.verb)
                            System.out.println("обрабатываю: " + GlobalArgs.ffname());
                        if(GlobalArgs.isVisibleFrame){
                            MenuFrame.statusLabel.setText("обрабатываю: " + GlobalArgs.ffname());
                        }
                        System.out.println("typeATS: " + GlobalArgs.typeATS);
                        //-------------------------------------------------------------------------
                        if(GlobalArgs.typeATS.equals("Si2000")){
                            GlobalArgs.si2Kparser = new Si2000parser();
                            GlobalArgs.si2Kparser.execute();

                            if(loader.LoadCdrToDb() == true)
                            {
                                System.out.println(GlobalArgs.curdate() + "-->Данные из файла " +
                                        GlobalArgs.filename + " загружены в базу успешно");
                                MenuFrame.listModel.add(0, GlobalArgs.curdate() + "  Данные из файла " +
                                        GlobalArgs.filename + " загружены в базу успешно");
                            }
                            else
                            {
                                System.out.println(GlobalArgs.curdate() + "-->Данные из файла " +
                                        GlobalArgs.filename + " не загружены в базу");
                                MenuFrame.listModel.add(0,GlobalArgs.curdate() + "  Данные из файла " +
                                        GlobalArgs.filename + " не загружены в базу" + GlobalArgs.cause);
                            }

                            if(GlobalArgs.cause.equals("")){
                                f.delete();
                            }

                            GlobalArgs.si2Kparser.ClearAll();
                            GlobalArgs.si2Kparser = null;
                        }
                        //-------------------------------------------------------------------------
                        if(GlobalArgs.typeATS.equals("AXE10") ){ //если это файлы АХЕ-10
                            GlobalArgs.hTTFile = new AXE10Parser();
                            try {
                                GlobalArgs.hTTFile.execute();
                            } catch (IOException ex) {
                                Logger.getLogger(HFThread.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            if(loader.LoadCdrToDb() == true)
                            {
                                System.out.println(GlobalArgs.curdate() + "-->Данные из файла " +
                                        GlobalArgs.filename + " загружены в базу успешно");
                                MenuFrame.listModel.add(0, GlobalArgs.curdate() + "  Данные из файла " +
                                        GlobalArgs.filename + " загружены в базу успешно");
                            }
                            else
                            {
                                System.out.println(GlobalArgs.curdate() + "-->Данные из файла " +
                                        GlobalArgs.filename + " не загружены в базу");
                                MenuFrame.listModel.add(0,GlobalArgs.curdate() + "  Данные из файла " +
                                        GlobalArgs.filename + " не загружены в базу" + GlobalArgs.cause);
                            }

                            if(GlobalArgs.cause.equals("")){
                                f.delete();
                            }

                            GlobalArgs.hTTFile.ClearAll();
                            GlobalArgs.hTTFile = null;
                        }
                        //------------------------------------------------------------------------------------
                        if(GlobalArgs.typeATS.equals("NEAX61") ){ //если это файлы NEAX61
                            GlobalArgs.hCDRFile = new Neax61Parser();
                            GlobalArgs.hCDRFile.execute();

                            if(loader.LoadCdrToDb() == true)
                            {
                                System.out.println(GlobalArgs.curdate() + "-->Данные из файла " +
                                        GlobalArgs.filename + " загружены в базу успешно");
                                MenuFrame.listModel.add(0, GlobalArgs.curdate() + "  Данные из файла " +
                                        GlobalArgs.filename + " загружены в базу успешно");
                            }
                            else
                            {
                                System.out.println(GlobalArgs.curdate() + "-->Данные из файла " +
                                        GlobalArgs.filename + " не загружены в базу");
                                MenuFrame.listModel.add(0,GlobalArgs.curdate() + "  Данные из файла " +
                                        GlobalArgs.filename + " не загружены в базу" + GlobalArgs.cause);
                            }

                            if(GlobalArgs.cause.equals("")){
                                f.delete();
                            }

                            GlobalArgs.hCDRFile.ClearAll();
                            GlobalArgs.hCDRFile = null;
                        }

                        loader.closeconn();
                        loader = null;
                        //закончили загрузку в базу
                        if(GlobalArgs.isVisibleFrame){
                            MenuFrame.statusLabel.setText("Закончил: " + GlobalArgs.ffname());
                        }

                        GlobalArgs.listFiles.remove(0);
                    } else System.out.println("file " + GlobalArgs.ffname() + " still opened");
                } else {
                    GlobalArgs.progress = false;
                }


                Thread.sleep(5*1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
