package org.springcat.logcdr.file2db.impl;


import org.springcat.logcdr.file2db.core.File2DbWorker;
import org.springcat.logcdr.file2db.core.File2DbFactory;

import java.util.List;


public class DemoFile2DbFactory implements File2DbFactory<DemoEntity> {


    @Override
    public DemoEntity covertTo(File2DbWorker<DemoEntity> file2DbWorker, List<String> colums) {
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
    public void save(File2DbWorker<DemoEntity> file2DbWorker, DemoEntity object) {
        System.out.println("save:"+ object + " thread:"+Thread.currentThread().getId());
        //save DemoLogCdr
    }

    @Override
    public void before(File2DbWorker<DemoEntity> file2DbWorker) {
        System.out.println("before");
    }

    @Override
    public void after(File2DbWorker<DemoEntity> file2DbWorker) {
        System.out.println("after");
    }

}
