package org.springcat.logcdr.file2db.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Getter;
import lombok.Setter;
import java.io.File;

@Getter
@Setter
public abstract class LocalFile2DbWorker<T> extends File2DbWorker<T> {

    private static Log LOGGER = LogFactory.get();

    private String localFilePath;

    public LocalFile2DbWorker(String localFilePath){
        this.localFilePath = localFilePath;
    }

    @Override
    public File init (){
        File localFile = FileUtil.newFile(localFilePath);
        LOGGER.info("init localFile:{}",localFile.getName());
        return localFile;
    }

}
