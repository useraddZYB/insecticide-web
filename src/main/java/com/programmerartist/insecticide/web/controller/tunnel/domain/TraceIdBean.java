package com.programmerartist.insecticide.web.controller.tunnel.domain;

import java.util.TreeSet;

/**
 * 一个链路(请求)的详细
 *
 * Created by 程序员Artist on 16/3/25.
 */
public class TraceIdBean {

    String tid;
    String enterMethod;
    TreeSet<Report> reports;

    /**
     *
     */
    public TraceIdBean() {
    }

    /**
     *
     * @param tid
     * @param enterMethod
     * @param reports
     */
    public TraceIdBean(String tid, String enterMethod, TreeSet<Report> reports) {
        this.tid = tid;
        this.enterMethod = enterMethod;
        this.reports = reports;
    }


    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public TreeSet<Report> getReports() {
        return reports;
    }

    public void setReports(TreeSet<Report> reports) {
        this.reports = reports;
    }

    public String getEnterMethod() {
        return enterMethod;
    }

    public void setEnterMethod(String enterMethod) {
        this.enterMethod = enterMethod;
    }

    @Override
    public String toString() {
        return "TraceIdBean{" +
                "tid='" + tid + '\'' +
                ", enterMethod='" + enterMethod + '\'' +
                ", reports=" + reports +
                '}';
    }
}
