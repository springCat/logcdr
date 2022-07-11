package org.springcat.logcdr.file2db.workerimpl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import org.springcat.logcdr.file2db.core.File2DbWorker;

import java.io.File;

public abstract class FtpFile2DbWorker<T> extends File2DbWorker<T> {

    private Ftp ftp = new Ftp("172.0.0.1");

    @Override
    public File init() {
        return getFile();
    }

    private File getFile(){
        //进入远程目录
        ftp.cd("/opt/upload");
        //下载远程文件
        File file = FileUtil.file("e:/test2.jpg");
        ftp.download("/opt/upload", "test.jpg", file);
        return file;
    }


}
