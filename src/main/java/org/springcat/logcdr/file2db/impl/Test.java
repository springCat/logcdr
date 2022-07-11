package org.springcat.logcdr.file2db.impl;

import org.springcat.logcdr.file2db.core.LocalFile2DbWorker;

public class Test {

    public static void main(String[] args) {

        LocalFile2DbWorker<DemoEntity> ossFile2DbConf = new LocalFile2DbWorker<DemoEntity>("/Users/springcat/Downloads/ireadsnsplt00480819X20220510091359.txt");

        new DemoFile2DbFactory()
                .start(ossFile2DbConf);
        System.out.println();
    }
}
