package org.chromie.service;

import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.chromie.dto.CollectData;
import org.chromie.dto.MonitoringData;
import org.chromie.dto.TimeRound;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 监控服务
 * @author liushuai7
 */
public class MonitoringService {


    public static final Logger LOG = Logger.getInstance(MonitoringService.class);

    private TimeRound<MonitoringData> currentTimeRound;

    private String date;

    private long dateStart;

    private static final String DATE_F = "zzzyyMMdd";

    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);



    public void add(CollectData data){
        init();
        currentTimeRound.add((System.currentTimeMillis()-dateStart)/1000,data);
    }

    public String getData(String date){
        if (date.equals(this.date)){
            return currentTimeRound.getAllStr();
        }else {
            //读取文件中的数据
            return null;
        }
    }

    private void init(){
        //初始化 || 日切
        if (this.currentTimeRound == null || this.date == null || !DateFormatUtils.format(new Date(),DATE_F).equals(this.date)){
            this.currentTimeRound = new TimeRound<>(new MonitoringData(),24,60);
            this.date = DateFormatUtils.format(new Date(),DATE_F);

            try {
                this.dateStart  = DateUtils.parseDate(date,new String[]{DATE_F}).getTime();
            } catch (Exception e) {}
            //
            final Runnable handler = new Runnable() {
                public void run() {
                    MonitoringData data = currentTimeRound.getData((System.currentTimeMillis()-dateStart)/1000-60);
                    //写入文件进行持久化
                    String d = data.getData();
                    LOG.info("持久化...."+d);
                    System.out.println("持久化...."+d);
                }
            };
            scheduler.scheduleAtFixedRate(handler, 60, 60, java.util.concurrent.TimeUnit.SECONDS);
            LOG.info("初始化....");
            System.out.println("初始化....");
        }
    }

}
