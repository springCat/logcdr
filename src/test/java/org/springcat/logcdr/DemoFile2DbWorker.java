package org.springcat.logcdr;


import cn.hutool.core.thread.GlobalThreadPool;
import lombok.Data;
import org.springcat.logcdr.file2db.core.File2DbWorker;
import org.springcat.logcdr.file2db.workerimpl.LocalFile2DbWorker;
import java.util.List;


public class DemoFile2DbWorker extends LocalFile2DbWorker<DemoFile2DbWorker.DemoEntity> {

    @Data
    class DemoEntity {
        /**
         * 书单ID
         */
        private String sheetId;
        /**
         * 身份ID
         */
        private String msisdn;
        /**
         * 图书ID
         */
        private String bookId;
        /**
         * 操作类型
         */
        private String optType;
        /**
         * 操作时间
         */
        private String optTime;
    }


    public DemoFile2DbWorker(String localFilePath) {
        super(localFilePath);
    }

    @Override
    public DemoEntity covertTo(File2DbWorker<DemoEntity> file2DbWorker, List<String> colums) {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setSheetId(colums.get(0));
        demoEntity.setMsisdn(colums.get(1));
        demoEntity.setBookId(colums.get(2));
        demoEntity.setOptType(colums.get(3));
        demoEntity.setOptTime(colums.get(4));
        LOGGER.info("convert:"+demoEntity);
        return demoEntity;
    }

    @Override
    public void save(File2DbWorker<DemoEntity> file2DbWorker, DemoEntity object) {
        LOGGER.info("save:"+ object + " thread:"+Thread.currentThread().getId());
    }

    @Override
    public void before(File2DbWorker<DemoEntity> file2DbWorker) {
        LOGGER.info("before");
    }

    @Override
    public void after(File2DbWorker<DemoEntity> file2DbWorker) {
        GlobalThreadPool.getExecutor().shutdownNow();
        LOGGER.info("after");

    }

}
