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
 * @param <T>
 */
public interface File2DbFactory<T> {

    Log LOGGER = LogFactory.get();

    T covertTo(File2DbWorker<T> file2DbWorker, List<String> colums);

    void save(File2DbWorker<T> file2DbWorker, T object);

    void before(File2DbWorker<T> file2DbWorker);

    void after(File2DbWorker<T> file2DbWorker);

    @SneakyThrows
    default void start(File2DbWorker<T> file2DbWorker){

        File dateFile = file2DbWorker.init();

        if(!FileUtil.exist(dateFile)){
            LOGGER.error("file:{} not exist",dateFile.getName());
            return;
        }

        //before hook
        before(file2DbWorker);

        Buffer<T> buffer = file2DbWorker.getBuffer();
        int dbOutputNum = file2DbWorker.getDbOutputNum();
        ExecutorService workPool = file2DbWorker.getWorkPool();

        CountDownLatch countDownLatch = new CountDownLatch(dbOutputNum+1);
        workPool.execute(()-> {
            try {
                FileUtil.readLines(dateFile, Charset.defaultCharset(), (LineHandler) line -> {
                    try {
                        List<String> colums = StrUtil.split(line, "|");
                        T entity = covertTo(file2DbWorker, colums);
                        buffer.put(entity);
                    } catch (Exception e) {
                        file2DbWorker.getFaildNum().incrementAndGet();
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
                        T data = buffer.get();
                        if (data == buffer.STOP_FLAG) {
                            break;
                        }

                        try {
                            save(file2DbWorker, data);
                            file2DbWorker.getSuccessNum().incrementAndGet();
                        } catch (Exception e){
                            LOGGER.error("File2DbHandler output error:"+e.getMessage()+" data:"+data);
                            file2DbWorker.getFaildNum().incrementAndGet();
                        }
                    }
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        //after hook
        after(file2DbWorker);

        file2DbWorker.destory();
    }


}
