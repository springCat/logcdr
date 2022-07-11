package org.springcat.logcdr.file2db.core;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.GlobalThreadPool;
import cn.hutool.log.Log;
import lombok.Data;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

@Data
public abstract class File2DbWorker<T> {

    private int dbOutputNum = 2;

    private AtomicLong successNum = new AtomicLong(0);

    private AtomicLong faildNum = new AtomicLong(0);

    private Buffer<T> buffer = new Buffer<T>();

    private TimeInterval timeInterval = new TimeInterval();

    //context attribute，传递在全生命周期的自定义变量
    private Map<String,Object> attribute = new ConcurrentHashMap();

    //默认使用全局的threadpool，重要业务可以自定义传入thread pool
    private ExecutorService workPool = GlobalThreadPool.getExecutor();

    public abstract File init();

    public void destory(){
        Log.get(this.getClass()).info("file2db stop cost:{},success:{},faild:{}",timeInterval.interval(),successNum,faildNum);
        GlobalThreadPool.getExecutor().shutdownNow();
    }
}
