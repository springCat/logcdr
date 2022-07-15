package org.springcat.logcdr.file2db.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.SneakyThrows;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * core handler 类
 */
public class File2DbFactory {

    private final static Log LOGGER = LogFactory.get();

    @SneakyThrows
    public static void start(File2DbWorker file2DbWorker){

        File dateFile = file2DbWorker.init();

        if(!FileUtil.exist(dateFile)){
            LOGGER.error("file:{} not exist",dateFile.getName());
            return;
        }

        //before hook
        file2DbWorker.before(file2DbWorker);

        File2DbBuffer buffer = file2DbWorker.getBuffer();
        int dbOutputNum = file2DbWorker.getDbOutputNum();
        ExecutorService workPool = file2DbWorker.getWorkPool();

        CountDownLatch countDownLatch = new CountDownLatch(dbOutputNum+1);
        workPool.execute(()-> {
            try {
                FileUtil.readLines(dateFile, Charset.defaultCharset(), (LineHandler) line -> {
                    try {
                        List<String> colums = StrUtil.split(line, "|");
                        buffer.put(file2DbWorker.covertTo(file2DbWorker, colums));
                    } catch (Exception e) {
                        file2DbWorker.getFailedNum().incrementAndGet();
                        LOGGER.error("File2DbHandler input error:" + e.getMessage() + " data" + line);
                    }
                });
                buffer.stop(dbOutputNum);
            } finally {
                countDownLatch.countDown();
            }
        });

        for (int i = 0; i < dbOutputNum; i++) {
            workPool.execute(()->{
                try {
                    while (true) {
                        Object data = buffer.get();
                        if (data == buffer.STOP_FLAG) {
                            break;
                        }

                        try {
                            file2DbWorker.save(file2DbWorker, data);
                            file2DbWorker.getSuccessNum().incrementAndGet();
                        } catch (Exception e){
                            LOGGER.error("File2DbHandler output error:"+e.getMessage()+" data:"+data);
                            file2DbWorker.getFailedNum().incrementAndGet();
                        }
                    }
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        file2DbWorker.report();

        //after hook
        file2DbWorker.after(file2DbWorker);


    }


}
