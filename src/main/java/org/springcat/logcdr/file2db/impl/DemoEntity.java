package org.springcat.logcdr.file2db.impl;

import lombok.Data;

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