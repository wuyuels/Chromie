package org.chromie.dto;

/**
 * @author liushuai7
 */
public class MonitoringData implements Monitoring{
    public static final String  R="r";
    public static final String  W="w";
    int r = 0;
    int w = 0;
    @Override
    public void add(CollectData data) {
        r = r+data.getR();
        w = w+data.getW();
    }

    @Override
    public String getData() {
        return r+"|"+w;
    }


}
