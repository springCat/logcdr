package org.springcat.logcdr.file2db.core;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.GlobalThreadPool;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Data;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

/**
 *  一个生产者，多个消费者组队的模式，一般来说够用。
 *  如果要实现多个生产者的模式，可以多new几个worker组，进一步提升并发能力
 * @param <T>
 */
@Data
public abstract class File2DbWorker<T> {

    public final Log LOGGER = LogFactory.get();

    private int dbOutputNum = 5;

    private AtomicLong successNum = new AtomicLong(0);

    private AtomicLong failedNum = new AtomicLong(0);

    private File2DbBuffer buffer = new File2DbBuffer();

    private TimeInterval timeInterval = new TimeInterval();

    //context attribute，传递在全生命周期的自定义变量
    private Map<String,Object> attribute = new ConcurrentHashMap();

    //默认使用全局的threadpool，重要业务可以自定义传入thread pool
    private ExecutorService workPool = GlobalThreadPool.getExecutor();

    public void report(){
        Log.get(this.getClass()).info("file2db stop cost:{},success:{},faild:{}",timeInterval.interval(),successNum, failedNum);
    }

    public abstract T covertTo(File2DbWorker<T> file2DbWorker, List<String> colums);

    public abstract void save(File2DbWorker<T> file2DbWorker, T object);

    public abstract File init();

    public abstract void before(File2DbWorker<T> file2DbWorker);

    public abstract void after(File2DbWorker<T> file2DbWorker);
}
