package org.springcat.logcdr.file2db.impl;


import org.springcat.logcdr.file2db.core.File2DbConf;
import org.springcat.logcdr.file2db.core.File2DbHandler;

import java.util.List;


public class DemoFile2DbHandler implements File2DbHandler<DemoEntity> {


    @Override
    public DemoEntity covertTo(File2DbConf<DemoEntity> file2DbConf, List<String> colums) {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setSheetId(colums.get(0));
        demoEntity.setMsisdn(colums.get(1));
        demoEntity.setBookId(colums.get(2));
        demoEntity.setOptType(colums.get(3));
        demoEntity.setOptTime(colums.get(4));
        System.out.println("convert:"+demoEntity);
        return demoEntity;
    }

    @Override
    public void save(File2DbConf<DemoEntity> file2DbConf, DemoEntity object) {
        System.out.println("save:"+ object);
        //save DemoLogCdr
    }

    @Override
    public void before(File2DbConf<DemoEntity> file2DbConf) {
        System.out.println("before");
    }

    @Override
    public void after(File2DbConf<DemoEntity> file2DbConf) {
        System.out.println("after");
    }

}
