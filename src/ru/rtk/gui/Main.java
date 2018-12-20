package ru.rtk.gui;

/**
 *
 * Главный класс. Содержит функции создания графического интерфейса и
 * запуска в отдельных потоках классов, прослушивающих появление
 * новых тарификационных файлов в каталоге-источнике и классов, обрабатывающих
 * данные в тарификационных файлах.
 */
import ru.rtk.file.handler.DirChangeListener;
import ru.rtk.file.handler.HFThread;
import java.awt.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws AWTException {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("error:" + ex);
        } catch (InstantiationException ex) {
            System.out.println("error:" + ex);
        } catch (IllegalAccessException ex) {
            System.out.println("error:" + ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println("error:" + ex);
        }
        //</editor-fold>
        CreateTrayIcon createTrayIcon = new CreateTrayIcon();
        try {
            createTrayIcon.ShowTrayIcon();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //-------- В отдельном потоке запускаем слушатель добавление файлов в каталог
        Runnable r= new DirChangeListener();
        Thread t = new Thread(r);
        t.start();
        //-------- В отдельном потоке запустим обработчик файлов в каталоге
        Runnable r2= new HFThread();
        Thread t2 = new Thread(r2);
        t2.start();
    }
}
