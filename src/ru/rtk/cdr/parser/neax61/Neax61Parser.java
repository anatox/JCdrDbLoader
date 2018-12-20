package ru.rtk.cdr.parser.neax61;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Neax61Parser {
    String firstRecordDateTime = "";
    String lastRecordDateTime = null;

    private  ArrayList<String> arrANumb;
    private  ArrayList<String> arrBNumb;
    private  ArrayList<String> arrDat;
    private  ArrayList<String> arrTim;
    private  ArrayList<Integer> arrDur;
    private  ArrayList<String> arrIncRt;
    private  ArrayList<String> arrOutRt;
    private  ArrayList<String> arrRecType;

    byte[] buffer = new byte[(int) new File(ru.rtk.file.handler.GlobalArgs.ffname()).length()];
    byte[] block = new byte[128];
    byte[] RecType = new byte[1];
    byte[] ANumb = new byte[15];
    byte[] ACat = new byte[2];
    byte[] BNumb = new byte[32];
    byte[] Dat = new byte[6];
    byte[] Tim = new byte[6];
    byte[] Dur = new byte[6];
    byte[] OutRoute = new byte[8];
    byte[] InRoute = new byte[8];
    BufferedInputStream f = null;
    int position =0;
    int i = 0;

    public  void execute() {
        arrANumb = new ArrayList<String>();
        arrBNumb = new ArrayList<String>();
        arrDat = new ArrayList<String>();
        arrTim = new ArrayList<String>();
        arrDur = new ArrayList<Integer>();
        arrIncRt = new ArrayList<String>();
        arrOutRt = new ArrayList<String>();
        arrRecType = new ArrayList<String>();

        try
        {
            f = new BufferedInputStream(new FileInputStream(ru.rtk.file.handler.GlobalArgs.ffname()));
            f.read(buffer);
        }   catch (IOException ex) {
            System.out.println("exception: "+ex);
        }
        finally
        {
            if (f != null)
            {
                try {
                    f.close();
                }
                catch (IOException ignored) { }
            }
        }

        while (i<buffer.length)
        {
            try {
                System.arraycopy(buffer, i, block, 0, 128);
            }  catch (ArrayIndexOutOfBoundsException aiobe)
            {
                System.out.println("Exeption in HandleTTFile.execute: ArrayIndexOutOfBoundsException: "+aiobe);
            }

            System.arraycopy(block, position+1, ANumb, 0, 15);
            System.arraycopy(block, position+16, ACat, 0, 2);
            System.arraycopy(block, position+22, BNumb, 0, 32);
            System.arraycopy(block, position+54, Dat, 0, 6);
            System.arraycopy(block, position+60, Tim, 0, 6);
            System.arraycopy(block, position+66, Dur, 0, 6);
            System.arraycopy(block, position+84, OutRoute, 0, 8);
            System.arraycopy(block, position+72, InRoute, 0, 8);
            System.arraycopy(block, position+107, RecType, 0, 1);

            arrANumb.add((new String(ANumb)).trim());
            arrBNumb.add((new String(BNumb)).trim());
            arrDat.add(InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim));
            arrTim.add(InSQLTimeFormat(Tim));
            arrDur.add(DurSec(Dur));
            arrIncRt.add((new String(InRoute)).trim());
            arrOutRt.add((new String(OutRoute)).trim());
            arrRecType.add(new String(RecType));

            if (firstRecordDateTime.equals("")) firstRecordDateTime=InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
            System.out.println(new String(ANumb)+"\t"+new String(ACat)+"\t"+new String(BNumb)+"\t"+new String(Dat)+"\t"+new String(Tim)+"\t"+new String(Dur)+"\t"+new String(OutRoute)+"\t"+new String(InRoute)+"\t"+new String(RecType));

            i=i+128;
        }
        lastRecordDateTime = InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
    }

    private  String InSQLDateFormat(byte[] Dat){
        String DateSQLFormat = null;
        byte[] year = new byte[4];
        byte[] month = new byte[2];
        byte[] day = new byte[2];
        year[0] = '2';
        year[1] = '0';
        year[2] = Dat[0];
        year[3] = Dat[1];
        month[0] = Dat[2];
        month[1] = Dat[3];
        day[0] = Dat[4];
        day[1] = Dat[5];
        DateSQLFormat = new String(year) + "-" + new String(month) + "-" + new String(day);
        year = null;
        month = null;
        day = null;
        return DateSQLFormat;
    }

    private  String InSQLTimeFormat(byte[] Tim){
        String TimeSQLFormat = null;
        byte[] hr = new byte[2];
        byte[] mn = new byte[2];
        byte[] sc = new byte[2];
        hr[0] = Tim[0];
        hr[1] = Tim[1];
        mn[0] = Tim[2];
        mn[1] = Tim[3];
        sc[0] = Tim[4];
        sc[1] = Tim[5];
        TimeSQLFormat = new String(hr) + ":" + new String(mn) + ":" + new String(sc);
        hr = null;
        mn = null;
        sc = null;
        return TimeSQLFormat;
    }

    private  int DurSec(byte[] Dur){
        int DurSecFormat = 0;

        byte[] hr = new byte[2];
        byte[] mn = new byte[2];
        byte[] sc = new byte[2];
        hr[0] = Dur[0];
        hr[1] = Dur[1];
        mn[0] = Dur[2];
        mn[1] = Dur[3];
        sc[0] = Dur[4];
        sc[1] = Dur[5];

        DurSecFormat = Integer.parseInt( new String(hr))*3600 + Integer.parseInt( new String(mn))*60 + Integer.parseInt( new String(sc));
        hr = null;
        mn = null;
        sc = null;
        return DurSecFormat;
    }

    public ArrayList<String> getANum(){
        return arrANumb;
    }

    public ArrayList<String> getBNum(){
        return arrBNumb;
    }

    public ArrayList<String> getDat(){
        return arrDat;
    }

    public ArrayList<String> getTim(){
        return arrTim;
    }

    public ArrayList<Integer> getDur(){
        return arrDur;
    }

    public ArrayList<String> getOutRt(){
        return arrOutRt;
    }

    public ArrayList<String> getIncRt(){
        return arrIncRt;
    }

    public ArrayList<String> getRecType(){
        return arrRecType;
    }

    public String getFirstRecDateTime(){
        return firstRecordDateTime;
    }

    public String getLastRecDateTime(){
        return lastRecordDateTime;
    }

    public void ClearAll(){
        arrANumb.clear();
        arrANumb = null;
        arrBNumb.clear();
        arrBNumb = null;
        arrDat.clear();
        arrDat = null;
        arrTim.clear();
        arrTim = null;
        arrDur.clear();
        arrDur = null;
        arrIncRt.clear();
        arrIncRt = null;
        arrOutRt.clear();
        arrOutRt = null;
        arrRecType.clear();
        arrRecType = null;
        buffer = null;
        block = null;
        RecType = null;
        ANumb = null;
        ACat = null;
        BNumb = null;
        Dat = null;
        Tim = null;
        Dur = null;
        OutRoute = null;
        InRoute = null;
        f = null;
    }
}
