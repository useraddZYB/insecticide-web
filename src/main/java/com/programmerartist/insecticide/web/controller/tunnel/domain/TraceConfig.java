package com.programmerartist.insecticide.web.controller.tunnel.domain;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by 程序员Artist on 16/3/25.
 */
public class TraceConfig {
    private int DEFAULT_REPORT_NUM = 20;

    private String traceName;                         // 链路业务名称
    private List<String> did;                         // 业务"用户"id
    private boolean stop;                             // 是否关闭上报

    private LinkedHashMap<String, String> didToAlias; // 业务"用户"id 及其 别名
    private int reportNum = DEFAULT_REPORT_NUM;       // 此业务的每个did的报告条数上限(LRU处理)

    /**
     * for json
     */
    public TraceConfig() {
    }

    /**
     *
     * @param traceName
     * @param did
     * @param stop
     * @param didToAlias
     */
    public TraceConfig(String traceName, List<String> did, boolean stop, LinkedHashMap<String, String> didToAlias) {
        this.traceName = traceName;
        this.did = did;
        this.stop = stop;
        this.didToAlias = didToAlias;
    }

    public String getTraceName() {
        return traceName;
    }

    public void setTraceName(String traceName) {
        this.traceName = traceName;
    }

    public List<String> getDid() {
        return did;
    }

    public void setDid(List<String> did) {
        this.did = did;
    }

    public LinkedHashMap<String, String> getDidToAlias() {
        return didToAlias;
    }

    public void setDidToAlias(LinkedHashMap<String, String> didToAlias) {
        this.didToAlias = didToAlias;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public int getReportNum() {
        return reportNum;
    }

    public void setReportNum(int reportNum) {
        this.reportNum = reportNum;
    }

    @Override
    public String toString() {
        return "TraceConfig{" +
                "traceName='" + traceName + '\'' +
                ", did=" + did +
                ", stop=" + stop +
                ", didToAlias=" + didToAlias +
                ", reportNum=" + reportNum +
                '}';
    }
}
