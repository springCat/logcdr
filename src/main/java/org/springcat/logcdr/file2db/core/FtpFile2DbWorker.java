package org.springcat.logcdr.file2db.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;

import java.io.File;

public class FtpFile2DbWorker<T> extends File2DbWorker<T> {

    @Override
    public File init() {
        //匿名登录（无需帐号密码的FTP服务器）
        Ftp ftp = new Ftp("172.0.0.1");
        //进入远程目录
        ftp.cd("/opt/upload");
        //下载远程文件
        File file = FileUtil.file("e:/test2.jpg");
        ftp.download("/opt/upload", "test.jpg", file);
        return file;
    }
}
