package org.springcat.logcdr.cdrlog4j;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;

/*
 * 临时文件 TMP_9X
 * TMP_           固定配置前缀
 * 9X             cdrNo
 *
 * 缓冲文件名称  TMP_springcat1550Q20210223181044.txt
 * TMP_springcat            固定配置前缀
 * 155                  ip最后3位
 * PTA74                cdrNo
 * 202103230700190331   time format
 * .txt                文件名后缀
 *
 * 正式话单文件名称结构 springcatsnsplt1559094PTA74202103230700190331.txt
 * springcatsnsplt         固定配置前缀
 * 155                 ip最后3位
 * 9094                port
 * PTA74               cdrNo
 * 202103230700190331  time format
 * .txt                文件名后缀
 */
public interface CdrLog4j {

    default void log(String conjunction){
        Object[] fieldsValue = ReflectUtil.getFieldsValue(this);
        String line = StrUtil.join(conjunction, fieldsValue);
        Log.get().info(line);
    }

    default void log(){
        log("|");
    }
}
