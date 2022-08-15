package org.chromie.util;

import org.apache.commons.lang.StringUtils;
import org.chromie.dto.CollectData;
import org.chromie.dto.Monitoring;
import org.chromie.dto.TimeRound;

/**
 * 数据处理
 *
 * @author liushuai7
 */
public class DataUtil {
    public static int getTotalTime(String data, String def) {

        if (!StringUtils.isEmpty(data)) {
            int count = 0;
            String[] datas = data.split("\n");
            for (String d : datas) {
                if (!d.equals(def)) {
                    count++;
                }
            }
            return count;
        }
        return 0;
    }

    public static int getRelativeTime(String data, String def) {
        if (!StringUtils.isEmpty(data)) {
            int count = 0;
            int max = 0;
            String[] datas = data.split("\n");
            for (String d : datas) {
                if (!d.equals(def)) {
                    count++;
                } else if (count > max) {
                    max = count;
                    count = 0;
                } else {
                    count = 0;
                }

            }
            return max;
        }
        return 0;
    }

    public static <T extends Monitoring> TimeRound<T> buildData(String data, TimeRound<T> currentTimeRound) {
        if (!StringUtils.isEmpty(data)) {
            String[] datas = data.split("\n");
            long len = 0L;
            for (String d : datas) {
                currentTimeRound.add(++len * 60 - 1, buildTemData(d));
            }
        }
        return currentTimeRound;
    }

    private static CollectData buildTemData(String data) {
        CollectData reData = new CollectData();
        if (!StringUtils.isEmpty(data)) {
            String[] datas = data.split("\\|");
            if (datas.length > 0 && datas[0] != null) {
                reData.setR(Integer.parseInt(datas[0]));
            }
            if (datas.length > 1 && datas[1] != null) {
                reData.setW(Integer.parseInt(datas[1]));
            }
        }
        return reData;
    }
}
