package org.springcat.logcdr.cdrlog4j;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.*;
import org.apache.logging.log4j.core.util.Loader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * 路径：REPORT/buffer/
 * 临时文件1 TMP_9X
 * TMP_           固定配置前缀
 * 9X             cdrNo
 *
 * 路径：REPORT/buffer/
 * 临时文件2  TMP_springcat1550Q20210223181044.txt
 * TMP_springcat            固定配置前缀
 * 155                  ip最后3位
 * PTA74                cdrNo
 * 202103230700190331   time format
 * .txt                文件名后缀
 *
 * 路径：REPORT/send/
 * 正式话单文件名称结构 springcatsnsplt1559094PTA74202103230700190331.txt
 * springcatsnsplt         固定配置前缀
 * 155                 ip最后3位
 * 9094                port
 * PTA74               cdrNo
 * 202103230700190331  time format
 * .txt                文件名后缀
 *
 */
@Component
public class CdrLog4jConfig {

    public static final String tempDir = "REPORT/buffer/";

    public static final String tempPrefix = "TMP_";

    public static final String officalDir = "REPORT/send/";

    public static final String officalPrefix = "springcatsnsplt";

    public static final String fileExtension = ".txt";

    private static final String ip = StrUtil.padPre(StrUtil.subAfter(NetUtil.getLocalHostName(),".",true),3,'0');

    @Value("${server.port}")
    private String port;

    public static final String implPackageName = "org.springcat.logcdr.cdrlog4j.impl";
    public static final String logPackageName = "org.springcat.logcdr.cdrlog4j";

    @PostConstruct
    public void init() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        try {
            ctx.setConfigLocation(Loader.getResource("log4j2.xml", null).toURI());

            Set<Class<?>> cdrLogList = ClassUtil.scanPackageByAnnotation(implPackageName, CdrTag.class);
            cdrLogList.stream()
                            .forEach(cdrLog ->{
                                CdrTag annotation = AnnotationUtil.getAnnotation(cdrLog, CdrTag.class);
                                if(StrUtil.isBlank(annotation.value())){
                                    LogManager.getRootLogger().error("cdrLog class:"+cdrLog.getClass()+"has no cdrlog");
                                }
                                createCdrAppender(ctx,annotation.value());
                            });
        } catch (final Exception e) {
            LogManager.getRootLogger().error("load log4j2 configuration error", e);
        }
    }

    public void createCdrAppender(LoggerContext ctx, String cdrNo){
        final org.apache.logging.log4j.core.config.Configuration config = ctx.getConfiguration();

        final TriggeringPolicy timePolicy = TimeBasedTriggeringPolicy.newBuilder()
                .withModulate(true)
                .withInterval(60)
                .build();

        final TriggeringPolicy sizePolicy = SizeBasedTriggeringPolicy.createPolicy("2 MB");

        CompositeTriggeringPolicy policy = CompositeTriggeringPolicy.createPolicy(timePolicy, sizePolicy);
        DefaultRolloverStrategy strategy = DefaultRolloverStrategy.newBuilder().withMax("1").build();

        final Appender appender = RollingFileAppender.newBuilder()
                .setName(cdrNo+"Cdrlog")
                .withFileName(tempDir+tempPrefix+cdrNo+fileExtension)
                    .withFilePattern(tempDir+officalPrefix+ip+port+cdrNo+"%d{yyyyMMddHHmmss}"+fileExtension)
                .withPolicy(policy)
                .withStrategy(strategy)
                .build();

        appender.start();
        config.addAppender(appender);

        config.getLoggerConfig(logPackageName)
                .addAppender(appender, Level.INFO,null);
        ctx.updateLoggers(config);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void uploadToOss()
    {

        List<File> waitToUploadOss = PathUtil.loopFiles(Path.of(tempDir), fileName ->
                StrUtil.isSurround(fileName.getName(), officalPrefix + ip + port, fileExtension));

        if(waitToUploadOss.size() == 0){
            return;
        }

        LogManager.getRootLogger().info("upload to oss start");
        waitToUploadOss.forEach(file ->{
            try {
                //do upload to oss
                FileUtil.del(file);
                LogManager.getRootLogger().error("delete local cdr filename:"+file.getName());
            } catch (Exception e) {
                LogManager.getRootLogger().error("update to oss err filename:"+file.getName());
            }
        });
        LogManager.getRootLogger().info("upload to oss end");
    }

}
