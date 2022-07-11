package org.springcat.logcdr.file2db.core;

import cn.hutool.core.thread.GlobalThreadPool;
import lombok.Data;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Data
public abstract class File2DbWorker<T> {

    private int dbOutputNum = 2;

    private Buffer<T> buffer = new Buffer<T>();

    //context attribute，传递在全生命周期的自定义变量
    private Map<String,Object> attribute = new ConcurrentHashMap();

    //默认使用全局的threadpool，重要业务可以自定义传入thread pool
    private ExecutorService workPool = GlobalThreadPool.getExecutor();

    public abstract File init();

    public void destory(){
        GlobalThreadPool.getExecutor().shutdownNow();
    }
}
