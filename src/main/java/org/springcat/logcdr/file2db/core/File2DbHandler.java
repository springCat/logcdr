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
public interface File2DbHandler<T> {

    Log LOGGER = LogFactory.get();

    T covertTo(File2DbConf<T> file2DbConf,List<String> colums);

    void save(File2DbConf<T> file2DbConf,T object);

    void before(File2DbConf<T> file2DbConf);

    void after(File2DbConf<T> file2DbConf);

    @SneakyThrows
    default void start(File2DbConf<T> file2DbConf){

        file2DbConf.init();

        File dateFile = file2DbConf.getDateFile();

        if(!FileUtil.exist(dateFile)){
            LOGGER.error("file:{} not exist",dateFile.getName());
            return;
        }

        //before hook
        before(file2DbConf);

        Buffer<T> buffer = file2DbConf.getBuffer();
        int dbOutputNum = file2DbConf.getDbOutputNum();
        ExecutorService workPool = file2DbConf.getWorkPool();

        CountDownLatch countDownLatch = new CountDownLatch(dbOutputNum+1);

        workPool.execute(()-> {
            try {
                FileUtil.readLines(dateFile, Charset.defaultCharset(), (LineHandler) line -> {
                    try {
                        List<String> colums = StrUtil.split(line, "|");
                        buffer.put(covertTo(file2DbConf, colums));
                    } catch (Exception e) {
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
                        if (data == null) {
                            return;
                        }

                        try {
                            save(file2DbConf, data);
                        } catch (Exception e){
                            LOGGER.error("File2DbHandler output error:"+e.getMessage()+" data:"+data);
                        }
                    }
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        //after hook
        after(file2DbConf);

        file2DbConf.destory();
    }


}
