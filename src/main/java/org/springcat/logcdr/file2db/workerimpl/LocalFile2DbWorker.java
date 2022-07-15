package org.springcat.logcdr.file2db.workerimpl;

import cn.hutool.core.io.FileUtil;
import lombok.Data;
import org.springcat.logcdr.file2db.core.File2DbWorker;

import java.io.File;

@Data
public abstract class LocalFile2DbWorker<T> extends File2DbWorker<T> {

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
