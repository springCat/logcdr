package org.springcat.logcdr.file2db.impl;

import org.springcat.logcdr.file2db.core.File2DbFactory;
import org.springcat.logcdr.file2db.core.LocalFile2DbWorker;

public class Test {

    public static void main(String[] args) {

        LocalFile2DbWorker<DemoFile2DbWorker.DemoEntity> ossFile2DbConf = new DemoFile2DbWorker("/Users/springcat/Downloads/ireadsnsplt00480819X20220510091359.txt");

        File2DbFactory.start(ossFile2DbConf);
        System.out.println();
    }
}
