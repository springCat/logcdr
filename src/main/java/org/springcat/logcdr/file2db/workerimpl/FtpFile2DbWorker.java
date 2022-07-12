package org.springcat.logcdr.file2db.workerimpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.log.Log;
import org.springcat.logcdr.file2db.core.File2DbWorker;

import java.io.File;

public abstract class FtpFile2DbWorker<T> extends File2DbWorker<T> {

    private Ftp ftp = new Ftp("172.0.0.1");

    private String fileName;

    private String remotePath;

    private String localWorkerPath;

    private String localBackupPath;

    private String remoteFilePath = remotePath + FileUtil.FILE_SEPARATOR + fileName;

    private String localWorkFilePath = localWorkerPath + FileUtil.FILE_SEPARATOR + fileName;

    private String localBackupFilePath = localWorkerPath + FileUtil.FILE_SEPARATOR + fileName;


    @Override
    public File init() {
        return getRemoteFile();
    }


    @Override
    public void after(File2DbWorker<T> file2DbWorker){
        backupLocalFile();
        deleteRemoteFile();
    }

    private File getRemoteFile(){
        //下载远程文件
        File localWorkerFile = FileUtil.file(localWorkerPath);
        ftp.download(remotePath, fileName, localWorkerFile);
        return localWorkerFile;
    }

    private boolean deleteRemoteFile(){
        return ftp.delFile(remoteFilePath);
    }

    private boolean backupLocalFile(){
        try {
            FileUtil.move(FileUtil.newFile(localWorkerPath),FileUtil.newFile(localBackupFilePath),false);
        }catch (IORuntimeException exception){
            String rename = localBackupFilePath + DateUtil.current();
            try {
                FileUtil.move(FileUtil.newFile(localWorkerPath), FileUtil.newFile(rename), false);
            }catch (Exception e){
                Log.get().error(e);
                return false;
            }
        }
        return true;
    }




}
