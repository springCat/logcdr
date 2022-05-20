package org.springcat.logcdr.file2db.core;

import lombok.SneakyThrows;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 简单自定的buffer，后续控速，监控可以在上面扩展，目前没有需求，先简单处理
 * @param <T>
 */
public class Buffer<T> {

    private ArrayBlockingQueue pool = new ArrayBlockingQueue(10000);

    public Object STOP_FLAG = new Object();

    @SneakyThrows
    public void put(Object data){
        pool.put(data);
    }

    @SneakyThrows
    public T get(){
        while (true) {
            Object data = pool.poll();
            if(data == null){
                continue;
            }
            return (T) data;
        }
    }

    /**
     * 毒丸
     * 因为有多个消费者，所以放多个毒丸
     * @param num
     */
    public void stop(int num){
        for (int i = 0; i < num; i++) {
            put(STOP_FLAG);
        }
    }
}
