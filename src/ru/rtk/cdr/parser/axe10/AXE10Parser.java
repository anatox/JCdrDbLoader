package ru.rtk.cdr.parser.axe10;

import ru.rtk.file.handler.GlobalArgs;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AXE10Parser {
    int iter = 0;
    String firstRecordDateTime = "";
    String lastRecordDateTime = null;
    String strRecType = "";

    private ArrayList<String> arrANumb;
    private ArrayList<String> arrBNumb;
    private ArrayList<String> arrDat;
    private ArrayList<String> arrTim;
    private ArrayList<Integer> arrDur;
    private ArrayList<String> arrIncRt;
    private ArrayList<String> arrOutRt;
    private ArrayList<String> arrRecType;

    byte[] buffer = new byte[(int) new File(GlobalArgs.ffname()).length()];
    byte[] block = new byte[2048];
    byte[] RecType = new byte[2];
    byte[] ANumb = new byte[20];
    byte[] ACat = new byte[2];
    byte[] BNumb = new byte[18];
    byte[] Dat = new byte[6];
    byte[] Tim = new byte[6];
    byte[] Dur = new byte[6];
    byte[] OutRoute = new byte[7];
    byte[] InRoute = new byte[7];
    byte[] TypeANum = new byte[3];
    BufferedInputStream f = null;
    int position =0;
    int typfield =0;
    String strTypeField = "";
    int i = 0;

    public  void execute() throws IOException {
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
            f = new BufferedInputStream(new FileInputStream(GlobalArgs.ffname()));
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
                System.arraycopy(buffer, i, block, 0, 2048);
            }  catch (ArrayIndexOutOfBoundsException aiobe)
            {
                System.out.println("Exeption in HandleTTFile.execute: ArrayIndexOutOfBoundsException: "+aiobe);
                break;}

            while (position+1 < 2048) {
                System.arraycopy(block, position+0, RecType, 0, 2);
                if (RecType[0]==0 && RecType[1]==0) break; //если это символы заполнения с кодом 0 прерываем цикл

                strTypeField = new String(RecType);

                if(strTypeField.equals("00")){
                    System.arraycopy(block, position+72, ANumb, 0, 20);
                    System.arraycopy(block, position+34, ACat, 0, 2);
                    System.arraycopy(block, position+36, TypeANum, 0, 3);
                    System.arraycopy(block, position+45, BNumb, 0, 18);
                    System.arraycopy(block, position+119, Dat, 0, 6);
                    System.arraycopy(block, position+125, Tim, 0, 6);
                    System.arraycopy(block, position+143, Dur, 0, 6);
                    System.arraycopy(block, position+209, OutRoute, 0, 7);
                    System.arraycopy(block, position+216, InRoute, 0, 7);

                    arrANumb.add((new String(ANumb)).trim());
                    arrBNumb.add(cutLastF((new String(BNumb)).trim()));
                    arrDat.add(InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim));
                    arrTim.add(InSQLTimeFormat(Tim));
                    arrDur.add(DurSec(Dur));
                    arrIncRt.add((new String(InRoute)).trim());
                    arrOutRt.add((new String(OutRoute)).trim());
                    arrRecType.add(new String(RecType));
                    if(GlobalArgs.verb)
                        System.out.println(new String(RecType) + ";" + new String(ANumb) + ";" + new String(ACat) + ";" + new String(BNumb) + ";" + InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim) + ";" + InSQLTimeFormat(Tim) + ";" + DurSec(Dur) + ";" + new String(OutRoute) + ";" + new String(InRoute));
                    position=position+241;
                    iter++;
                    if (firstRecordDateTime.equals("")) firstRecordDateTime=InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
                }

                if(strTypeField.equals("01")){
                    System.arraycopy(block, position+72, ANumb, 0, 20);
                    System.arraycopy(block, position+34, ACat, 0, 2);
                    System.arraycopy(block, position+36, TypeANum, 0, 3);
                    System.arraycopy(block, position+45, BNumb, 0, 18);
                    System.arraycopy(block, position+119, Dat, 0, 6);
                    System.arraycopy(block, position+125, Tim, 0, 6);
                    System.arraycopy(block, position+143, Dur, 0, 6);
                    System.arraycopy(block, position+209, OutRoute, 0, 7);
                    System.arraycopy(block, position+216, InRoute, 0, 7);

                    arrANumb.add((new String(ANumb)).trim());
                    arrBNumb.add(cutLastF((new String(BNumb)).trim()));
                    arrDat.add(InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim));
                    arrTim.add(InSQLTimeFormat(Tim));
                    arrDur.add(DurSec(Dur));
                    arrIncRt.add((new String(InRoute)).trim());
                    arrOutRt.add((new String(OutRoute)).trim());
                    arrRecType.add(new String(RecType));
                    if(GlobalArgs.verb)
                        System.out.println(new String(RecType) + ";" + new String(ANumb) + ";" + new String(ACat) + ";" + new String(BNumb) + ";" + InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim) + ";" + InSQLTimeFormat(Tim) + ";" + "0" + ";" + new String(OutRoute) + ";" + new String(InRoute));
                    position=position+241;
                    iter++;
                    if (firstRecordDateTime.equals("")) firstRecordDateTime=InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
                }

                if(strTypeField.equals("09")){
                    System.arraycopy(block, position+72, ANumb, 0, 20);
                    System.arraycopy(block, position+34, ACat, 0, 2);
                    System.arraycopy(block, position+36, TypeANum, 0, 3);
                    System.arraycopy(block, position+45, BNumb, 0, 18);
                    System.arraycopy(block, position+119, Dat, 0, 6);
                    System.arraycopy(block, position+125, Tim, 0, 6);
                    System.arraycopy(block, position+143, Dur, 0, 6);
                    System.arraycopy(block, position+217, OutRoute, 0, 7);
                    System.arraycopy(block, position+224, InRoute, 0, 7);
                    //System.arraycopy(block, position+172, eos, 0, 4);  //EOS

                    arrANumb.add((new String(ANumb)).trim());
                    arrBNumb.add(cutLastF((new String(BNumb)).trim()));
                    arrDat.add(InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim));
                    arrTim.add(InSQLTimeFormat(Tim));
                    arrDur.add(DurSec(Dur));
                    arrIncRt.add((new String(InRoute)).trim());
                    arrOutRt.add((new String(OutRoute)).trim());
                    arrRecType.add(new String(RecType));
                    if(GlobalArgs.verb)
                        System.out.println(new String(RecType) + ";" + new String(ANumb) + ";" + new String(ACat) + ";" + new String(BNumb) + ";" + InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim) + ";" + InSQLTimeFormat(Tim) + ";" + DurSec(Dur)+ ";" + new String(OutRoute)+ ";" + new String(InRoute));
                    position=position+283;
                    iter++;
                    if (firstRecordDateTime.equals(""))
                        firstRecordDateTime=InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
                }

                if(strTypeField.equals("0A")){
                    System.arraycopy(block, position+72, ANumb, 0, 20);
                    System.arraycopy(block, position+34, ACat, 0, 2);
                    System.arraycopy(block, position+36, TypeANum, 0, 3);
                    System.arraycopy(block, position+45, BNumb, 0, 18);
                    System.arraycopy(block, position+119, Dat, 0, 6);
                    System.arraycopy(block, position+125, Tim, 0, 6);
                    System.arraycopy(block, position+143, Dur, 0, 6);
                    System.arraycopy(block, position+217, OutRoute, 0, 7);
                    System.arraycopy(block, position+224, InRoute, 0, 7);

                    arrANumb.add((new String(ANumb)).trim());
                    arrBNumb.add(cutLastF((new String(BNumb)).trim()));
                    arrDat.add(InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim));
                    arrTim.add(InSQLTimeFormat(Tim));
                    arrDur.add(DurSec(Dur));
                    arrIncRt.add((new String(InRoute)).trim());
                    arrOutRt.add((new String(OutRoute)).trim());
                    arrRecType.add(new String(RecType));
                    if(GlobalArgs.verb)
                        System.out.println(new String(RecType) + ";" + new String(ANumb) + ";" + new String(ACat) + ";" + new String(BNumb) + ";" + InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim) + ";" + InSQLTimeFormat(Tim) + ";" + "0"+ ";" + new String(OutRoute)+ ";" + new String(InRoute));
                    position=position+283;
                    iter++;
                    if (firstRecordDateTime.equals(""))
                        firstRecordDateTime=InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
                }

                if(strTypeField.equals("06")){
                    System.arraycopy(block, position+5, ANumb, 0, 10);
                    System.arraycopy(block, position+15, ACat, 0, 2);
                    System.arraycopy(block, position+23, BNumb, 0, 18);
                    System.arraycopy(block, position+50, Dat, 0, 6);
                    System.arraycopy(block, position+56, Tim, 0, 6);

                    arrANumb.add((new String(ANumb)).trim());
                    arrBNumb.add(cutLastF((new String(BNumb)).trim()));
                    arrDat.add(InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim));
                    arrTim.add(InSQLTimeFormat(Tim));
                    arrDur.add(0);
                    arrIncRt.add("undef");
                    arrOutRt.add("undef");
                    arrRecType.add(new String(RecType));
                    if(GlobalArgs.verb)
                        System.out.println(new String(RecType) + ";" + new String(ANumb) + ";" + new String(ACat) + ";" + new String(BNumb) + ";" + InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim) + ";" + InSQLTimeFormat(Tim) + ";" + DurSec(Dur));
                    position=position+126;
                    iter++;
                    if (firstRecordDateTime.equals("")) firstRecordDateTime=InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
                }

                if(strTypeField.equals("04")){
                    System.arraycopy(block, position+8, ANumb, 0, 10);
                    System.arraycopy(block, position+18, ACat, 0, 2);
                    System.arraycopy(block, position+29, BNumb, 0, 18);
                    System.arraycopy(block, position+147, Dat, 0, 6);
                    System.arraycopy(block, position+153, Tim, 0, 6);
                    System.arraycopy(block, position+171, Dur, 0, 6);
                    System.arraycopy(block, position+237, OutRoute, 0, 7);
                    System.arraycopy(block, position+244, InRoute, 0, 7);

                    arrANumb.add((new String(ANumb)).trim());
                    arrBNumb.add(cutLastF((new String(BNumb)).trim()));
                    arrDat.add(InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim));
                    arrTim.add(InSQLTimeFormat(Tim));
                    arrDur.add(DurSec(Dur));
                    arrIncRt.add((new String(InRoute)).trim());
                    arrOutRt.add((new String(OutRoute)).trim());
                    arrRecType.add(new String(RecType));
                    if(GlobalArgs.verb)
                        System.out.println(new String(RecType) + ";" + new String(ANumb) + ";" + new String(ACat) + ";" + new String(BNumb) + ";" + InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim) + ";" + InSQLTimeFormat(Tim) + ";" + DurSec(Dur));
                    position=position+302;
                    iter++;
                    if (firstRecordDateTime.equals("")) firstRecordDateTime=InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
                }

                if(strTypeField.equals("05")){
                    System.arraycopy(block, position+8, ANumb, 0, 10);
                    System.arraycopy(block, position+18, ACat, 0, 2);
                    System.arraycopy(block, position+29, BNumb, 0, 18);
                    System.arraycopy(block, position+147, Dat, 0, 6);
                    System.arraycopy(block, position+153, Tim, 0, 6);
                    System.arraycopy(block, position+171, Dur, 0, 6);
                    System.arraycopy(block, position+237, OutRoute, 0, 7);
                    System.arraycopy(block, position+244, InRoute, 0, 7);

                    arrANumb.add((new String(ANumb)).trim());
                    arrBNumb.add(cutLastF((new String(BNumb)).trim()));
                    arrDat.add(InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim));
                    arrTim.add(InSQLTimeFormat(Tim));
                    arrDur.add(0);
                    arrIncRt.add((new String(InRoute)).trim());
                    arrOutRt.add((new String(OutRoute)).trim());
                    arrRecType.add(new String(RecType));
                    if(GlobalArgs.verb)
                        System.out.println(new String(RecType) + ";" + new String(ANumb) + ";" + new String(ACat) + ";" + new String(BNumb) + ";" + InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim) + ";" + InSQLTimeFormat(Tim) + ";" + "0");
                    position=position+302;
                    iter++;
                    if (firstRecordDateTime.equals("")) firstRecordDateTime=InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
                }

                if(strTypeField.equals("07")){
                    System.arraycopy(block, position+5, ANumb, 0, 10);
                    System.arraycopy(block, position+15, ACat, 0, 2);
                    System.arraycopy(block, position+19, Dat, 0, 6);
                    System.arraycopy(block, position+25, Tim, 0, 6);

                    arrANumb.add((new String(ANumb)).trim());
                    arrBNumb.add(cutLastF((new String(BNumb)).trim()));
                    arrDat.add(InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim));
                    arrTim.add(InSQLTimeFormat(Tim));
                    arrDur.add(0);
                    arrIncRt.add("undef");
                    arrOutRt.add("undef");
                    arrRecType.add(new String(RecType));
                    if(GlobalArgs.verb)
                        System.out.println(new String(RecType) + ";" + new String(ANumb) + ";" + new String(ACat) + ";" + new String(BNumb) + ";" + InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim) + ";" + InSQLTimeFormat(Tim) + ";" + "undef");
                    position=position+79;
                    iter++;
                    if (firstRecordDateTime.equals("")) firstRecordDateTime=InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
                }

                if(strTypeField.equals("08")){
                    System.arraycopy(block, position+4, ANumb, 0, 10);
                    arrANumb.add((new String(ANumb)).trim());
                    arrBNumb.add("undef");
                    arrDat.add("undef");
                    arrTim.add("undef");
                    arrDur.add(0);
                    arrIncRt.add("undef");
                    arrOutRt.add("undef");
                    arrRecType.add(new String(RecType));
                    if(GlobalArgs.verb)
                        System.out.println(new String(RecType) + ";" + new String(ANumb) + ";" + "undef" + ";" + "undef" + ";" + "undef" + ";" + "undef" + ";" + "undef");
                    position=position+31;
                    iter++;
                }

                if(strTypeField.equals("02")){
                    System.arraycopy(block, position+72, ANumb, 0, 20);
                    System.arraycopy(block, position+34, ACat, 0, 2);
                    System.arraycopy(block, position+36, TypeANum, 0, 3);
                    System.arraycopy(block, position+45, BNumb, 0, 18);
                    System.arraycopy(block, position+119, Dat, 0, 6);
                    System.arraycopy(block, position+125, Tim, 0, 6);
                    System.arraycopy(block, position+143, Dur, 0, 6);
                    System.arraycopy(block, position+216, OutRoute, 0, 7);
                    System.arraycopy(block, position+223, InRoute, 0, 7);

                    arrANumb.add((new String(ANumb)).trim());
                    arrBNumb.add(cutLastF((new String(BNumb)).trim()));
                    arrDat.add(InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim));
                    arrTim.add(InSQLTimeFormat(Tim));
                    arrDur.add(DurSec(Dur));
                    arrIncRt.add((new String(InRoute)).trim());
                    arrOutRt.add((new String(OutRoute)).trim());
                    arrRecType.add(new String(RecType));

                    if(GlobalArgs.verb)
                        System.out.println(new String(RecType) + ";" + new String(ANumb) + ";" + new String(ACat) + ";" + new String(BNumb) + ";" + InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim) + ";" + InSQLTimeFormat(Tim) + ";" + DurSec(Dur) + ";" + new String(OutRoute) + ";" + new String(InRoute));
                    position=position+283;
                    iter++;
                    if (firstRecordDateTime.equals("")) firstRecordDateTime=InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
                }
            }
            position = 0;
            i = i + 2048;
        }
        lastRecordDateTime = InSQLDateFormat(Dat)+ " " + InSQLTimeFormat(Tim);
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

    private String InSQLDateFormat(byte[] Dat){
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

    private String InSQLTimeFormat(byte[] Tim){
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

    private int DurSec(byte[] Dur){
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

    public int getCount(){
        return iter;
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
        BNumb = null;
        Dat = null;
        Tim = null;
        Dur = null;
        OutRoute = null;
        InRoute = null;
        TypeANum = null;
        f = null;
    }

    public String cutLastF(String s){
        int pos=s.indexOf("F");
        if(pos<0) {return s;}
        else{
            s=s.substring(0, s.length()-1);
            return s;
        }


    }
}
