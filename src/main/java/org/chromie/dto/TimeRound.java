package org.chromie.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 监控数据
 * @author liushuai7
 */
public class TimeRound<T extends Monitoring> {

    /**
     * 节点数据
     */
    T data;
    /**
     * 子节点大小
     */
    int size;
    /**
     * 节点总数
     */
    int count;
    /**
     * 节点分片大小
     */
    int key;
    /**
     * 子节点
     */
    TimeRound<T>[] list;

    public TimeRound(T data,int... ints){
        try {
            this.data = (T) data.getClass().newInstance();
            if (ints.length >0){
                this.list = buildTimeRound(data,ints[0],ints);
                this.size = this.list.length;
                this.count = buildCount(ints);
                this.key = ints[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public TimeRound(int key,T data,int... ints){
        try {
            this.data = (T) data.getClass().newInstance();
            if (ints.length >0){
                this.list = buildTimeRound(data,ints[0],ints);
                this.size = this.list.length;
                this.count = buildCount(ints);
                this.key = ints[0]*key;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int buildCount(int[] ints) {
        int count = 1;
        for (int i:ints) {
            count =count*i;
        }
        return count;
    }

    private TimeRound<T>[] buildTimeRound(T d,int anInt, int[] ints) {
        TimeRound<T>[] data = new TimeRound[anInt];
        for (int i = 0; i <anInt; i++) {
            int[] arr2 = new int[ints.length-1];
            System.arraycopy(ints,1,arr2,0,ints.length-1);
            data[i] = new TimeRound<T>(anInt,d,arr2);
        }
        return data;
    }

    public void add(long time,CollectData d){
        try {
            data.add(d);
            if (this.size > 0 ){
                int l = (int)(time*this.key/86400)%this.size;
                this.list[l].add(time,d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get(long time) {
        return getData(time).getData();
    }
    public T getData(long time) {
        if (size == 0){
            return this.data;
        }else{
            int l = (int)(time*this.key/86400)%this.size;
            return list[l].getData(time);
        }
    }

    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        if (size == 0){
            list.add(this.data);
        }else{
            for (int i = 0; i < size; i++) {
                list.addAll(this.list[i].getAll());
            }
        }
        return list;
    }

    public String getAllStr() {
        List<T> lis = getAll();
        StringBuffer sb = new StringBuffer();
        for (T t: lis) {
            sb.append(t.getData()).append("\n");
        }
        return sb.toString();
    }


    public T getData() {
        return data;
    }
    public int getCount() {
        return count;
    }

}
