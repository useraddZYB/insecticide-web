package com.programmerartist.insecticide.web.controller.tunnel.domain;

import com.alibaba.fastjson.JSON;

/**
 *
 * 与客户端插件保持完全一致
 * 链路上的某一次上报调用
 *
 * Created by 程序员Artist on 16/3/26.
 *
 */
public class Report implements Comparable<Report> {

    private String traceName;   // 链路业务名称
    private String traceId;     // 每一个请求链路的唯一id
    private String did;         // 业务"用户"id
    private long timestamp;     // 调用时候打的时间戳
    private String method;      // 方法名称
    private Object jsonRequ;    // 方法参数json格式
    private Object jsonResp;    // 方法响应json格式
    private long cost;          // 方法耗时
    private String ip;

    public Report() {
    }

    public Report(String traceName, String traceId, String did, long timestamp, String method, Object jsonRequ, Object jsonResp, long cost, String ip) {
        this.traceName = traceName;
        this.traceId = traceId;
        this.did = did;
        this.timestamp = timestamp;
        this.method = method;
        this.jsonRequ = jsonRequ;
        this.jsonResp = jsonResp;
        this.cost = cost;
        this.ip = ip;
    }

    public String getTraceName() {
        return traceName;
    }

    public void setTraceName(String traceName) {
        this.traceName = traceName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getJsonRequ() {
        return jsonRequ;
    }

    public void setJsonRequ(Object jsonRequ) {
        this.jsonRequ = JSON.parse((String)jsonRequ);
    }

    public Object getJsonResp() {
        return jsonResp;
    }

    public void setJsonResp(Object jsonResp) {
        this.jsonResp = JSON.parse((String)jsonResp);
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Report report) {
        if (this.timestamp > report.timestamp) {
            return 1;
        } else if (this.timestamp < report.timestamp) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Report{" +
                "traceName='" + traceName + '\'' +
                ", traceId='" + traceId + '\'' +
                ", did='" + did + '\'' +
                ", timestamp=" + timestamp +
                ", method='" + method + '\'' +
                ", jsonRequ='" + jsonRequ + '\'' +
                ", jsonResp='" + jsonResp + '\'' +
                ", cost=" + cost +
                ", ip='" + ip + '\'' +
                '}';
    }
}