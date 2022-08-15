package org.chromie.service;

import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.ThrowableComputable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.chromie.dto.CollectData;
import org.chromie.dto.MonitoringData;
import org.chromie.dto.TimeRound;
import org.chromie.util.DataUtil;
import org.chromie.util.DateUtil;
import org.chromie.util.FileUtil;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 监控服务
 *
 * @author liushuai7
 */
public class MonitoringService {


    public static final Logger LOG = Logger.getInstance(MonitoringService.class);

    private TimeRound<MonitoringData> currentTimeRound;

    private String date;

    long tag = 0;

    private static final String DATE_F = "yyMMdd";

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void add(CollectData data) {
        init();
        currentTimeRound.add(DateUtil.getTodayRelativeSeconds(), data);
    }

    public String getData(String date) {
        if (date.equals(this.date)) {
            return currentTimeRound.getAllStr();
        } else {
            //读取文件中的数据
            return FileUtil.getFileData(date);
        }
    }

    public int getTotalTime(String date) {
      return DataUtil.getTotalTime(getData(date),(new MonitoringData()).getData());
    }

    public int getRelativeTime(String date) {
      return DataUtil.getRelativeTime(getData(date),(new MonitoringData()).getData());
    }

    private void init() {
        //初始化 || 日切
        if (this.currentTimeRound == null || this.date == null || !DateFormatUtils.format(new Date(), DATE_F).equals(this.date)) {
            this.date = DateFormatUtils.format(new Date(), DATE_F);
            this.currentTimeRound = DataUtil.buildData(FileUtil.getFileData(this.date),new TimeRound<>(new MonitoringData(), 24, 60));
            this.tag = DateUtil.getTodayRelativeSeconds();
            System.out.printf(getTotalTime(date)+"-----"+getRelativeTime(date));
            //
            final Runnable handler = new Runnable() {
                public void run() {
                    MonitoringData data = currentTimeRound.getData(tag);
                    //写入文件进行持久化
                    String d = data.getData();
                    FileUtil.saveData(date, tag / 60, d, (new MonitoringData()).getData());
                    LOG.info("持久化...." + d);
                    tag += 60;
                }
            };
            scheduler.scheduleAtFixedRate(handler, 60, 60, java.util.concurrent.TimeUnit.SECONDS);
            LOG.info("初始化....");
        }
    }

}
