package org.chromie.service;

import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.chromie.dto.CollectData;
import org.chromie.dto.MonitoringData;
import org.chromie.dto.TimeRound;
import org.chromie.util.DataUtil;
import org.chromie.util.DateUtil;
import org.chromie.util.FileUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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

    private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("chromie-schedule-pool-%d").daemon(true).build());

    public void add(CollectData data) {
        init();
        currentTimeRound.add(DateUtil.getTodayRelativeSeconds(), data);
    }

    public String getData(String date) {
        if (date.equals(this.date)) {
            //当日数据
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

    public void init() {

        if (this.currentTimeRound == null || this.date == null) {
            dataInit();
            schedulerInit();
        }
    }

    private void schedulerInit() {
        //
        final Runnable handler = new Runnable() {
            public void run() {
                MonitoringData data = currentTimeRound.getData(tag);
                //写入文件进行持久化
                FileUtil.saveData(date, tag / 60, data.getData(), (new MonitoringData()).getData());
                LOG.info("持久化...." + data.getData());
                tag += 60;
                if ( !DateFormatUtils.format(new Date(), DATE_F).equals(date)){
                    dataInit();
                }
            }
        };
        scheduler.scheduleAtFixedRate(handler, 60, 60, java.util.concurrent.TimeUnit.SECONDS);
    }

    private void dataInit() {
        //初始化 || 日切
        this.date = DateUtil.getToday();
        this.currentTimeRound = DataUtil.buildData(FileUtil.getFileData(this.date),new TimeRound<>(new MonitoringData(), 24, 60));
        this.tag = DateUtil.getTodayRelativeSeconds();
        LOG.info("初始化....");
    }

    public List<MonitoringData> getBeanData(String date) {
        if (date.equals(this.date)) {
            //当日数据
            return currentTimeRound.getAll();
        } else {
            //读取文件中的数据
            TimeRound<MonitoringData> data = DataUtil.buildData(FileUtil.getFileData(date), new TimeRound<>(new MonitoringData(), 24, 60));

            return data != null ?data.getAll():new ArrayList();
        }
    }
}
