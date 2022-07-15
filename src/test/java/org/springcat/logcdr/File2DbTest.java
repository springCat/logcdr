package org.springcat.logcdr;

import org.junit.Test;
import org.springcat.logcdr.file2db.core.File2DbFactory;
import org.springcat.logcdr.file2db.workerimpl.LocalFile2DbWorker;

public class File2DbTest {

    @Test
    public void testLocalFile2DbWorker(){

        LocalFile2DbWorker<DemoFile2DbWorker.DemoEntity> localFile2DbWorker = new DemoFile2DbWorker("/Users/springcat/Downloads/ireadsnsplt00480819X20220510091359.txt");
        File2DbFactory.start(localFile2DbWorker);
    }
}
