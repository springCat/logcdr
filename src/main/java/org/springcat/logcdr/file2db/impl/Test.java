package org.springcat.logcdr.file2db.impl;

import org.springcat.logcdr.file2db.core.LocalFile2DbConf;

public class Test {

    public static void main(String[] args) {

        LocalFile2DbConf<DemoEntity> ossFile2DbConf = new LocalFile2DbConf<DemoEntity>("/Users/springcat/Downloads/springcatsnsplt00480819X20220510091359.txt");

        new DemoFile2DbHandler()
                .start(ossFile2DbConf);
        System.out.println();
    }
}
