package ru.rtk.cdr.parser.si2000;

import ru.rtk.file.handler.GlobalArgs;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Si2000parser {
    private  ArrayList<String> arrANumb;
    private  ArrayList<String> arrBNumb;
    private  ArrayList<String> arrDat;
    private  ArrayList<String> arrTim;
    private  ArrayList<Integer> arrDur;
    private  ArrayList<String> arrIncRt;
    private  ArrayList<String> arrOutRt;
    private  ArrayList<String> arrRecType;

    private int AddCentureToYear(int year) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        String yearS = dateFormat.format(calendar.getTime());
        int y1 = Integer.parseInt(yearS);
        dateFormat = new SimpleDateFormat("yy");
        yearS = dateFormat.format(calendar.getTime());
        int y2 = Integer.parseInt(yearS);
        int y3 = y1 - y2 ;
        return y3 + year;
    }

    private boolean thisCallRec(int value) {
        if( ((value >> 0) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisFAURec(int value) {
        if( ((value >> 1) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisFAISRec(int value) {
        if( ((value >> 2) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisCallSuccRec(int value) {
        if( ((value >> 3) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisPulseCounterRec(int value) {
        if( ((value >> 4) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisAMARec(int value) {
        if( ((value >> 5) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisAMAImmediateOutRec(int value) {
        if( ((value >> 6) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisTarDebOutRec(int value) {
        if( ((value >> 7) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisDebImmediateOutRec(int value) {
        if( ((value >> 0) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisOMOBRec(int value) {
        if( ((value >> 1) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisTMOBRec(int value) {
        if( ((value >> 2) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisPMOBRec(int value) {
        if( ((value >> 3) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisPMOBImmediateRec(int value) {
        if( ((value >> 4) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisREVRec(int value) {
        if( ((value >> 5) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisSYSErrRec(int value) {
        if( ((value >> 6) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisIncSideSelfTarRec(int value) {
        if( ((value >> 7) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisCentrexCallRec(int value) {
        if( ((value >> 0) & 1) == 1)
            return true;
        return false;
    }

    private boolean thisPrepaidCallRec(int value) {
        if( ((value >> 1) & 1) == 1)
            return true;
        return false;
    }

    private boolean isSingleRec(byte value) {
        if( (value >> 4) == 1 )
            return true;
        return false;
    }

    private boolean isFirstRecInSequenceRecs(byte value) {
        if( (value >> 4) == 2 )
            return true;
        return false;
    }

    private boolean isImmediateRecInSequenceRecs(byte value) {
        if( (value >> 4) == 3 )
            return true;
        return false;
    }

    private boolean isLastRecInSequenceRecs(byte value) {
        if( (value >> 4) == 4 ) {
            return true;
        }
        return false;
    }

    private String BCDtoString(byte bcd) {
        StringBuffer sb = new StringBuffer();

        byte high = (byte) (bcd & 0xf0);
        high >>>= (byte) 4;
        high = (byte) (high & 0x0f);
        byte low = (byte) (bcd & 0x0f);

        sb.append(high);
        sb.append(low);

        return sb.toString();
    }

    private String BCDtoString(byte[] bcd) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < bcd.length; i++) {
            sb.append(BCDtoString(bcd[i]));
        }

        return sb.toString();
    }

    private String getZoneCode(byte[] zcsn, int buflen, int digInZC, int digInSpisNum) {
        String zcsn_str = BCDtoString(zcsn);
        //System.out.printf("zcsn_str=%s\n",zcsn_str);

        String zc = zcsn_str.substring(0, digInZC);
        //System.out.printf("ZC=%s\n",zc);

        return zc;
    }

    private String getSpisNum(byte[] zcsn, int buflen, int digInZC, int digInSpisNum) {
        String zcsn_str = BCDtoString(zcsn);
        //System.out.printf("zcsn_str=%s\n",zcsn_str);

        String sn = zcsn_str.substring(digInZC, digInZC + digInSpisNum);
        //System.out.printf("SN=%s\n",sn);

        return sn;
    }

    public void execute() {
        arrANumb = new ArrayList<String>();
        arrBNumb = new ArrayList<String>();
        arrDat = new ArrayList<String>();
        arrTim = new ArrayList<String>();
        arrDur = new ArrayList<Integer>();
        arrIncRt = new ArrayList<String>();
        arrOutRt = new ArrayList<String>();
        arrRecType = new ArrayList<String>();
        String strANumb;
        String strBNumb;
        String strDat;
        String strTim;
        String strMon;
        String strDay;
        String strHours;
        String strMin;
        String strSec;
        int intDur;
        String strIncRt;
        String strOutRt;
        String strRecType;

        BufferedInputStream f = null;
        String fname = GlobalArgs.ffname();
        int fsize = (int) new File(fname).length();
        byte[] buffer = new byte[fsize];

        try {
            f = new BufferedInputStream(new FileInputStream(fname));
            f.read(buffer);
            System.out.println("SUCCESS: file was open " + fname);
        } catch (IOException ex) {
            System.out.println("exception: " + ex);
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException ignored) {
                    System.out.println("finally: IOException ignored");
                }
            } else {
                System.out.println("finally: f == null");
            }
        }

        int i = 0;
        int rc = 0;
        while ( i < buffer.length ) {
            strANumb = "NULL";
            strBNumb = "NULL";
            strDat = "NULL";
            strTim = "NULL";
            intDur = 0;
            strIncRt = "NULL";
            strOutRt = "NULL";
            strRecType = "NULL";

            int rectype = buffer[i] & 0xff;
            short reclen;
            rc++;
            System.out.printf("%d ",rc);
            switch (rectype) {
                case 200 :
                    reclen = ByteBuffer.wrap(buffer, i+1, 2).getShort();
                    int secnum = ByteBuffer.wrap(buffer, i+3, 4).getInt();
                    int pidtar = ByteBuffer.wrap(buffer, i+7, 4).getInt();
                    //System.out.print(String.format("SECNUM %d PIDTAR %d ", secnum, pidtar));

                    /* значения ниже определяются битовым полем, по сути для наших целей они не нужны
                    if( thisCallRec((int)(buffer[i+11] & 0xff)) ) {
                        System.out.print("CALL ");
                    }
                    if( thisFAURec((int)(buffer[i+11] & 0xff)) ) {
                        System.out.print("FAU ");
                    }
                    if( thisFAISRec((int)(buffer[i+11] & 0xff)) ) {
                        System.out.print("FAIS ");
                    }
                    if( thisCallSuccRec((int)(buffer[i+11] & 0xff)) ) {
                        System.out.print("CALLSUCC ");
                    }
                    if( thisPulseCounterRec((int)(buffer[i+11] & 0xff)) ) {
                        System.out.print("PULSEUSED ");
                    }
                    if( thisAMARec((int)(buffer[i+11] & 0xff)) ) {
                        System.out.print("AMAUSED ");
                    }
                    if( thisAMAImmediateOutRec((int)(buffer[i+11] & 0xff)) ) {
                        System.out.print("AMAIMM");
                    }
                    if( thisTarDebOutRec((int)(buffer[i+11] & 0xff)) ) {
                        System.out.print("DEBUSED ");
                    }

                    if( thisDebImmediateOutRec((int)(buffer[i+12] & 0xff)) ) {
                        System.out.print("DEBIMM ");
                    }
                    if( thisOMOBRec((int)(buffer[i+12] & 0xff)) ) {
                        System.out.print("OMOBUSED ");
                    }
                    if( thisTMOBRec((int)(buffer[i+12] & 0xff)) ) {
                        System.out.print("TMOBUSED ");
                    }
                    if( thisPMOBRec((int)(buffer[i+12] & 0xff)) ) {
                        System.out.print("PMOBUSED ");
                    }
                    if( thisPMOBImmediateRec((int)(buffer[i+12] & 0xff)) ) {
                        System.out.print("PMOBIMM ");
                    }
                    if( thisREVRec((int)(buffer[i+12] & 0xff)) ) {
                        System.out.print("REVUSED ");
                    }
                    if( thisSYSErrRec((int)(buffer[i+12] & 0xff)) ) {
                        System.out.print("SYSTEMERROR");
                    }
                    if( thisIncSideSelfTarRec((int)(buffer[i+12] & 0xff)) ) {
                        System.out.print("DEBUSED ");
                    }

                    if( thisCentrexCallRec( (int)(buffer[i+13] & 0xff)) ) {
                        System.out.print("CENTREX ");
                    }
                    if( thisPrepaidCallRec( (int)(buffer[i+13] & 0xff)) ) {
                        System.out.print("PREPAID ");
                    }

                    byte seq = buffer[i+14];
                    if( isSingleRec(seq) ) {
                        System.out.print("SEC-NO ");
                    }
                    if( isFirstRecInSequenceRecs(seq) ) {
                        System.out.print("FIRST ");
                    }
                    if( isImmediateRecInSequenceRecs(seq) ) {
                        System.out.print("IMMEDIATE ");
                    }
                    if( isLastRecInSequenceRecs(seq) ) {
                        System.out.print("LAST ");
                    }
                    ******************************************************** */

                    //byte lczlln = buffer[i+15];
                    int lczlln = buffer[i+15] & 0xFF; //получаю беззнаковое целое
                    int lac = lczlln >> 5;   //ДЛИНА КОДА ЗОНЫ. Количество цифр в коде зоны. цифры в BCD формате - две цифры в одном байте
                    int lln = lczlln & 0x1F; //ДЛИНА СПИСОЧНОГО НОМЕРА. Количество цифр в списочном номере. цифры в BCD формате - две цифры в одном байте
                    //System.out.printf("\nlszlln_HEX=%02X lczlln_DEC=%d lac=%d lln=%d\n",lczlln,lczlln,lac,lln);

                    int digitsInZoneCodeSpisNum = lac + lln; //количество цифр в буффере с кодом зоны и списочным номером ??? что за номер такой? спросить у Старикова.
                    //System.out.printf("\nAreaCodeLen=%02X SubscrNumLen=%d digitsInZoneCodeSpisNum=%d\n",lac,lln,digitsInZoneCodeSpisNum);
                    int bytesZoneCodeSpisNum; //длинна буфера для зонального кода и списочного номера
                    if( ( digitsInZoneCodeSpisNum % 2 == 0) ) { //если количество цифр нечётное, значит будет неопределённое значение, чтобы его не потерять делаем так...
                        bytesZoneCodeSpisNum = digitsInZoneCodeSpisNum/2;
                    } else {
                        bytesZoneCodeSpisNum = (digitsInZoneCodeSpisNum + 1)/2;
                    }
                    //System.out.printf("i=%d lczlln=%02X bytesZoneCodeSpisNum=%d digitsInZoneCodeSpisNum=%d lac=%d lln=%d\n",
                    //        i, lczlln, bytesZoneCodeSpisNum, digitsInZoneCodeSpisNum, lac, lln);

                    byte[] bufZoneCodeSpisNum = new byte[bytesZoneCodeSpisNum];
                    System.arraycopy(buffer, i+16, bufZoneCodeSpisNum, 0, bytesZoneCodeSpisNum);

                    String zc = getZoneCode(bufZoneCodeSpisNum, bytesZoneCodeSpisNum, lac, lln);
                    //System.out.printf("ZONE CODE=%s ", zc);
                    String sn = getSpisNum(bufZoneCodeSpisNum, bytesZoneCodeSpisNum, lac, lln);

                    //System.out.printf("LENGTH ZONE CODE %d ", lac);
                    //System.out.printf("LENGTH LIST NUM %d ", lln);
                    //System.out.printf("ZC=%s ANUM=%s ", zc, sn);
                    if(sn.length() > 0){
                        strANumb = zc + sn;
                    } else {
                        strANumb ="NULL";
                    }
                    System.out.printf("ANUM=%s ", strANumb);
                    bufZoneCodeSpisNum = null; //для того чтобы сборщик мусора освободил память

                    int offset = i+16+bytesZoneCodeSpisNum;
                    //System.out.printf("\n!!!!!>i=%d bytesZoneCodeSpisNum=%d offset=%d reclen=%d \n", i, bytesZoneCodeSpisNum, offset, reclen);
                    //System.out.printf("\n---!!!!!!> offset=%d reclen=%d \n", offset, reclen);
                    while (offset < i + reclen) {
                        //System.out.printf("\n-----!!!!!!> offset=%d i=%d reclen=%d \n", offset, i, reclen);
                        int varRecPartID = buffer[offset] & 0xff;
                        if(varRecPartID == 100) {
                            offset = offset + 1;
                            int digBnum = buffer[offset] & 0xff; // длина B номера
                            int bytesBnum;
                            if(digBnum % 2 == 0) {
                                bytesBnum = digBnum / 2;
                            } else {
                                bytesBnum = (digBnum + 1)/ 2;
                            }
                            byte[] bufBnum = new byte[bytesBnum];
                            offset = offset + 1;
                            System.arraycopy(buffer, offset, bufBnum, 0, bytesBnum);
                            String bnum = BCDtoString(bufBnum).substring(0, digBnum);
                            System.out.printf("BNUM=%s ", bnum);
                            if(bnum.length() > 0){
                                strBNumb = bnum;
                            } else {
                                strBNumb = "NULL";
                            }
                            bufBnum = null; //для того чтобы сборщик мусора освободил память
                            offset = offset + bytesBnum;
                            continue;
                        }
                        if(varRecPartID == 101) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            System.out.printf(";Call accepting party number: length==%d ", ielen);
                            offset = offset + ielen;
                            continue;
                            //System.out.print("Call accepting party number");
                        }
                        if(varRecPartID == 102) {
                            int year  = buffer[++offset] & 0xff;
                            year = AddCentureToYear(year);
                            int mon   = buffer[++offset] & 0xff;
                            int day   = buffer[++offset] & 0xff;
                            int hours = buffer[++offset] & 0xff;
                            int min   = buffer[++offset] & 0xff;
                            int sec   = buffer[++offset] & 0xff;
                            int msec  = buffer[++offset] & 0xff;
                            int f1    = buffer[++offset] & 0x1; // флаг последовательности, если 1 то эта запись часть последовательности записей
                            // и время это длительность этой последовательности
                            //System.out.printf("TIMEBEGIN=\'%d-%d-%d %d:%d:%d.%d\' ",year,mon,day,hours,min,sec,msec,f1); // время начала разговора
                            if(mon < 10){
                                strMon = "0" + Integer.toString(mon);
                            } else {
                                strMon = Integer.toString(mon);
                            }
                            if(day < 10){
                                strDay = "0" + Integer.toString(day);
                            } else {
                                strDay = Integer.toString(day);
                            }
                            if(hours < 10){
                                strHours = "0" + Integer.toString(hours);
                            } else {
                                strHours = Integer.toString(hours);
                            }
                            if(min < 10){
                                strMin = "0" + Integer.toString(min);
                            } else {
                                strMin = Integer.toString(min);
                            }
                            if(sec < 10){
                                strSec = "0" + Integer.toString(sec);
                            } else {
                                strSec = Integer.toString(sec);
                            }
                            strTim = strHours + ":" + strMin + ":" + strSec;
                            strDat = Integer.toString(year) + "-" + strMon + "-" + strDay + " " + strTim;
                            System.out.printf("TIMEBEGIN='%s' ", strDat);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 103) {
                            int year  = buffer[++offset] & 0xff;
                            int mon   = buffer[++offset] & 0xff;
                            int day   = buffer[++offset] & 0xff;
                            int hours = buffer[++offset] & 0xff;
                            int min   = buffer[++offset] & 0xff;
                            int sec   = buffer[++offset] & 0xff;
                            int msec  = buffer[++offset] & 0xff;
                            int f1    = buffer[++offset] & 0x1; // флаг равен 0 если системное время изменялось за время обслуживания вызова
                            //System.out.printf("TIMEND=\'%d-%d-%d %d:%d:%d.%d\' ",year,mon,day,hours,min,sec,msec,f1); // время завершения разговора
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 104) {
                            int x1 = buffer[++offset] & 0xFF;
                            x1 = x1 << 16;
                            int x2 = buffer[++offset] & 0xFF;
                            x2 = x2 << 8;
                            int x3 = buffer[++offset] & 0xFF;
                            int tarPulses = x1 | x2 | x3;
                            //System.out.printf("TARPULS %d %s", tarPulses, Integer.toBinaryString(tarPulses));
                            //System.out.printf("TARPULS %d ", tarPulses);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 105) {
                            int bsids = buffer[++offset] & 0xFF;
                            int bsptc = buffer[++offset] & 0xFF;
                            //System.out.printf("BASESRV %d %d ", bsids, bsptc);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 106) {
                            int addsrvci = buffer[++offset] & 0xFF;
                            //System.out.printf("Дополнительная услуга у инициатора вызова %d ", addsrvci);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 107) {
                            int addsrv = buffer[++offset] & 0xFF;
                            //System.out.printf("Дополнительная услуга %d ", addsrv);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 108) {
                            int typeinput = buffer[++offset] & 0xFF;
                            int addsrv = buffer[++offset] & 0xFF;
                            //System.out.printf("Администрирование услуги абонентом тип ввода %d Дополнительная услуга %d ", typeinput, addsrv);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 109) {
                            int numChars = buffer[++offset] & 0xFF;
                            int bytesSec;
                            if(numChars % 2 == 0) {
                                bytesSec = numChars / 2;
                            } else {
                                bytesSec = (numChars + 1)/ 2;
                            }
                            byte[] bufSec = new byte[bytesSec];
                            offset = offset + 1;
                            System.arraycopy(buffer, offset, bufSec, 0, bytesSec);
                            String secv = BCDtoString(bufSec).substring(0, numChars);
                            bufSec = null; //для того чтобы сборщик мусора освободил память
                            offset = offset + bytesSec;
                            //System.out.printf("Последовательность введённых символов. Число символов: %d Символы: %s", numChars, secv);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 110) {
                            int kategory = buffer[++offset] & 0xFF;
                            //System.out.printf("Исходящая категория %d ", kategory);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 111) {
                            int tarvec = buffer[++offset] & 0xFF;
                            //System.out.printf("Тарифное направление %d ", tarvec);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 112) {
                            int resunsuccall = buffer[++offset] & 0xFF;
                            //System.out.printf("Причина безуспешного вызова %d ", resunsuccall);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 113) {
                            int tractGroupID = ByteBuffer.wrap(buffer, ++offset, 2).getShort();
                            offset++;
                            int tractID = ByteBuffer.wrap(buffer, ++offset, 2).getShort();
                            offset++;
                            int moduleID = buffer[++offset] & 0xFF;
                            int portID = ByteBuffer.wrap(buffer, ++offset, 2).getShort();
                            offset++;
                            int channelID = buffer[++offset] & 0xFF;
                            System.out.printf("INC GRP=%d TRUNK=%d ", tractGroupID, tractID ); // Идентификация входящего тракта
                            strIncRt = "GRP" + Integer.toString(tractGroupID) + "-TRK" + Integer.toString(tractID);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 114) {
                            int tractGroupID = ByteBuffer.wrap(buffer, ++offset, 2).getShort();
                            offset++;
                            int tractID = ByteBuffer.wrap(buffer, ++offset, 2).getShort();
                            offset++;
                            int moduleID = buffer[++offset] & 0xFF;
                            int portID = ByteBuffer.wrap(buffer, ++offset, 2).getShort();
                            offset++;
                            int channelID = buffer[++offset] & 0xFF;
                            System.out.printf("OUT GRP=%d TRUNK=%d ", tractGroupID, tractID, moduleID, portID, channelID); // Идентификация исходящего тракта
                            strOutRt = Integer.toString(tractGroupID) + "_" + Integer.toString(tractID);
                            offset++;
                            continue;
                        }
                        if(varRecPartID == 115) {
                            int duration = ByteBuffer.wrap(buffer, ++offset, 4).getInt();
                            if (duration >= 1000) {
                                duration = duration / 1000;
                            }else{
                                if(duration > 0) {
                                    duration = 1;
                                }
                            }
                            System.out.printf("DUR=%d ", duration); // Длительность вызова или использования допуслуги
                            intDur = duration;
                            offset = offset + 4;
                            continue;
                        }
                        if(varRecPartID == 116) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            short checksum = ByteBuffer.wrap(buffer, offset+2, 2).getShort();
                            //System.out.printf(";Checksum: %02X ", checksum);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 117) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            ielen = ielen > 0 ? ielen : 10; // Длина данного информационного элемента зафиксирована и составляет 10 байтов
                            //System.out.printf(";Business and Centrex Group ID: length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 118) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            //System.out.printf(";Carrier Access Code: length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 119) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            //System.out.printf(";Original Calling Party Number: length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 120) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            ielen = ielen > 0 ? ielen : 15; // Длина данного информационного элемента зафиксирована и составляет 15 байтов
                            //System.out.printf(";Prepaid Account Recharge Data: length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 121) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            ielen = ielen > 0 ? ielen : 5; // Длина данного информационного элемента зафиксирована и составляет 5 байтов
                            //System.out.printf(";Call Release Cause: length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 122) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            ielen = ielen > 0 ? ielen : 5; // Длина данного информационного элемента зафиксирована и составляет 5 байтов
                            System.out.printf(";Charge Band Number: length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 123) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            ielen = ielen > 0 ? ielen : 6; // Длина данного информационного элемента зафиксирована и составляет 6 байтов
                            //System.out.printf(";Common Call ID: length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 124) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            ielen = ielen > 0 ? ielen : 10; // Длина данного информационного элемента зафиксирована и составляет 10 байтов
                            //System.out.printf(";Durations before Answer: length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 125) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            ielen = ielen > 0 ? ielen : 5; // Длина данного информационного элемента зафиксирована и составляет 5 байтов
                            //System.out.printf(";VoIP Info (old): length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 126) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            ielen = ielen > 0 ? ielen : 13; // Длина данного информационного элемента зафиксирована и составляет 13 байтов
                            //System.out.printf(";Amount of Transferred Data (old): length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 127) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            //System.out.printf(";IP Addresses: length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 128) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            ielen = ielen > 0 ? ielen : 13; // Длина данного информационного элемента зафиксирована и составляет 13 байтов
                            //System.out.printf(";VoIP Info: length==%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        if(varRecPartID == 129) {
                            int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                            ielen = ielen > 0 ? ielen : 25; // Длина данного информационного элемента зафиксирована и составляет 25 байтов
                            //System.out.printf(";Amount of Transferred Data: length=%d ", ielen);
                            offset = offset + ielen;
                            continue;
                        }
                        // пытаемся перескочить неизвестный элемент
                        int ielen = buffer[offset+1] & 0xFF; // длина информационного элемента в байтах
                        ielen = ielen > 0 ? ielen : 1; // делаем чтобы не ушёл в бесконечность
                        System.out.printf(";UNKNOWN PART ID %d: length=%d ", varRecPartID, ielen);
                        offset = offset + ielen;
                    }

                    System.out.println(); // конец записи типа 200
                    offset = 0;

                    arrANumb.add(strANumb);
                    arrBNumb.add(strBNumb);
                    arrDat.add(strDat);
                    arrTim.add(strTim);
                    arrDur.add(intDur);
                    arrIncRt.add(strIncRt);
                    arrOutRt.add(strOutRt);
                    arrRecType.add("200");

                    break;
                case 210 :
                    reclen = 19;
                    System.out.println(String.format("REC TYPE %d LEN %d", rectype, reclen));
                case 211 :
                    reclen = 16;
                    System.out.println(String.format("REC TYPE %d LEN %d", rectype, reclen));
                case 212 :
                    reclen = 12;
                    System.out.println(String.format("REC TYPE %d LEN %d", rectype, reclen));
                default:
                    System.out.println(String.format("RECTYPE NOT FOUND HEX: %02X INT: %d", buffer[0], rectype));
                    reclen = 1;
            }
            i = i + reclen;
            //System.out.printf("######### i=%d \n", i);
        }


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
    }
}
