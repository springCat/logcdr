package org.springcat.logcdr.file2db.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Getter;
import lombok.Setter;
import java.io.File;

@Getter
@Setter
public class LocalFile2DbConf<T> extends File2DbConf<T>{

    private static Log LOGGER = LogFactory.get();

    private String localFilePath;

    public LocalFile2DbConf(String localFilePath){
        this.localFilePath = localFilePath;
    }

    @Override
    public void init (){
        File localFile = FileUtil.newFile(localFilePath);
        setDateFile(localFile);
        LOGGER.info("init localFile:{}",localFile.getName());
    }

}
