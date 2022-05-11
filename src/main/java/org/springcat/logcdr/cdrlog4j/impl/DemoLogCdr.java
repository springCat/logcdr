package org.springcat.logcdr.cdrlog4j.impl;

import lombok.Builder;
import lombok.Data;
import org.springcat.logcdr.cdrlog4j.CdrLog4j;
import org.springcat.logcdr.cdrlog4j.CdrTag;

/**
 *
 * usage:
 *         DemoLogCdr build = DemoLogCdr
 *                 .builder()
 *                 .bookId("bookId")
 *                 .sheetId("sheetId")
 *                 .optType("optType")
 *                 .optTime("optTime")
 *                 .msisdn("msisdn")
 *                 .build();
 *         build.log();
 *
 *  数据严格安装定义的字段顺序打印，demo如下
 *  sheetId|msisdn|bookId|optType|optTime
 *
 *
 * 书目话单
 */
@CdrTag("cdrNo")
@Builder
@Data
public class DemoLogCdr implements CdrLog4j {

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
