package ru.rtk.file.handler;

import java.io.File;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;

public class DirChangeListener implements FileListener, Runnable {
    FileObject listendir;
    DefaultFileMonitor fm;

    @Override
    public void fileChanged(FileChangeEvent fileChangeEvent) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void fileCreated(FileChangeEvent fileChangeEvent) throws Exception {
        System.out.println("listener->"+fileChangeEvent.getFile().getName().getBaseName());
        if(fileChangeEvent.getFile().getName().getBaseName().toString().contains(".ok")) {
            removeOK(fileChangeEvent.getFile().getName().getBaseName().toString());
        } else {
            GlobalArgs.listFiles.add(fileChangeEvent.getFile().getName().getBaseName());
        }

        synchronized (this) {
            this.notifyAll();
        }

    }

    private void removeOK(String okFile) {
        // удаляем файл *.ok
        File sourceFile = new File(GlobalArgs.pathFolder + "/" + okFile);
        if(sourceFile.delete()) System.out.println("File "+ okFile +" deleted succesfully");
    }

    @Override
    public void fileDeleted(FileChangeEvent arg0) throws Exception {

    }

    @Override
    public void run(){
        try {
            FileSystemManager fsManager = VFS.getManager();
            listendir = fsManager.resolveFile(GlobalArgs.pathFolder);
            fm = new DefaultFileMonitor(this);
            fm.setRecursive(true);
            fm.addFile(listendir);
            fm.start();

            synchronized (fm) {
                fm.wait();
            }
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
    }
}
