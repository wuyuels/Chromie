package org.chromie.dto;

/**
 * 监控接口
 * @author liushuai7
 */
public interface Monitoring {
     void add(CollectData data);
     String getData();
}
